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

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TypeCheckerTest {
        public static TypeChecking emptyTypechecker() throws TypeErrorException {
                return new TypeChecking(new Program(new ArrayList<ClassDef>(),
                                new ExpStmt(new IntegerLiteralExp(0))));
        }

        // test exp error: exp doesn't exist
        @Test(expected = TypeErrorException.class)
        public void testExpError() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("x"), new ClassNameType(new ClassName("Dog")));

                final Type receivedType = emptyTypechecker().typeofExp(new Exp() {
                },
                                typeEnvironment, new ClassName(""));
        }

        // test variable type ClassNameType: Dog x
        @Test
        public void testVariable() throws TypeErrorException {
                final Type expectedType = new ClassNameType(new ClassName("Dog"));
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("x"), new ClassNameType(new ClassName("Dog")));

                final Type receivedType = emptyTypechecker().typeofExp(new VariableExp(new Variable("x")),
                                typeEnvironment, new ClassName(""));
                assertEquals(expectedType, receivedType);
        }

        // test IntLiteral type Int: 1
        @Test
        public void testIntLiteral() throws TypeErrorException {
                final Type expectedType = new IntType();
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new IntegerLiteralExp(1),
                                typeEnvironment, new ClassName(""));
                assertEquals(expectedType, receivedType);
        }

        // test StringLiteral type String: "test"
        @Test
        public void testStringLiteral() throws TypeErrorException {
                final Type expectedType = new StringType();
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new StringLiteralExp("test"),
                                typeEnvironment, new ClassName(""));
                assertEquals(expectedType, receivedType);
        }

        // test BoolLiteral type Bool: true
        @Test
        public void testBooleanLiteral() throws TypeErrorException {
                final Type expectedType = new BoolType();
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new BooleanLiteralExp(true),
                                typeEnvironment, new ClassName(""));
                assertEquals(expectedType, receivedType);
        }

        // test op error: op doesn't exist
        @Test(expected = TypeErrorException.class)
        public void testOpError() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new Op() {
                                },
                                new IntegerLiteralExp(1)),
                                typeEnvironment, new ClassName(""));
        }

        // test plus op: 1 + 1
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

        // test plus op mismatch: 1 + "hey"
        @Test(expected = TypeErrorException.class)
        public void testPlusOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new PlusOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test plus op mismatch: "hey" + "hey"
        @Test(expected = TypeErrorException.class)
        public void testPlusOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new PlusOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test minus op: 1 - 1
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

        // test minus op mismatch: 1 - "hey"
        @Test(expected = TypeErrorException.class)
        public void testMinusOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new MinusOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test minus op mismatch: "hey" - "hey"
        @Test(expected = TypeErrorException.class)
        public void testMinusOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new MinusOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test multiply op: 1 * 1
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

        // test multiply op mismatch: 1 * "hey"
        @Test(expected = TypeErrorException.class)
        public void testMultiplyOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new MultiplyOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test multiply op mismatch: "hey" * "hey"
        @Test(expected = TypeErrorException.class)
        public void testMultiplyOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new MultiplyOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test divide op: 1 / 1
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

        // test divide op mismatch: 1 / "hey"
        @Test(expected = TypeErrorException.class)
        public void testDivideOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new DivideOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test divide op mismatch: "hey" / "hey"
        @Test(expected = TypeErrorException.class)
        public void testDivideOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new DivideOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test greater than op: 1 > 1
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

        // test greater than op mismatch: 1 > "hey"
        @Test(expected = TypeErrorException.class)
        public void testGreaterThanOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new GreaterThanOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test greater than op mismatch: "hey" > "hey"
        @Test(expected = TypeErrorException.class)
        public void testGreaterThanOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new GreaterThanOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test less than op: 1 < 1
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

        // test less than op mismatch: 1 < "hey"
        @Test(expected = TypeErrorException.class)
        public void testLessThanOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new LessThanOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test less than op mismatch: "hey" < "hey"
        @Test(expected = TypeErrorException.class)
        public void testLessThanOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new LessThanOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test greater than equals op: 1 >= 1
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

        // test greater than equals op mismatch: 1 >= "hey"
        @Test(expected = TypeErrorException.class)
        public void testGreaterThanEqualsOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new GreaterThanEqualsOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test greater than equals op mismatch: "hey" >= "hey"
        @Test(expected = TypeErrorException.class)
        public void testGreaterThanEqualsOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new GreaterThanEqualsOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test less than equals op 1 <= 1
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

        // test less than op mismatch: 1 <= "hey"
        @Test(expected = TypeErrorException.class)
        public void testLessThanOpEqualsM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new LessThanEqualsOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test less than op mismatch: "hey" <= "hey"
        @Test(expected = TypeErrorException.class)
        public void testLessThanOpEqualsM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new LessThanEqualsOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test equal equal op: 1 == 1
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

        // test less than op mismatch: 1 == "hey"
        @Test(expected = TypeErrorException.class)
        public void testEqualsEqualsOpM() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new IntegerLiteralExp(1),
                                new EqualsEqualsOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test less than op mismatch: "hey" == "hey"
        @Test(expected = TypeErrorException.class)
        public void testEqualsEqualsOpM2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Type receivedType = emptyTypechecker().typeofExp(new OpExp(new StringLiteralExp("hey"),
                                new EqualsEqualsOp(),
                                new StringLiteralExp("hey")),
                                typeEnvironment, new ClassName(""));
        }

        // test var init stmt: Int foo = 1
        @Test
        public void testVarInitStmt() throws TypeErrorException {
                final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();
                expectedStmt.put(new Variable("foo"), new IntType());

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Stmt varInitStmt = new VariableInitializationStmt(new Vardec(new IntType(), new Variable("foo")),
                                new IntegerLiteralExp(1));
                final Type type = new IntType();

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(varInitStmt,
                                typeEnvironment,
                                new ClassName("name"),
                                type);

                assertEquals(expectedStmt, receivedStmt);
        }

        // test stmt mismatch
        @Test(expected = TypeErrorException.class)
        public void testStmtMismatch() throws TypeErrorException {

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Stmt varInitStmt = new VariableInitializationStmt(new Vardec(new IntType(), new Variable("foo")),
                                new IntegerLiteralExp(1));
                final Type type = new IntType();

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(new Stmt() {
                },
                                typeEnvironment,
                                new ClassName("name"),
                                type);

        }

        // test while stmt:
        // while(true){
        // Println("ahh");
        // Int FooWhile = 1;
        // }
        @Test
        public void testWhileStmt() throws TypeErrorException {
                final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                final Type type = new BoolType();

                final List<Stmt> bodyStmts = new ArrayList<Stmt>();
                bodyStmts.add(new PrintlnStmt(new StringLiteralExp("ahhh")));
                bodyStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooWhile")),
                                new IntegerLiteralExp(1)));

                final Stmt whileStmt = new WhileStmt(new BooleanLiteralExp(true), new BlockStmt(bodyStmts));

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(whileStmt,
                                typeEnvironment,
                                new ClassName("name"),
                                type);

                assertEquals(expectedStmt, receivedStmt);
        }

        // test while error: guard = String
        @Test(expected = TypeErrorException.class)
        public void testWhileStmtError() throws TypeErrorException {

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                final Type type = new StringType();

                final List<Stmt> bodyStmts = new ArrayList<Stmt>();
                bodyStmts.add(new PrintlnStmt(new StringLiteralExp("ahhh")));
                bodyStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooWhile")),
                                new IntegerLiteralExp(1)));

                final Stmt whileStmt = new WhileStmt(new StringLiteralExp("value"), new BlockStmt(bodyStmts));

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(whileStmt,
                                typeEnvironment,
                                new ClassName("name"),
                                type);
        }

        // test if stmt:
        // if(1 == 1){
        // Println("yup");
        // Int FooTrue = 1;
        // } else {
        // Println("nah");
        // Int FooFalse = 1;
        // }
        @Test
        public void testIfStmt() throws TypeErrorException {
                final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();

                List<Stmt> trueStmts = new ArrayList<Stmt>();
                trueStmts.add(new PrintlnStmt(new StringLiteralExp("yup")));
                trueStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooTrue")),
                                new IntegerLiteralExp(1)));

                List<Stmt> falseStmts = new ArrayList<Stmt>();
                falseStmts.add(new PrintlnStmt(new StringLiteralExp("nah")));
                falseStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooFalse")),
                                new IntegerLiteralExp(1)));

                final Stmt ifStmt = new IfStmt(
                                new OpExp(new IntegerLiteralExp(1),
                                                new EqualsEqualsOp(), new IntegerLiteralExp(1)),
                                new BlockStmt(trueStmts),
                                new BlockStmt(falseStmts));

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(ifStmt, typeEnvironment,
                                new ClassName("name"), new IntType());

                assertEquals(expectedStmt, receivedStmt);
        }

        // test if stmt error: guard not bool
        @Test(expected = TypeErrorException.class)
        public void testIfStmtError() throws TypeErrorException {
                final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();

                List<Stmt> trueStmts = new ArrayList<Stmt>();
                trueStmts.add(new PrintlnStmt(new StringLiteralExp("yup")));
                trueStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooTrue")),
                                new IntegerLiteralExp(1)));

                List<Stmt> falseStmts = new ArrayList<Stmt>();
                falseStmts.add(new PrintlnStmt(new StringLiteralExp("nah")));
                falseStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooFalse")),
                                new IntegerLiteralExp(1)));

                final Stmt ifStmt = new IfStmt(
                                new OpExp(new IntegerLiteralExp(1),
                                                new PlusOp(), new IntegerLiteralExp(1)),
                                new BlockStmt(trueStmts),
                                new BlockStmt(falseStmts));

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(ifStmt, typeEnvironment,
                                new ClassName("name"), new IntType());

                assertEquals(expectedStmt, receivedStmt);
        }

        // test block stmt:
        // {
        // Println(1);
        // Int FooBlock = 1;
        // }
        @Test
        public void testBlockStmt() throws TypeErrorException {
                final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                final List<Stmt> stmts = new ArrayList<Stmt>();
                stmts.add(new PrintlnStmt(new IntegerLiteralExp(1)));
                stmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("FooBlock")),
                                new IntegerLiteralExp(1)));
                final BlockStmt blockStmt = new BlockStmt(stmts);
                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfBlock(blockStmt,
                                typeEnvironment, new ClassName("name"),
                                (Type) new IntType());

                assertEquals(expectedStmt, receivedStmt);
        }

        // test non void stmt: return(Int FooNonVoid);
        @Test
        public void TestNonVoid() throws TypeErrorException {
                final Map<Variable, Type> expectedStmt = new HashMap<Variable, Type>();
                expectedStmt.put(new Variable("FooNonVoid"), new IntType());

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("FooNonVoid"), new IntType());
                final ReturnNonVoidStmt nonVoid = new ReturnNonVoidStmt(new VariableExp(new Variable("FooNonVoid")));

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(nonVoid,
                                typeEnvironment, new ClassName("name"), new IntType());

                assertEquals(expectedStmt, receivedStmt);
        }

        // test non void error: no return type
        @Test(expected = TypeErrorException.class)
        public void TestNonVoidError() throws TypeErrorException {

                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("FooNonVoid"), new IntType());
                final ReturnNonVoidStmt nonVoid = new ReturnNonVoidStmt(new VariableExp(new Variable("FooNonVoid")));

                final Map<Variable, Type> receivedStmt = emptyTypechecker().returnEnvOfStmt(nonVoid,
                                typeEnvironment, new ClassName("name"), null);

        }

        // test in scope variable
        @Test
        public void testVariableInScope() throws TypeErrorException {
                final Type expectedType = new IntType();
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("x"), new IntType());

                final Type receivedType = emptyTypechecker().typeofVariable(new VariableExp(new Variable("x")),
                                typeEnvironment);
                assertEquals(expectedType, receivedType);
        }

        // test out of scope variable
        @Test(expected = TypeErrorException.class)
        public void testVariableOutOfScope() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                emptyTypechecker().typeofVariable(new VariableExp(new Variable("x")),
                                typeEnvironment);
        }

        // program with class FOR TESTING
        public static TypeChecking methodCallTypechecker() throws TypeErrorException {
                List<ClassDef> classList = new ArrayList<ClassDef>();
                classList.add(parentClass());
                return new TypeChecking(new Program(classList, new ExpStmt(new IntegerLiteralExp(1))));
        }

        public static ClassDef parentClass() throws TypeErrorException {
                // Car Class Non-Overloaded
                // class Car extends Object{
                // Int a;
                // String d;
                // constructor(Int b, Boolean c){
                // super(b, c);
                // Println("You made it!");
                // String hey = "blah";
                // }
                // Int doSomething(Int e){
                // Println(e);
                // return (e);
                // }
                // String doSomething2(String e){
                // Println(e);
                // return (e);
                // }
                // }
                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new IntType(), new Variable("a")));
                instanceVariables.add(new Vardec(new StringType(), new Variable("d")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("b")));
                constructorArguments.add(new Vardec(new BoolType(), new Variable("c")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new VariableExp(new Variable("b")));
                superParams.add(new VariableExp(new Variable("c")));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new PrintlnStmt(new StringLiteralExp("You made it!")));
                constructorBody.add(new VariableInitializationStmt(new Vardec(new StringType(), new Variable("hey")),
                                new StringLiteralExp("blah")));

                // methods
                List<Vardec> methodOneArguments = new ArrayList<Vardec>();
                methodOneArguments.add(new Vardec(new IntType(), new Variable("e")));

                List<Stmt> methodOneBodyStmts = new ArrayList<Stmt>();
                methodOneBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodOneBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodOneBody = new BlockStmt(methodOneBodyStmts);

                MethodDef firstMethod = new MethodDef(new IntType(),
                                new MethodName("doSomething"),
                                methodOneArguments,
                                methodOneBody);

                List<Vardec> methodTwoArguments = new ArrayList<Vardec>();
                methodTwoArguments.add(new Vardec(new StringType(), new Variable("e")));

                List<Stmt> methodTwoBodyStmts = new ArrayList<Stmt>();
                methodTwoBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodTwoBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodTwoBody = new BlockStmt(methodTwoBodyStmts);

                MethodDef secondMethod = new MethodDef(new StringType(),
                                new MethodName("doSomething2"),
                                methodTwoArguments,
                                methodTwoBody);

                List<MethodDef> methods = new ArrayList<MethodDef>();
                methods.add(firstMethod);
                methods.add(secondMethod);

                final ClassDef carClass = new ClassDef(new ClassName("Car"),
                                new ClassName("Object"),
                                instanceVariables,
                                constructorArguments,
                                superParams,
                                constructorBody,
                                methods);

                return carClass;
        }

        public static ClassDef subClass() throws TypeErrorException {
                // Car Class Non-Overloaded
                // class Honda extends Car{
                // Int a;
                // String d;
                // constructor(Int b, Boolean c){
                // super(b, c);
                // Println("You made it!");
                // String hey = "blah";
                // }
                // Int doSomething(Int e){
                // Println(e);
                // return (e);
                // }
                // String doSomething2(String e){
                // Println(e);
                // return (e);
                // }
                // }
                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new IntType(), new Variable("a")));
                instanceVariables.add(new Vardec(new StringType(), new Variable("d")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("b")));
                constructorArguments.add(new Vardec(new BoolType(), new Variable("c")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new VariableExp(new Variable("b")));
                superParams.add(new VariableExp(new Variable("c")));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new PrintlnStmt(new StringLiteralExp("You made it!")));
                constructorBody.add(new VariableInitializationStmt(new Vardec(new StringType(), new Variable("hey")),
                                new StringLiteralExp("blah")));

                // methods
                List<Vardec> methodOneArguments = new ArrayList<Vardec>();
                methodOneArguments.add(new Vardec(new IntType(), new Variable("e")));

                List<Stmt> methodOneBodyStmts = new ArrayList<Stmt>();
                methodOneBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodOneBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodOneBody = new BlockStmt(methodOneBodyStmts);

                MethodDef firstMethod = new MethodDef(new IntType(),
                                new MethodName("doSomething"),
                                methodOneArguments,
                                methodOneBody);

                List<Vardec> methodTwoArguments = new ArrayList<Vardec>();
                methodTwoArguments.add(new Vardec(new StringType(), new Variable("e")));

                List<Stmt> methodTwoBodyStmts = new ArrayList<Stmt>();
                methodTwoBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodTwoBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodTwoBody = new BlockStmt(methodTwoBodyStmts);

                MethodDef secondMethod = new MethodDef(new StringType(),
                                new MethodName("doSomething2"),
                                methodTwoArguments,
                                methodTwoBody);

                List<MethodDef> methods = new ArrayList<MethodDef>();
                methods.add(firstMethod);
                methods.add(secondMethod);

                final ClassDef carClass = new ClassDef(new ClassName("Honda"),
                                new ClassName("Car"),
                                instanceVariables,
                                constructorArguments,
                                superParams,
                                constructorBody,
                                methods);

                return carClass;
        }

        // test method def error: unknown class
        @Test(expected = TypeErrorException.class)
        public void testMethodDefError1() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("myCar"), new ClassType(new ClassName("Car")));

                Exp target = new VariableExp(new Variable("myCar"));
                MethodName methodName = new MethodName("doSomething");
                List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(1));

                final Type receivedType = emptyTypechecker().typeofExp(
                                new MethodCallExp(target, methodName, params),
                                typeEnvironment, new ClassName(""));
        }

        // test method def error: unknown method
        @Test(expected = TypeErrorException.class)
        public void testMethodDefError2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("myCar"), new ClassType(new ClassName("Car")));

                Exp target = new VariableExp(new Variable("myCar"));
                MethodName methodName = new MethodName("notHere");
                List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(1));

                final Type receivedType = methodCallTypechecker().typeofExp(
                                new MethodCallExp(target, methodName, params),
                                typeEnvironment, new ClassName(""));
        }

        // test method call exp : myCar.doSomething(1);
        @Test
        public void testMethodCallExp() throws TypeErrorException {
                final Type expectedType = new IntType();
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("myCar"), new ClassType(new ClassName("Car")));

                Exp target = new VariableExp(new Variable("myCar"));
                MethodName methodName = new MethodName("doSomething");
                List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(1));

                final Type receivedType = methodCallTypechecker().typeofExp(
                                new MethodCallExp(target, methodName, params),
                                typeEnvironment, new ClassName(""));
                assertEquals(expectedType, receivedType);
        }

        // test method call exp error: myCar.doSomething(1, "value"); wrong # of params
        @Test(expected = TypeErrorException.class)
        public void testMethodCallExpError() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("myCar"), new ClassType(new ClassName("Car")));

                Exp target = new VariableExp(new Variable("myCar"));
                MethodName methodName = new MethodName("doSomething");
                List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(1));
                params.add(new StringLiteralExp("value"));

                final Type receivedType = methodCallTypechecker().typeofExp(
                                new MethodCallExp(target, methodName, params),
                                typeEnvironment, new ClassName(""));
        }

        // test method call exp error: 1.doSomething(1); method call on non class
        @Test(expected = TypeErrorException.class)
        public void testMethodCallExpError2() throws TypeErrorException {
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
                typeEnvironment.put(new Variable("myCar"), new ClassType(new ClassName("Car")));

                Exp target = new IntegerLiteralExp(1);
                MethodName methodName = new MethodName("doSomething");
                List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(1));

                final Type receivedType = methodCallTypechecker().typeofExp(
                                new MethodCallExp(target, methodName, params),
                                typeEnvironment, new ClassName(""));
        }

        // test new exp: new Car(1, true)
        @Test
        public void testNewExp() throws TypeErrorException {
                final Type expectedType = new ClassType(new ClassName("Car"));
                final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();

                ClassName className = new ClassName("Car");
                List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(1));
                params.add(new BooleanLiteralExp(true));

                final Type receivedType = methodCallTypechecker().typeofExp(
                                new NewExp(className, params),
                                typeEnvironment, new ClassName(""));
                assertEquals(expectedType, receivedType);
        }

        // test program error: no such class that it extends from
        @Test(expected = TypeErrorException.class)
        public void getClassError() throws TypeErrorException {

                List<ClassDef> classList = new ArrayList<ClassDef>();
                classList.add(subClass());
                new TypeChecking(new Program(classList, new ExpStmt(new IntegerLiteralExp(1))));
        }

        // // program with class FOR TESTING
        // public static TypeChecking isEqualOrSubtypeTypechecker() throws TypeErrorException {
        //         List<ClassDef> classList = new ArrayList<ClassDef>();
        //         classList.add(parentClass());
        //         classList.add(subClass());
        //         return new TypeChecking(new Program(classList, new ExpStmt(new IntegerLiteralExp(1))));
        // }

        // // test class is equal or subtype don't actually know how to hit it normally
        // @Test //(expected = NullPointerException.class)
        // public void testClassIsEqualOrSubtype() throws TypeErrorException {
        //         ClassType first = new ClassType(new ClassName("Car"));
        //         ClassType second = new ClassType(new ClassName("Honda"));
        //         isEqualOrSubtypeTypechecker().isEqualOrSubtypeOf(first, second);
        // }

        // program with class FOR TESTING
        public static TypeChecking methodCallTypechecker2() throws TypeErrorException {
                List<ClassDef> classList = new ArrayList<ClassDef>();
                classList.add(parentClassOverloaded());
                return new TypeChecking(new Program(classList, new ExpStmt(new IntegerLiteralExp(1))));
        }

        public static ClassDef parentClassOverloaded() throws TypeErrorException {
                // Car class overloaded
                // class Car extends Object{
                // Int a;
                // String d;
                // constructor(Int b, Boolean c){
                // super(b, c);
                // Println("You made it!");
                // String hey = "blah";
                // }
                // Int doSomething(Int e){
                // Println(e);
                // return (e);
                // }
                // String doSomething(String e){
                // Println(e);
                // return (e);
                // }
                // }
                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new IntType(), new Variable("a")));
                instanceVariables.add(new Vardec(new StringType(), new Variable("d")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("b")));
                constructorArguments.add(new Vardec(new BoolType(), new Variable("c")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new VariableExp(new Variable("b")));
                superParams.add(new VariableExp(new Variable("c")));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new PrintlnStmt(new StringLiteralExp("You made it!")));
                constructorBody.add(new VariableInitializationStmt(new Vardec(new StringType(), new Variable("hey")),
                                new StringLiteralExp("blah")));

                // methods
                List<Vardec> methodOneArguments = new ArrayList<Vardec>();
                methodOneArguments.add(new Vardec(new IntType(), new Variable("e")));

                List<Stmt> methodOneBodyStmts = new ArrayList<Stmt>();
                methodOneBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodOneBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodOneBody = new BlockStmt(methodOneBodyStmts);

                MethodDef firstMethod = new MethodDef(new IntType(),
                                new MethodName("doSomething"),
                                methodOneArguments,
                                methodOneBody);

                List<Vardec> methodTwoArguments = new ArrayList<Vardec>();
                methodTwoArguments.add(new Vardec(new StringType(), new Variable("e")));

                List<Stmt> methodTwoBodyStmts = new ArrayList<Stmt>();
                methodTwoBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodTwoBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodTwoBody = new BlockStmt(methodTwoBodyStmts);

                MethodDef secondMethod = new MethodDef(new StringType(),
                                new MethodName("doSomething"),
                                methodTwoArguments,
                                methodTwoBody);

                List<MethodDef> methods = new ArrayList<MethodDef>();
                methods.add(firstMethod);
                methods.add(secondMethod);

                final ClassDef carClass = new ClassDef(new ClassName("Car"),
                                new ClassName("Object"),
                                instanceVariables,
                                constructorArguments,
                                superParams,
                                constructorBody,
                                methods);

                return carClass;
        }


        // @Test(expected = TypeErrorException.class)
        // public void testDuplicateMethodSignatures() throws TypeErrorException {

        // final Type expectedType = new IntType();
        // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        // typeEnvironment.put(new Variable("husky"), new ClassType(new
        // ClassName("Test")));

        // Exp target = new VariableExp(new Variable("husky"));
        // MethodName methodName = new MethodName("test");
        // List<Exp> params = new ArrayList<Exp>();
        // params.add(new IntegerLiteralExp(1));

        // final Type receivedType = methodCallTypechecker2().typeofExp(
        // new MethodCallExp(target, methodName, params),
        // typeEnvironment, new ClassName("Test"));
        // assertEquals(expectedType, receivedType);
        // }

        // // test method call exp : husky.test(true);
        // @Test
        // public void testOverloading1() throws TypeErrorException {

        // final Type expectedType = new BoolType();
        // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        // typeEnvironment.put(new Variable("husky"), new ClassType(new
        // ClassName("Test")));

        // Exp target = new VariableExp(new Variable("husky"));
        // MethodName methodName = new MethodName("test");
        // List<Exp> params = new ArrayList<Exp>();
        // params.add(new BooleanLiteralExp(true));

        // final Type receivedType = methodCallTypechecker().typeofExp(
        // new MethodCallExp(target, methodName, params),
        // typeEnvironment, new ClassName("Test"));
        // assertEquals(expectedType, receivedType);
        // }

        // // test method call exp : husky.test(1);
        // @Test
        // public void testOverloading2() throws TypeErrorException {

        // final Type expectedType = new IntType();
        // final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        // typeEnvironment.put(new Variable("husky"), new ClassType(new
        // ClassName("Test")));

        // Exp target = new VariableExp(new Variable("husky"));
        // MethodName methodName = new MethodName("test");
        // List<Exp> params = new ArrayList<Exp>();
        // params.add(new IntegerLiteralExp(1));

        // final Type receivedType = methodCallTypechecker().typeofExp(
        // new MethodCallExp(target, methodName, params),
        // typeEnvironment, new ClassName("Test"));
        // assertEquals(expectedType, receivedType);
        // }

        // NOT ALLOWED
        // public int hey(int x) {
        // return 2;
        // }

        // public boolean hey(int x) {
        // return true;
        // }

        // ALLOWED
        // public int hey(int x) {
        // return 2;
        // }

        // public boolean hey() {
        // return true;
        // }
        // Method overloading only allows duplicate names if parameters are different

}
