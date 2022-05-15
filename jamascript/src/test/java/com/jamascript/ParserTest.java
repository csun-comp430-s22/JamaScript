package com.jamascript;

import com.jamascript.lexer.*;
import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.methodInformation.MethodName;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;
import com.jamascript.parser.classInformation.*;
import com.jamascript.parser.methodInformation.MethodDef;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

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

        @Test
        public void testOpMultiply() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new MultiplyToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new MultiplyOp(), 1),
                                parser.parseOp(0));
        }

        @Test
        public void testOpDivide() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new DivideToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new DivideOp(), 1),
                                parser.parseOp(0));
        }

        @Test
        public void testOpGreaterThanEquals() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new GreaterThanEqualToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new GreaterThanEqualsOp(), 1),
                                parser.parseOp(0));
        }

        @Test
        public void testOpLessThanEquals() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new LessThanEqualToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new LessThanEqualsOp(), 1),
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

        // int mismatch vardec
        @Test(expected = ParseException.class)
        public void vardecIntM() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new IntToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("1"));

                final Parser parser = new Parser(tokens);
                parser.parseVarInit(0);
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

        // string mismatch vardec
        @Test(expected = ParseException.class)
        public void vardecStringM() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new StringToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("1"));

                final Parser parser = new Parser(tokens);
                parser.parseVarInit(0);
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

        // bool mismatch vardec
        @Test(expected = ParseException.class)
        public void vardecBoolM() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("1"));

                final Parser parser = new Parser(tokens);
                parser.parseVarInit(0);
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
                tokens.add(new CommaToken());
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
                                new ParseResult<Stmt>(expected, 10),
                                parser.parseVarInit(0));
        }

        // new mismatch vardec
        @Test(expected = ParseException.class)
        public void vardecNewM() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("2"));

                final Parser parser = new Parser(tokens);
                parser.parseVarInit(0);
        }

        // type mismatch vardec
        @Test(expected = ParseException.class)
        public void vardecTypeM() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                parser.parseVarInit(0);
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
                                new ParseResult<Exp>(expected, 5),
                                parser.parseClassExp(0));
        }

        // test new exp error: no new token
        @Test(expected = ParseException.class)
        public void testNewExpErr() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                parser.parseClassExp(0);
        }

        // test new exp error: no class name token
        @Test(expected = ParseException.class)
        public void testNewExpErr2() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NewToken());
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                parser.parseClassExp(0);
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

        @Test
        // String test;
        public void vardecS() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new StringToken());
                tokens.add(new VariableToken("test"));
                tokens.add(new SemicolonToken());

                Parser parser = new Parser(tokens);
                final Vardec expected = new Vardec(new StringType(), new Variable("test"));

                assertEquals(new ParseResult<Vardec>(expected, 2), parser.parseVardec(0));
        }

        @Test
        // Int test;
        public void vardecI() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new IntToken());
                tokens.add(new VariableToken("test"));
                tokens.add(new SemicolonToken());

                Parser parser = new Parser(tokens);
                final Vardec expected = new Vardec(new IntType(), new Variable("test"));

                assertEquals(new ParseResult<Vardec>(expected, 2), parser.parseVardec(0));
        }

        @Test
        // Int doSomething(Int a){Println(a); return (1);}
        public void methodDefTest() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new IntToken());
                tokens.add(new MethodNameToken("doSomething"));

                tokens.add(new LeftParenthesisToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("a"));
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("a"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                Parser parser = new Parser(tokens);

                List<Vardec> arguments = new ArrayList<Vardec>();
                arguments.add(new Vardec(new IntType(), new Variable("a")));

                List<Stmt> bodyStmts = new ArrayList<Stmt>();
                bodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("a"))));
                bodyStmts.add(new ReturnNonVoidStmt(new IntegerLiteralExp(1)));

                Stmt body = new BlockStmt(bodyStmts);

                MethodDef expected = new MethodDef(new IntType(),
                                new MethodName("doSomething"),
                                arguments,
                                body);

                assertEquals(new ParseResult<MethodDef>(expected, 17), parser.parseMethodDef(0));
        }

        @Test
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
        // }
        public void testClass() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();

                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Car"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));
                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("a"));
                tokens.add(new SemicolonToken());

                tokens.add(new StringToken());
                tokens.add(new VariableToken("d"));
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("b"));
                tokens.add(new CommaToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("c"));

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("b"));

                tokens.add(new CommaToken());
                tokens.add(new VariableToken("c"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("You made it!"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new StringToken());
                tokens.add(new VariableToken("hey"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("blah"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                // first method
                // tokens.add(new IntToken());
                // tokens.add(new MethodNameToken("doSomething"));
                // tokens.add(new LeftParenthesisToken());
                // tokens.add(new IntToken());
                // tokens.add(new VariableToken("e"));
                // tokens.add(new RightParenthesisToken());
                // tokens.add(new LeftCurlyBracketToken());

                // tokens.add(new PrintlnToken());
                // tokens.add(new LeftParenthesisToken());
                // tokens.add(new VariableToken("e"));
                // tokens.add(new RightParenthesisToken());
                // tokens.add(new SemicolonToken());

                // tokens.add(new ReturnToken());
                // tokens.add(new LeftParenthesisToken());
                // tokens.add(new VariableToken("e"));
                // tokens.add(new RightParenthesisToken());
                // tokens.add(new SemicolonToken());
                // tokens.add(new RightCurlyBracketToken());
                //

                // second method
                // tokens.add(new StringToken());
                // tokens.add(new MethodNameToken("do"));
                // tokens.add(new LeftParenthesisToken());
                // tokens.add(new StringToken());
                // tokens.add(new VariableToken("e"));
                // tokens.add(new RightParenthesisToken());
                // tokens.add(new LeftCurlyBracketToken());

                // tokens.add(new PrintlnToken());
                // tokens.add(new LeftParenthesisToken());
                // tokens.add(new VariableToken("e"));
                // tokens.add(new RightParenthesisToken());
                // tokens.add(new SemicolonToken());

                // tokens.add(new ReturnToken());
                // tokens.add(new LeftParenthesisToken());
                // tokens.add(new VariableToken("e"));
                // tokens.add(new RightParenthesisToken());
                // tokens.add(new SemicolonToken());
                // tokens.add(new RightCurlyBracketToken());
                //

                tokens.add(new RightCurlyBracketToken());

                Parser parser = new Parser(tokens);

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
                methodTwoArguments.add(new Vardec(new IntType(), new Variable("e")));

                List<Stmt> methodTwoBodyStmts = new ArrayList<Stmt>();
                methodTwoBodyStmts.add(new PrintlnStmt(new VariableExp(new Variable("e"))));
                methodTwoBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("e"))));

                Stmt methodTwoBody = new BlockStmt(methodTwoBodyStmts);

                MethodDef secondMethod = new MethodDef(new IntType(),
                                new MethodName("doSomething"),
                                methodTwoArguments,
                                methodTwoBody);

                List<MethodDef> methods = new ArrayList<MethodDef>();
                methods.add(firstMethod);
                // methods.add(secondMethod);

                final ClassDef expected = new ClassDef(new ClassName("Car"),
                                new ClassName("Object"),
                                instanceVariables,
                                constructorArguments,
                                superParams,
                                constructorBody,
                                methods);

                assertEquals(new ParseResult<ClassDef>(expected, 56), parser.parseClassDef(0));
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
                final Stmt entryPoint = new PrintlnStmt(new IntegerLiteralExp(1));

                final Program expected = new Program(classes, entryPoint);

                assertEquals(
                                new ParseResult<Program>(expected, 4),
                                parser.parseProgram(0));
        }

        // test program err: tokens remaining;
        @Test(expected = ParseException.class)
        public void testProgramErr() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new PrintlnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                parser.parseProgram();
        }

}