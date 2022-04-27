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
import java.util.HashMap;
import java.util.ArrayList;

public class TypeChecking {
    // Things to track:
    // 1.) Variables in scope, and their types
    // 2.) Classes available, parameters constructors take, methods they have, what
    // their parent
    // class is.
    //
    // Sorts of queries we want to make to class information:
    // 1. Is this a valid class?
    // 2. For this class, what are the argument types for the constructor?
    // 3. Does this class support a given method? If so, what are the parameter
    // types for the method?
    // - Need to take inheritance into account
    // 4. Is this given class a subclass of another class?
    // 5. Does our class hierarchy form a tree?

    public final List<ClassDef> classes;
    public final Program program;

    // recommended: ClassName -> All Methods on the Class
    // recommended: ClassName -> ParentClass
    public TypeChecking(final Program program) {
        this.program = program;
        this.classes = program.classes;
        // TODO: check that class hierarchy is a tree
    }

    public Type typeofVariable(final VariableExp exp,
            final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
        final Type mapType = typeEnvironment.get(exp.variable);
        if (mapType == null) {
            throw new TypeErrorException("Used variable not in scope: " + exp.variable.name);
        } else {
            return mapType;
        }
    }

    public Type typeofThis(final ClassName classWeAreIn) throws TypeErrorException {
        if (classWeAreIn == null) {
            throw new TypeErrorException("this used in the entry point");
        } else {
            return new ClassNameType(classWeAreIn);
        }
    }

