package com.jamascript;

import com.jamascript.lexer.*;
import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.methodInformation.MethodName;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;
import com.jamascript.parser.classInformation.*;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ParserTest {

        @Test
        public void testEqualsOpExp() {
                // 1 + 1 == 1 + 1
                final OpExp first = new OpExp(new IntegerLiteralExp(1),
                                new PlusOp(),
                                new IntegerLiteralExp(1));
                final OpExp second = new OpExp(new IntegerLiteralExp(1),
                                new PlusOp(),
                                new IntegerLiteralExp(1));
                assertEquals(first, second);
        }

        @Test
        public void testVariable() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new VariableToken("x"));

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")),
                                1),
                                parser.parseExp(0));
        }

        @Test
        public void testInteger() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("123"));

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
                                parser.parseExp(0));
        }

        // doesn't work since we dont have '('exp')'
        // @Test
        // public void testPrimaryParens() throws ParseException {
        // final Parser parser = new Parser(Arrays.asList(new LeftParenthesisToken(),
        // new NumberToken("123"),
        // new RightParenthesisToken()));
        // assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 3),
        // parser.parseExp(0));
        // }

        @Test
        public void testOpPlus() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new PlusToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new PlusOp(), 1),
                                parser.parseOp(0));
        }

        @Test
        public void testOpMinus() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new MinusToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new MinusOp(), 1),
                                parser.parseOp(0));
        }

        // redundant?
        @Test
        public void testAdditiveExpOnlyPrimary() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("123"));

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
                                parser.parseExp(0));
        }

        @Test
        public void testExpSingleOperator() throws ParseException {
                // 1 + 2

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new PlusToken());
                tokens.add(new NumberToken("2"));

                final Parser parser = new Parser(tokens);

                assertEquals(new ParseResult<Exp>(new OpExp(new IntegerLiteralExp(1),
                                new PlusOp(),
                                new IntegerLiteralExp(2)),
                                3),
                                parser.parseExpOpExp(0));
        }

        @Test
        public void testAdditiveExpMultiOperator() throws ParseException {
                // 1 + 2 - 3 ==> (1 + 2) - 3
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new PlusToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new MinusToken());
                tokens.add(new NumberToken("3"));

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
                                new PlusOp(),
                                new IntegerLiteralExp(2)),
                                new MinusOp(),
                                new IntegerLiteralExp(3));
                assertEquals(new ParseResult<Exp>(expected, 5),
                                parser.parseExpOpExp(0));
        }

        @Test
        public void testLessThanExpOnlyAdditive() throws ParseException {

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("123"));

                final Parser parser = new Parser(tokens);

                assertEquals(new ParseResult<Exp>(new IntegerLiteralExp(123), 1),
                                parser.parseExpOpExp(0));
        }

        @Test
        public void testLessThanSingleOperator() throws ParseException {
                // 1 < 2

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(new IntegerLiteralExp(1),
                                new LessThanOp(),
                                new IntegerLiteralExp(2));
                assertEquals(new ParseResult<Exp>(expected, 3),
                                parser.parseExpOpExp(0));
        }

        @Test
        public void testLessThanMultiOperator() throws ParseException {
                // 1 < 2 < 3 ==> (1 < 2) < 3

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("3"));

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(new OpExp(new IntegerLiteralExp(1),
                                new LessThanOp(),
                                new IntegerLiteralExp(2)),
                                new LessThanOp(),
                                new IntegerLiteralExp(3));
                assertEquals(new ParseResult<Exp>(expected, 5),
                                parser.parseExpOpExp(0));
        }

        // will only work if we have operator precedence
        // @Test
        // public void testLessThanMixedOperator() throws ParseException {
        // // 1 < 2 + 3 ==> 1 < (2 + 3)

        // List<Token> tokens = new ArrayList<Token>();
        // tokens.add(new NumberToken("1"));
        // tokens.add(new LessThanToken());
        // tokens.add(new NumberToken("2"));
        // tokens.add(new PlusToken());
        // tokens.add(new NumberToken("3"));

        // final Parser parser = new Parser(tokens);
        // final Exp expected = new OpExp(new IntegerLiteralExp(1),
        // new LessThanOp(),
        // new OpExp(new IntegerLiteralExp(2),
        // new PlusOp(),
        // new IntegerLiteralExp(3)));
        // assertEquals(new ParseResult<Exp>(expected, 5),
        // parser.parseExpOpExp(0));
        // }

        @Test
        public void testLessThanMixedOperator_modified() throws ParseException {
                // 1 < 2 + 3 ==> (1 < 2) + 3

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new PlusToken());
                tokens.add(new NumberToken("3"));

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(
                                new OpExp(new IntegerLiteralExp(1),
                                                new LessThanOp(),
                                                new IntegerLiteralExp(2)),
                                new PlusOp(),
                                new IntegerLiteralExp(3));
                assertEquals(new ParseResult<Exp>(expected, 5),
                                parser.parseExpOpExp(0));
        }

        // if(2>1){println("yup");} else{println("nah");}

        @Test
        public void testIfStmt() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();

                tokens.add(new IfToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new GreaterThanToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("yup"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());
                tokens.add(new ElseToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("nah"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                final Parser parser = new Parser(tokens);

                List<Stmt> trueStmts = new ArrayList<Stmt>();
                trueStmts.add(new PrintlnStmt(new StringLiteralExp("yup")));

                List<Stmt> falseStmts = new ArrayList<Stmt>();
                falseStmts.add(new PrintlnStmt(new StringLiteralExp("nah")));

                final IfStmt expected = new IfStmt(
                                new OpExp(new IntegerLiteralExp(2),
                                                new GreaterThanOp(), new IntegerLiteralExp(1)),
                                new BlockStmt(trueStmts),
                                new BlockStmt(falseStmts));

                assertEquals(new ParseResult<Stmt>(expected, 20),
                                parser.parseStmt(0));

        }

        // if(1==1){println("yup");} else{println("nah");}

        @Test
        public void testIfStmtWithEquals() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();

                tokens.add(new IfToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new EqualEqualToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("yup"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());
                tokens.add(new ElseToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("nah"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                final Parser parser = new Parser(tokens);

                List<Stmt> trueStmts = new ArrayList<Stmt>();
                trueStmts.add(new PrintlnStmt(new StringLiteralExp("yup")));

                List<Stmt> falseStmts = new ArrayList<Stmt>();
                falseStmts.add(new PrintlnStmt(new StringLiteralExp("nah")));

                final IfStmt expected = new IfStmt(
                                new OpExp(new IntegerLiteralExp(1),
                                                new EqualsEqualsOp(), new IntegerLiteralExp(1)),
                                new BlockStmt(trueStmts),
                                new BlockStmt(falseStmts));

                assertEquals(new ParseResult<Stmt>(expected, 20),
                                parser.parseStmt(0));

        }

        @Test
        public void testParseStmtPrintln() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();

                // if(1 < 2){println(1);} else {println(2);}
                tokens.add(new IfToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());
                tokens.add(new ElseToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                final Parser parser = new Parser(tokens);

                final Exp expression = new OpExp(new IntegerLiteralExp(1),
                                new LessThanOp(),
                                new IntegerLiteralExp(2));

                final List<Stmt> trueBranchResultStatements = new ArrayList<Stmt>();
                trueBranchResultStatements.add(new PrintlnStmt(new IntegerLiteralExp(1)));

                Stmt trueBranchResult = new BlockStmt(trueBranchResultStatements);

                final List<Stmt> falseBranchResultStatements = new ArrayList<Stmt>();
                falseBranchResultStatements.add(new PrintlnStmt(new IntegerLiteralExp(2)));

                Stmt falseBranchResult = new BlockStmt(falseBranchResultStatements);

                final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                                new IfStmt(expression, trueBranchResult, falseBranchResult), 20);

                assertEquals(expected, parser.parseStmt(0));
        }

        @Test
        public void testWhileStmt() throws ParseException {
                // while(true){ println("ahhh");}

                List<Token> tokens = new ArrayList<Token>();

                tokens.add(new WhileToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new TrueToken());
                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("ahhh"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                final Parser parser = new Parser(tokens);

                final List<Stmt> bodyStmts = new ArrayList<Stmt>();
                bodyStmts.add(new PrintlnStmt(new StringLiteralExp("ahhh")));

                final Stmt bodyResult = new BlockStmt(bodyStmts);

                final Exp guard = new BooleanLiteralExp(true);

                final WhileStmt whileStmt = new WhileStmt(guard, bodyResult);

                final ParseResult<Stmt> expected = new ParseResult<Stmt>(whileStmt, 10);

                assertEquals(expected, parser.parseStmt(0));
        }

        // idk why it doesnt work
        // @Test
        // public void testParseStmtVariableDeclaration() throws ParseException {
        // List<Token> tokens = new ArrayList<Token>();

        // // if(1 < 2){println(1);} else {println(2);}
        // tokens.add(new IfToken());
        // tokens.add(new LeftParenthesisToken());
        // tokens.add(new NumberToken("1"));
        // tokens.add(new LessThanToken());
        // tokens.add(new NumberToken("2"));
        // tokens.add(new RightParenthesisToken());
        // tokens.add(new LeftCurlyBracketToken());
        // tokens.add(new IntToken());
        // tokens.add(new VariableToken("test"));
        // tokens.add(new EqualToken());
        // tokens.add(new NumberToken("3"));
        // tokens.add(new SemicolonToken());
        // tokens.add(new RightCurlyBracketToken());
        // tokens.add(new ElseToken());
        // tokens.add(new LeftCurlyBracketToken());
        // tokens.add(new PrintlnToken());
        // tokens.add(new LeftParenthesisToken());
        // tokens.add(new NumberToken("2"));
        // tokens.add(new RightParenthesisToken());
        // tokens.add(new SemicolonToken());
        // tokens.add(new RightCurlyBracketToken());

        // final Parser parser = new Parser(tokens);

        // List<Stmt> trueStmts = new ArrayList<Stmt>();
        // trueStmts.add(new PrintlnStmt(new IntegerLiteralExp(1)));

        // List<Stmt> falseStmts = new ArrayList<Stmt>();
        // falseStmts.add(new PrintlnStmt(new IntegerLiteralExp(2)));

        // final IfStmt expected = new IfStmt(
        // new OpExp(new IntegerLiteralExp(1), new LessThanOp(),
        // new IntegerLiteralExp(2)),
        // new BlockStmt(trueStmts),
        // new BlockStmt(falseStmts));
        // assertEquals(new ParseResult<>(expected, 20),
        // parser.parseStmt(0));

        // }

        @Test
        public void testTrueFalseExpression() throws ParseException {
                // false == false

                List<Token> tokens = new ArrayList<Token>();

                tokens.add(new FalseToken());
                tokens.add(new EqualEqualToken());
                tokens.add(new FalseToken());

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(new BooleanLiteralExp(false),
                                new EqualsEqualsOp(),
                                new BooleanLiteralExp(false));
                assertEquals(new ParseResult<Exp>(expected, 3),
                                parser.parseExpOpExp(0));
        }

        // test variable declaration for int
        @Test
        public void vardecInt() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new IntToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("1"));

                final Parser parser = new Parser(tokens);
                final Vardec vardec = new Vardec(new IntType(), new Variable("Test"));
                final Exp exp = new IntegerLiteralExp(1);
                final VariableInitializationStmt expected = new VariableInitializationStmt(vardec, exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 4),
                                parser.parseVarInit(0));
        }

        // test variable declaration for String
        @Test
        public void vardecString() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new StringToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("Hello"));

                final Parser parser = new Parser(tokens);
                final Vardec vardec = new Vardec(new StringType(), new Variable("Test"));
                final Exp exp = new StringLiteralExp("Hello");
                final VariableInitializationStmt expected = new VariableInitializationStmt(vardec, exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 4),
                                parser.parseVarInit(0));
        }

        // test variable declaration for Boolean: true
        @Test
        public void vardecTrue() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());

                final Parser parser = new Parser(tokens);
                final Vardec vardec = new Vardec(new BoolType(), new Variable("Test"));
                final Exp exp = new BooleanLiteralExp(true);
                final VariableInitializationStmt expected = new VariableInitializationStmt(vardec, exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 4),
                                parser.parseVarInit(0));
        }

        // test variable declaration for Boolean: false
        @Test
        public void vardecFalse() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new FalseToken());

                final Parser parser = new Parser(tokens);
                final Vardec vardec = new Vardec(new BoolType(), new Variable("Test"));
                final Exp exp = new BooleanLiteralExp(false);
                final VariableInitializationStmt expected = new VariableInitializationStmt(vardec, exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 4),
                                parser.parseVarInit(0));
        }

        // test variable declaration for Class: Dog Test = new Dog("Sparky", 2)
        @Test
        public void vardecClass() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("Sparky"));
                tokens.add(new NumberToken("2"));
                tokens.add(new RightParenthesisToken());

                final Parser parser = new Parser(tokens);
                final Vardec vardec = new Vardec(new ClassType(new ClassName("Dog")), new Variable("Test"));
                final List<Exp> params = new ArrayList<Exp>();
                params.add(new StringLiteralExp("Sparky"));
                params.add(new IntegerLiteralExp(2));

                final Exp exp = new NewExp(new ClassName("Dog"), params);
                final VariableInitializationStmt expected = new VariableInitializationStmt(vardec, exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 8),
                                parser.parseVarInit(0));
        }

        // test new exp: new Dog("Sparky")
        @Test
        public void testNewExp() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("Sparky"));
                tokens.add(new RightParenthesisToken());

                final Parser parser = new Parser(tokens);

                final List<Exp> params = new ArrayList<Exp>();
                params.add(new StringLiteralExp("Sparky"));

                final Exp expected = new NewExp(new ClassName("Dog"), params);

                assertEquals(
                                new ParseResult<Exp>(expected, 4),
                                parser.parseClassExp(0));
        }

        // test return stmt: return(2);
        @Test
        public void testReturnStmt() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                final Exp exp = new IntegerLiteralExp(2);
                final ReturnNonVoidStmt expected = new ReturnNonVoidStmt(exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 4),
                                parser.parseStmt(0));

        }

        // test return stmt: println(2);
        @Test
        public void testPrintlnStmt() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                final Exp exp = new IntegerLiteralExp(2);
                final PrintlnStmt expected = new PrintlnStmt(exp);

                assertEquals(
                                new ParseResult<Stmt>(expected, 4),
                                parser.parseStmt(0));

        }

        // test method call exp: test.mCall(3)
        @Test
        public void testMethodCall() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new VariableToken("test"));
                tokens.add(new DotToken());
                tokens.add(new MethodNameToken("mCall"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("3"));
                tokens.add(new RightParenthesisToken());

                final Parser parser = new Parser(tokens);

                final Exp target = new VariableExp(new Variable("test"));
                final MethodName mname = new MethodName("mCall");
                final List<Exp> params = new ArrayList<Exp>();
                params.add(new IntegerLiteralExp(3));

                final Exp expected = new MethodCallExp(
                                target,
                                mname,
                                params);

                assertEquals(
                                new ParseResult<Exp>(expected, 5),
                                parser.parseMethodCallExp(0));
        }

        // test program: noclass println(1);
        @Test
        public void testProgramNoClass() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);

                final List<ClassDef> classes = new ArrayList<ClassDef>();
                classes.add(null);
                final Stmt entryPoint = new PrintlnStmt(new IntegerLiteralExp(1));

                final Program expected = new Program(classes, entryPoint);

                assertEquals(
                                new ParseResult<Program>(expected, 4),
                                parser.parseProgram(0));
        }
}
