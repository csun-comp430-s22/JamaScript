package com.jamascript.typechecker;

import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.methodInformation.MethodDef;
import com.jamascript.parser.methodInformation.MethodName;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;
import com.jamascript.parser.classInformation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class TypeChecking {
    public static final String BASE_CLASS_NAME = "Object";
    public final Program program;

    public final Map<ClassName, ClassDef> classes;

    public final Map<ClassName, Map<MethodName, MethodDef>> methods;

    public static ClassDef getClass(final ClassName className,
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        if (className.name.equals(BASE_CLASS_NAME)) {
            return null;
        } else {
            final ClassDef classDef = classes.get(className);
            if (classDef == null) {
                throw new TypeErrorException("no such class: " + className);
            } else {
                return classDef;
            }
        }
    }

    public ClassDef getClass(final ClassName className) throws TypeErrorException {
        return getClass(className, classes);
    }

    public static ClassDef getParent(final ClassName className,
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final ClassDef classDef = getClass(className, classes);
        return getClass(classDef.extendsClassName, classes);
    }

    public ClassDef getParent(final ClassName className) throws TypeErrorException {
        return getParent(className, classes);
    }

    public static void assertInheritanceNonCyclicalForClass(final ClassDef classDef,
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final Set<ClassName> seenClasses = new HashSet<ClassName>();
        seenClasses.add(classDef.className);
        ClassDef parentClassDef = getParent(classDef.className, classes);
        while (parentClassDef != null) {
            final ClassName parentClassName = parentClassDef.className;
            if (seenClasses.contains(parentClassName)) {
                throw new TypeErrorException("cyclic inheritance involving: " + parentClassName);
            }
            seenClasses.add(parentClassName);
            parentClassDef = getParent(parentClassName, classes);
        }
    }

    public static void assertInheritanceNonCyclical(final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        for (final ClassDef classDef : classes.values()) {
            assertInheritanceNonCyclicalForClass(classDef, classes);
        }
    }

    public static Map<MethodName, MethodDef> methodsForClass(final ClassName className,
            final Map<ClassName, ClassDef> classes) throws TypeErrorException {
        final ClassDef classDef = getClass(className, classes);
        if (classDef == null) {
            return new HashMap<MethodName, MethodDef>();
        } else {
            final Map<MethodName, MethodDef> retval = methodsForClass(classDef.extendsClassName, classes);
            final Set<MethodName> methodsOnThisClass = new HashSet<MethodName>();
            for (final MethodDef methodDef : classDef.methods) {
                final MethodName methodName = methodDef.mname;
                if (methodsOnThisClass.contains(methodName)) {
                    throw new TypeErrorException("duplicate method: " + methodName);
                }
                methodsOnThisClass.add(methodName);
                retval.put(methodName, methodDef);
            }
            return retval;
        }
    }

    public static Map<ClassName, Map<MethodName, MethodDef>> makeMethodMap(final Map<ClassName, ClassDef> classes)
            throws TypeErrorException {
        final Map<ClassName, Map<MethodName, MethodDef>> retval = new HashMap<ClassName, Map<MethodName, MethodDef>>();
        for (final ClassName className : classes.keySet()) {
            retval.put(className, methodsForClass(className, classes));
        }
        return retval;
    }

    // also makes sure inheritance hierarchies aren't cyclical
    public static Map<ClassName, ClassDef> makeClassMap(final List<ClassDef> classes) throws TypeErrorException {
        final Map<ClassName, ClassDef> retval = new HashMap<ClassName, ClassDef>();
        for (final ClassDef classDef : classes) {
            final ClassName className = classDef.className;
            if (retval.containsKey(classDef.className)) {
                throw new TypeErrorException("Duplicate class name: " + className);
            }
            retval.put(className, classDef);
        }

        assertInheritanceNonCyclical(retval);

        return retval;
    }

    public List<Type> expectedConstructorTypesForClass(final ClassName className)
        throws TypeErrorException {
        final ClassDef classDef = getClass(className);
        final List<Type> retval = new ArrayList<Type>();
        if (classDef == null) { // Object
            return retval;
        } else {
            for (final Vardec vardec : classDef.constructorArguments) {
                retval.add(vardec.type);
            }
            return retval;
        }
    }

    public void expressionsOk(final List<Type> expectedTypes,
                              final List<Exp> receivedExpressions,
                              final Map<Variable, Type> typeEnvironment,
                              final ClassName classWeAreIn) throws TypeErrorException {
        if (expectedTypes.size() != receivedExpressions.size()) {
            throw new TypeErrorException("Wrong number of parameters");
        }
        for (int index = 0; index < expectedTypes.size(); index++) {
            final Type paramType = typeofExp(receivedExpressions.get(index), typeEnvironment, classWeAreIn);
            final Type expectedType = expectedTypes.get(index);
            // myMethod(int, bool, int)
            // myMethod(  2, true,   3)
            //
            // myMethod2(BaseClass)
            // myMethod2(new SubClass())
            isEqualOrSubtypeOf(paramType, expectedType);
        }
    }

    public List<Type> expectedParameterTypesForClassAndMethod(final ClassName className,
                                                              final MethodName methodName)
        throws TypeErrorException {
        final MethodDef methodDef = getMethodDef(className, methodName);
        final List<Type> retval = new ArrayList<Type>();
        for (final Vardec vardec : methodDef.arguments) {
            retval.add(vardec.type);
        }
        return retval;
    }

    public MethodDef getMethodDef(final ClassName className,
                                  final MethodName methodName) throws TypeErrorException {
        final Map<MethodName, MethodDef> methodMap = methods.get(className);
        if (methodMap == null) {
            throw new TypeErrorException("Unknown class name: " + className);
        } else {
            final MethodDef methodDef = methodMap.get(methodName);
            if (methodDef == null) {
                throw new TypeErrorException("Unknown method name " + methodName + " for class " + className);
            } else {
                return methodDef;
            }
        }
    }

    public Type expectedReturnTypeForClassAndMethod(final ClassName className,
                                                    final MethodName methodName) throws TypeErrorException {
        return getMethodDef(className, methodName).returnType;
    }

    public TypeChecking(final Program program) throws TypeErrorException {
        this.program = program;
        classes = makeClassMap(program.classes);
        methods = makeMethodMap(classes);
    }

    // find type of expressions
    public Type typeofExp(final Exp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn)
            throws TypeErrorException {
        if (exp instanceof VariableExp) {
            return typeofVariable((VariableExp) exp, typeEnvironment);
        } else if (exp instanceof IntegerLiteralExp) {
            return new IntType();
        } else if (exp instanceof StringLiteralExp) {
            return new StringType();
        } else if (exp instanceof BooleanLiteralExp) {
            return new BoolType();
        } else if (exp instanceof OpExp) {
            return typeofOp((OpExp) exp, typeEnvironment, classWeAreIn);
        } else if (exp instanceof MethodCallExp) {
            return typeofMethodCall((MethodCallExp) exp, typeEnvironment, classWeAreIn);
        } else if (exp instanceof NewExp) {
            return typeofNew((NewExp) exp, typeEnvironment, classWeAreIn);
        } else {
            throw new TypeErrorException("Unrecognized expression: " + exp);
        }
    }

    // type of variable
    public Type typeofVariable(final VariableExp exp,
            final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
        final Type mapType = typeEnvironment.get(exp.variable);
        if (mapType == null) {
            throw new TypeErrorException("Used variable not in scope: " + exp.variable.name);
        } else {
            return mapType;
        }
    }

    // type of operators
    public Type typeofOp(final OpExp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type leftType = typeofExp(exp.left, typeEnvironment, classWeAreIn);
        final Type rightType = typeofExp(exp.right, typeEnvironment, classWeAreIn);
        if (exp.op instanceof PlusOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for +");
            }
        } else if (exp.op instanceof MinusOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for -");
            }
        } else if (exp.op instanceof MultiplyOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for *");
            }
        } else if (exp.op instanceof DivideOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for /");
            }
        } else if (exp.op instanceof GreaterThanOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Operand type mismatch for >");
            }
        } else if (exp.op instanceof LessThanOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Operand type mismatch for <");
            }
        } else if (exp.op instanceof GreaterThanEqualsOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Operand type mismatch for >=");
            }
        } else if (exp.op instanceof LessThanEqualsOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Operand type mismatch for <=");
            }
        } else if (exp.op instanceof EqualsEqualsOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Operand type mismatch for ==");
            }
        } else {
            throw new TypeErrorException("Unsupported operation: " + exp.op);
        }
    }

    // type of method call
    public Type typeofMethodCall(final MethodCallExp exp,
                                 final Map<Variable, Type> typeEnvironment,
                                 final ClassName classWeAreIn) throws TypeErrorException {
        final Type targetType = typeofExp(exp.target, typeEnvironment, classWeAreIn);
        if (targetType instanceof ClassType) {
            final ClassType asClassType = (ClassType)targetType;
            exp.targetType = asClassType;
            final ClassName className = asClassType.className;
            final List<Type> expectedTypes =
                expectedParameterTypesForClassAndMethod(className, exp.methodName);
            expressionsOk(expectedTypes, exp.params, typeEnvironment, classWeAreIn);
            return expectedReturnTypeForClassAndMethod(className, exp.methodName);
        } else {
            throw new TypeErrorException("Called method on non-class type: " + targetType);
        }
    }


    // type of new
    public Type typeofNew(final NewExp exp,
                          final Map<Variable, Type> typeEnvironment,
                          final ClassName classWeAreIn) throws TypeErrorException {
        // need to know what the constructor arguments for this class are
        final List<Type> expectedTypes = expectedConstructorTypesForClass(exp.className);
        expressionsOk(expectedTypes, exp.params, typeEnvironment, classWeAreIn);
        return new ClassType(exp.className);
    }

    // type of stmt
    public Map<Variable, Type> returnEnvOfStmt(final Stmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (stmt instanceof VariableInitializationStmt) {
            return returnEnvOfVarInit((VariableInitializationStmt) stmt,
                    typeEnvironment, classWeAreIn);
        } else if (stmt instanceof WhileStmt) {
            return returnEnvOfWhile((WhileStmt) stmt, typeEnvironment,
                    classWeAreIn, functionReturnType);
        } else if (stmt instanceof IfStmt) {
            return returnEnvOfIf((IfStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof PrintlnStmt) {
            return returnEnvPrintln((PrintlnStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof BlockStmt) {
            return returnEnvOfBlock((BlockStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof ReturnNonVoidStmt) {
            return returnEnvOfReturnNonVoid((ReturnNonVoidStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else {
            throw new TypeErrorException("no such stmt" + stmt);
        }
    }

    // helper
    public void isEqualOrSubtypeOf(final Type first, final Type second) throws TypeErrorException {
        if (first.equals(second)) {
            return;
        } else if (first instanceof ClassType &&
                second instanceof ClassType) {
            final ClassDef parentClassDef = getParent(((ClassType) first).className);
            isEqualOrSubtypeOf(new ClassType(parentClassDef.className), second);
        } else {
            throw new TypeErrorException("incompatible types: " + first + ", " + second);
        }
    }

    // helper
    public static Map<Variable, Type> addToMap(final Map<Variable, Type> map,
            final Variable variable,
            final Type type) {
        final Map<Variable, Type> result = new HashMap<Variable, Type>();
        result.putAll(map);
        result.put(variable, type);
        return result;
    }

    // type of var init stmt
    public Map<Variable, Type> returnEnvOfVarInit(final VariableInitializationStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type expType = typeofExp(stmt.exp, typeEnvironment, classWeAreIn);
        isEqualOrSubtypeOf(expType, stmt.vardec.type);
        return addToMap(typeEnvironment, stmt.vardec.variable, stmt.vardec.type);
    }

    // type of while stmt
    public Map<Variable, Type> returnEnvOfWhile(final WhileStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (typeofExp(stmt.guard, typeEnvironment, classWeAreIn) instanceof BoolType) {
            returnEnvOfStmt(stmt.body, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {
            throw new TypeErrorException("guard on while is not a boolean: " + stmt);
        }
    }

    // type of if stmt
    public Map<Variable, Type> returnEnvOfIf(final IfStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (typeofExp(stmt.guard, typeEnvironment, classWeAreIn) instanceof BoolType) {
            returnEnvOfStmt(stmt.trueBranch, typeEnvironment, classWeAreIn, functionReturnType);
            returnEnvOfStmt(stmt.falseBranch, typeEnvironment, classWeAreIn, functionReturnType);

            return typeEnvironment;
        } else {
            throw new TypeErrorException("guard of if is not a boolean: " + stmt);
        }
    }

    // type of println stmt
    public Map<Variable, Type> returnEnvPrintln(final PrintlnStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        typeofExp(((PrintlnStmt) stmt).exp, typeEnvironment, classWeAreIn);
        return typeEnvironment;
    }

    // type of block stmt
    public Map<Variable, Type> returnEnvOfBlock(final BlockStmt stmt,
            final Map<Variable, Type> initialEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        Map<Variable, Type> typeEnvironment = initialEnvironment;
        for (Stmt bodyStmt : stmt.stmts) {
            typeEnvironment = returnEnvOfStmt(bodyStmt, typeEnvironment, classWeAreIn, functionReturnType);
        }
        return initialEnvironment;
    }

    // type of Return NON VOID stmt
    public Map<Variable, Type> returnEnvOfReturnNonVoid(final ReturnNonVoidStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (functionReturnType == null) {
            throw new TypeErrorException("return in program entry point");
        } else {
            final Type receivedType = typeofExp(stmt.exp, typeEnvironment, classWeAreIn);
            isEqualOrSubtypeOf(receivedType, functionReturnType);
            return typeEnvironment;
        }
    }
}