    public Type typeofOp(final OpExp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type leftType = typeof(exp.left, typeEnvironment, classWeAreIn);
        final Type rightType = typeof(exp.right, typeEnvironment, classWeAreIn);
        // (leftType, exp.op, rightType) match {
        // case (IntType, PlusOp, IntType) => IntType
        // case (IntType, LessThanOp | EqualsOp, IntType) => Booltype
        // case _ => throw new TypeErrorException("Operator mismatch")
        // }
        if (exp.op instanceof PlusOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for +");
            }
        } else if (exp.op instanceof LessThanOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Operand type mismatch for <");
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

    // Find if the method exists in the class
    // If the method exists in the given class, return the type
    public Type expectedReturnTypeForClassAndMethod(final ClassName className,
            final MethodName mname) throws TypeErrorException { 
        
        for(ClassDef classDef : classes) {
            if(classDef.className.equals(className)) {
                for(MethodDef mdef : classDef.methods) {
                    if(mname.equals(mdef.mname)) {
                        return mdef.returnType;
                    }
                }
            }
        }
        throw new TypeErrorException("Class " + className + " or method " + mname + " does not exist in this context");
    }

    // Doesn't handle access modifiers right now; would be to know which class we
    // are calling from.
    //
    // class Base extends Object {
    // public void basePublic() {}
    // protected void baseProtected() {}
    // private void basePrivate() {}
    // }
    // class Sub extends Base {
    // public void foobar() {
    // this.basePublic(); // should be ok
    // this.baseProtected(); // should be ok
    // this.basePrivate(); // should give an error
    // }
    // }
    // class SomeOtherClass extends Object {
    // public void test() {
    // Sub sub = new Sub();
    // sub.basePublic(); // should be ok
    // sub.baseProtected(); // should give an error
    // sub.basePrivate(); // should give an error
    // }
    // }
    //
    // doesn't handle inherited methods
    // for every class:
    // - Methods on that class
    // - Methods on the parent of that class
    public List<Type> expectedParameterTypesForClassAndMethod(final ClassName className,
            final MethodName mname)
            throws TypeErrorException {
        for (final ClassDef candidateClass : classes) {
            if (candidateClass.className.equals(className)) {
                for (final MethodDef candidateMethod : candidateClass.methods) {
                    if (candidateMethod.mname.equals(mname)) {
                        final List<Type> expectedTypes = new ArrayList<Type>();
                        for (final Vardec vardec : candidateMethod.arguments) {
                            expectedTypes.add(vardec.type);
                        }
                        return expectedTypes;
                    }
                }
            }
        }

        throw new TypeErrorException("No method named " + mname + " on class " + className);
    }

    // Animal -> Dog
    // Is Dog a subtype of Animal?
    // first -> Dog
    // second -> Animal
    public boolean isSubtypeOf(final Type first, final Type second) throws TypeErrorException {
        
        if(first instanceof ClassNameType && second instanceof ClassNameType) {
            ClassNameType childClassType = (ClassNameType) first;
            ClassNameType parentClassType = (ClassNameType) second;

            for(ClassDef classDef : classes) {
                if(childClassType.className.equals(classDef.className)) {
                    if(classDef.extendsClassName == parentClassType.className) {
                        return true;
                    }
                    return false;
                }
            }
        }

        throw new TypeErrorException("First parameter class not found or parameters not instance of ClassNameType.");
    }

    public void isEqualOrSubtypeOf(final Type first, final Type second) throws TypeErrorException {
        if (!(first.equals(second) || isSubtypeOf(first, second))) {
            throw new TypeErrorException("types incompatible: " + first + ", " + second);
        }
    }

    // List<Type> - expected types
    // List<Exp> - received expressions
    // Check if parameters match the recieved expressions
    public void expressionsOk(final List<Type> expectedTypes,
            final List<Exp> receivedExpressions,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        if (expectedTypes.size() != receivedExpressions.size()) {
            throw new TypeErrorException("Wrong number of parameters");
        }
        for (int index = 0; index < expectedTypes.size(); index++) {
            final Type paramType = typeof(receivedExpressions.get(index), typeEnvironment, classWeAreIn);
            final Type expectedType = expectedTypes.get(index);
            // myMethod(int, bool, int)
            // myMethod( 2, true, 3)
            //
            // myMethod2(BaseClass)
            // myMethod2(new SubClass())
            isEqualOrSubtypeOf(paramType, expectedType);
        }
    }

    // 1.) target should be a class.
    // 2.) target needs to have the methodname method
    // 3.) need to know the expected parameter types for the method
    //
    // exp.methodname(exp*)
    // target.mname(params)
    public Type typeofMethodCall(final MethodCallExp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        final Type targetType = typeof(exp.target, typeEnvironment, classWeAreIn);
        if (targetType instanceof ClassNameType) {
            final ClassName className = ((ClassNameType) targetType).className;
            final List<Type> expectedTypes = expectedParameterTypesForClassAndMethod(className, exp.mname);
            expressionsOk(expectedTypes, exp.params, typeEnvironment, classWeAreIn);
            return expectedReturnTypeForClassAndMethod(className, exp.mname);
        } else {
            throw new TypeErrorException("Called method on non-class type: " + targetType);
        }
    }

    public List<Type> expectedConstructorTypesForClass(final ClassName className)
            throws TypeErrorException {

        for(ClassDef classDef : classes) {
            if(classDef.className.equals(className)) {
                final List<Type> expectedTypes = new ArrayList<Type>();
                for(Vardec vardec : classDef.constructorArguments) {
                    expectedTypes.add(vardec.type);
                }
                return expectedTypes;
            }
        }
        throw new TypeErrorException("Class not found from expected constructor types: " + className);
    }

    // new classname(exp*)
    // new className(params)
    public Type typeofNew(final NewExp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        // need to know what the constructor arguments for this class are
        final List<Type> expectedTypes = expectedConstructorTypesForClass(exp.className);
        expressionsOk(expectedTypes, exp.params, typeEnvironment, classWeAreIn);
        return new ClassNameType(exp.className);
    }

    // classWeAreIn is null if we are in the entry point
    public Type typeof(final Exp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        if (exp instanceof IntegerLiteralExp) {
            return new IntType();
        } else if (exp instanceof VariableExp) {
            return typeofVariable((VariableExp) exp, typeEnvironment);
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

    public static Map<Variable, Type> addToMap(final Map<Variable, Type> map,
            final Variable variable,
            final Type type) {
        final Map<Variable, Type> result = new HashMap<Variable, Type>();
        result.putAll(map);
        result.put(variable, type);
        return result;
    }

    public Map<Variable, Type> isWellTypedVar(final VariableInitializationStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        final Type expType = typeof(stmt.exp, typeEnvironment, classWeAreIn);
        
        // Animal dog -> Vardec
        // new Dog() -> Exp
        // Animal dog = new Dog(); -> VariableInitializationStmt
        isEqualOrSubtypeOf(expType, stmt.vardec.type);
        return addToMap(typeEnvironment, stmt.vardec.variable, stmt.vardec.type);
    }

    public Map<Variable, Type> isWellTypedIf(final IfStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (typeof(stmt.guard, typeEnvironment, classWeAreIn) instanceof BoolType) {
            isWellTypedStmt(stmt.trueBranch, typeEnvironment, classWeAreIn, functionReturnType);
            isWellTypedStmt(stmt.falseBranch, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {
            throw new TypeErrorException("guard of if is not a boolean: " + stmt);
        }
    }

    public Map<Variable, Type> isWellTypedWhile(final WhileStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (typeof(stmt.guard, typeEnvironment, classWeAreIn) instanceof BoolType) {
            isWellTypedStmt(stmt.body, typeEnvironment, classWeAreIn, functionReturnType);
            return typeEnvironment;
        } else {
            throw new TypeErrorException("guard on while is not a boolean: " + stmt);
        }
    }

    public Map<Variable, Type> isWellTypedBlock(final BlockStmt stmt,
            Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        for (final Stmt bodyStmt : stmt.stmts) {
            typeEnvironment = isWellTypedStmt(bodyStmt, typeEnvironment, classWeAreIn, functionReturnType);
        }
        return typeEnvironment;
    }

    // return exp;
    public Map<Variable, Type> isWellTypedReturnNonVoid(final ReturnNonVoidStmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (functionReturnType == null) {
            throw new TypeErrorException("return in program entry point");
        } else {
            final Type receivedType = typeof(stmt.exp, typeEnvironment, classWeAreIn);
            isEqualOrSubtypeOf(receivedType, functionReturnType);
            return typeEnvironment;
        }
    }

    public Map<Variable, Type> isWellTypedPrintln(final PrintlnStmt stmt, final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        // if (functionReturnType == null) {
        // throw new TypeErrorException("return in program entry point");
        // } else {
        // return typeEnvironment;
        // ((PrintlnStmt) stmt).exp
        // }
        if (typeof(stmt.exp, typeEnvironment, classWeAreIn) instanceof PrintlnStmt) {
            return typeEnvironment;
        } else {
            throw new TypeErrorException("not println" + stmt);
        }
    }

    // bool x = true;
    // while (true) {
    // int x = 17;
    // break;
    // }
    public Map<Variable, Type> isWellTypedStmt(final Stmt stmt,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn,
            final Type functionReturnType) throws TypeErrorException {
        if (stmt instanceof VariableInitializationStmt) {
            return isWellTypedVar((VariableInitializationStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof IfStmt) {
            return isWellTypedIf((IfStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof WhileStmt) {
            return isWellTypedWhile((WhileStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else if (stmt instanceof ReturnNonVoidStmt) {
            return isWellTypedReturnNonVoid((ReturnNonVoidStmt) stmt, typeEnvironment, classWeAreIn,
                    functionReturnType);
        } else if (stmt instanceof PrintlnStmt) {
            return isWellTypedPrintln((PrintlnStmt) stmt, typeEnvironment, classWeAreIn,
                    functionReturnType);
        } else if (stmt instanceof BlockStmt) {
            return isWellTypedBlock((BlockStmt) stmt, typeEnvironment, classWeAreIn, functionReturnType);
        } else {
            throw new TypeErrorException("Unsupported statement: " + stmt);
        }
    }

    // methoddef ::= type methodname(vardec*) stmt
    public void isWellTypedMethodDef(final MethodDef method,
            Map<Variable, Type> typeEnvironment, // instance variables
            final ClassName classWeAreIn) throws TypeErrorException {
        // starting type environment: just instance variables
        // int addTwo(int x, int y) { return x + y; }
        //
        // int x;
        // int addTwo(bool x, int x) { return x; }
        for (final Vardec vardec : method.arguments) {
            // odd semantics: last variable declaration shadows prior one
            typeEnvironment = addToMap(typeEnvironment, vardec.variable, vardec.type);
        }

        isWellTypedStmt(method.body,
                typeEnvironment, // instance variables + parameters
                classWeAreIn,
                method.returnType);
    }

    // classdef ::= class classname extends classname {
    // vardec*; // comma-separated instance variables
    // constructor(vardec*) {
    // super(exp*);
    // stmt* // comma-separated
    // }
    // methoddef*
    // }

    // -Check constructor
    // -Check methods
    public void isWellTypedClassDef(final ClassDef classDef) throws TypeErrorException {
        // TODO: add instance variables from parent classes; currently broken
        // weird: duplicate instance variables
        // class MyClass extends Object {
        // int x;
        // bool x;
        // ...
        // }
        Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        for (final Vardec vardec : classDef.instanceVariables) {
            typeEnvironment = addToMap(typeEnvironment, vardec.variable, vardec.type);
        }

        // check constructor
        Map<Variable, Type> constructorTypeEnvironment = typeEnvironment;
        for (final Vardec vardec : classDef.constructorArguments) {
            constructorTypeEnvironment = addToMap(constructorTypeEnvironment, vardec.variable, vardec.type);
        }
        // check call to super
        expressionsOk(expectedConstructorTypesForClass(classDef.extendsClassName),
                classDef.superParams,
                constructorTypeEnvironment,
                classDef.className);
        isWellTypedBlock(new BlockStmt(classDef.constructorBody),
                constructorTypeEnvironment,
                classDef.className,
                new VoidType());

        // check methods
        // TODO - this is broken - doesn't check for methods with duplicate names
        //
        // int foo(int x) { ... }
        // int foo(bool b) { ... }
        for (final MethodDef method : classDef.methods) {
            isWellTypedMethodDef(method,
                    typeEnvironment,
                    classDef.className);
        }
    }

    // program ::= classdef* stmt
    public void isWellTypedProgram() throws TypeErrorException {
        for (final ClassDef classDef : program.classes) {
            isWellTypedClassDef(classDef);
        }

        isWellTypedStmt(program.entryPoint,
                new HashMap<Variable, Type>(),
                null,
                null);
    }
}
