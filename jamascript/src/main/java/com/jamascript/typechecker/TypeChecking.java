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
    public final Program program;

    public final List<ClassDef> classes;

    public TypeChecking(final Program program) {
        this.program = program;
        this.classes = program.classes;
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
        if (targetType instanceof ClassNameType) {
            final ClassName className = ((ClassNameType) targetType).className;
            // final List<Type> expectedTypes =

        }
        throw new TypeErrorException("message");
    }

    // type of new
    public Type typeofNew(final NewExp exp,
            final Map<Variable, Type> typeEnvironment,
            final ClassName classWeAreIn) throws TypeErrorException {
        throw new TypeErrorException("message");
    }

}
