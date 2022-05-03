package com.jamascript;

import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.classInformation.*;
import com.jamascript.parser.methodInformation.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.*;
import com.jamascript.typechecker.types.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Map;

import com.jamascript.typechecker.TypeChecking;
import com.jamascript.lexer.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TypeCheckerTest {
    public static TypeChecking emptyTypechecker() throws TypeErrorException {
        return new TypeChecking(new Program(new ArrayList<ClassDef>(),
                new ExpStmt(new IntegerLiteralExp(0))));
    }

    // test variable type ClassNameType
    @Test
    public void testVariable() throws TypeErrorException {
        final Type expectedType = new ClassNameType(new ClassName("Dog"));
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        typeEnvironment.put(new Variable("x"), new ClassNameType(new ClassName("Dog")));

        final Type receivedType = emptyTypechecker().typeofExp(new VariableExp(new Variable("x")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test IntLiteral type Int
    @Test
    public void testIntLiteral() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new IntegerLiteralExp(1),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test StringLiteral type String
    @Test
    public void testStringLiteral() throws TypeErrorException {
        final Type expectedType = new StringType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new StringLiteralExp("test"),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test BoolLiteral type Bool
    @Test
    public void testBooleanLiteral() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new BooleanLiteralExp(true),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test plus op
    @Test
    public void testPlusOp() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new PlusOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test plus op mismatch
    @Test(expected = TypeErrorException.class)
    public void testPlusOpM() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new PlusOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test minus op
    @Test
    public void testMinusOp() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new MinusOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test minus op mismatch
    @Test(expected = TypeErrorException.class)
    public void testMinusOpM() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new MinusOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test multiply op
    @Test
    public void testMultiplyOp() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new MultiplyOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test multiply op mismatch
    @Test(expected = TypeErrorException.class)
    public void testMultiplyOpM() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new MultiplyOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test divide op
    @Test
    public void testDivideOp() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new DivideOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test divide op
    @Test(expected = TypeErrorException.class)
    public void testDivideOpM() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new DivideOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test greater than op
    @Test
    public void testGreaterThanOp() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new GreaterThanOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test greater than op mismatch
    @Test(expected = TypeErrorException.class)
    public void testGreaterThanOpM() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new GreaterThanOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test less than op
    @Test
    public void testLessThanOp() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new LessThanOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test less than op mismatch
    @Test(expected = TypeErrorException.class)
    public void testLessThanOpM() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new LessThanOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test greater than equals op
    @Test
    public void testGreaterThanEqualOp() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new GreaterThanEqualsOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test greater than equals op mismatch
    @Test(expected = TypeErrorException.class)
    public void testGreaterThanEqualsOpM() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new GreaterThanEqualsOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test less than equals op
    @Test
    public void testLessThanEqualsOp() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new LessThanEqualsOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test less than op mismatch
    @Test(expected = TypeErrorException.class)
    public void testLessThanOpEqualsM() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new LessThanEqualsOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test equal equal op
    @Test
    public void testEqualsEqualsOp() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new EqualsEqualsOp(),
                new IntegerLiteralExp(1)),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test less than op mismatch
    @Test(expected = TypeErrorException.class)
    public void testEqualsEqualsOpM() throws TypeErrorException {
        final Type expectedType = new BoolType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                new EqualsEqualsOp(),
                new StringLiteralExp("hey")),
                typeEnvironment, new ClassName(""));
        assertEquals(expectedType, receivedType);
    }

    // test var init stmt
    @Test
    public void testVarInitStmt() throws TypeErrorException {
        final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();
        expectedStmt.put(new Variable("foo"), new IntType());

        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

        final Stmt varInitStmt = new VariableInitializationStmt(new Vardec(new IntType(), new Variable("foo")),
                new IntegerLiteralExp(1));
        final Type type = new IntType();

        final Map<Variable, Type> receivedStmt = emptyTypechecker().typeOfStmt(varInitStmt,
                typeEnvironment,
                new ClassName("name"),
                type);

        assertEquals(expectedStmt, receivedStmt);
    }

    // test while stmt
    // @Test
    // public void testWhileStmt() throws TypeErrorException {
    // final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();

    // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
    // final Type type = new BoolType();

    // final List<Stmt> bodyStmts = new ArrayList<Stmt>();
    // bodyStmts.add(new PrintlnStmt(new StringLiteralExp("ahhh")));

    // final Stmt bodyResult = new BlockStmt(bodyStmts);

    // final Exp guard = new BooleanLiteralExp(true);

    // final Stmt whileStmt = new WhileStmt(guard, bodyResult);

    // final Map<Variable, Type> receivedStmt =
    // emptyTypechecker().typeOfStmt(whileStmt,
    // typeEnvironment,
    // new ClassName("name"),
    // type);

    // assertEquals(new IntegerLiteralExp(1), receivedStmt);
    // }

    // test if stmt
    // @Test
    // public void testIfStmt() throws TypeErrorException {
    // List<Stmt> trueStmts = new ArrayList<Stmt>();
    // trueStmts.add(new PrintlnStmt(new StringLiteralExp("yup")));

    // List<Stmt> falseStmts = new ArrayList<Stmt>();
    // falseStmts.add(new PrintlnStmt(new StringLiteralExp("nah")));

    // final Stmt ifStmt = new IfStmt(
    // new OpExp(new IntegerLiteralExp(1),
    // new EqualsEqualsOp(), new IntegerLiteralExp(1)),
    // new BlockStmt(trueStmts),
    // new BlockStmt(falseStmts));

    // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

    // final Map<Variable, Type> expectedStmt =
    // emptyTypechecker().typeOfStmt(ifStmt, typeEnvironment,
    // new ClassName("name"), new IntType());

    // assertEquals(expectedStmt, new StringLiteralExp("value"));
    // }

    // test block stmt
    @Test
    public void testBlockStmt() throws TypeErrorException {
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        final List<Stmt> stmts = new ArrayList<Stmt>();
        stmts.add(new PrintlnStmt(new IntegerLiteralExp(1)));
        final BlockStmt blockStmt = new BlockStmt(stmts);
        final Map<Variable, Type> expectedStmt = emptyTypechecker().typeOfBlock(blockStmt,
                typeEnvironment, new ClassName("name"),
                (Type)new IntType());
        assertEquals(new IntegerLiteralExp(1), expectedStmt);
    }

    // @Test
    // public void testVariableInScope() throws TypeErrorException {
    // final Type expectedType = new IntType();
    // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
    // typeEnvironment.put(new Variable("x"), new IntType());

    // final Type receivedType =
    // emptyTypechecker().typeofVariable(new VariableExp(new Variable("x")),
    // typeEnvironment);
    // assertEquals(expectedType, receivedType);
    // }

    // @Test (expected = TypeErrorException.class)
    // public void testVariableOutOfScope() throws TypeErrorException {
    // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
    // emptyTypechecker().typeofVariable(new VariableExp(new Variable("x")),
    // typeEnvironment);
    // }

    // @Test
    // public void testThisInClass() throws TypeErrorException {
    // assertEquals(new ClassNameType(new ClassName("foo")),
    // emptyTypechecker().typeofThis(new ClassName("foo")));
    // }

    // @Test(expected = TypeErrorException.class)
    // public void testThisNotInClass() throws TypeErrorException {
    // emptyTypechecker().typeofThis(null);
    // }
}
