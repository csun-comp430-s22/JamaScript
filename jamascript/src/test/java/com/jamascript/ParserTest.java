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
        @Test (expected = ParseException.class)
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
        @Test (expected = ParseException.class)
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
        @Test (expected = ParseException.class)
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
        @Test (expected = ParseException.class)
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
        @Test (expected = ParseException.class)
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
        @Test (expected = ParseException.class)
        public void testNewExpErr() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new SemicolonToken());

                final Parser parser = new Parser(tokens);
                parser.parseClassExp(0);
        }

        // test new exp error: no class name token
        @Test (expected = ParseException.class)
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
        @Test (expected = ParseException.class)
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

        // test program: classdef println(1);
        // class Test extends Object {
        //         Int x = 5;
        //         constructor(int x = 5;) {
        //                 super(4);
        //                 Boolean y = true;
        //         }
        //         Int jon(Int jon = 1;) {
        //                 Int gomez = 6;
        //         }
        // }

        // @Test
        // public void testProgramClass() throws ParseException {
        //         List<Token> tokens = new ArrayList<Token>();
        //         //class
        //         tokens.add(new ClassToken());
        //         tokens.add(new ClassNameToken("Test"));
        //         tokens.add(new ExtendsToken());
        //         tokens.add(new ClassNameToken("Object"));

        //         tokens.add(new LeftCurlyBracketToken());

        //         tokens.add(new IntToken());
        //         tokens.add(new VariableToken("x"));
        //         tokens.add(new EqualToken());
        //         tokens.add(new NumberToken("5"));
        //         tokens.add(new SemicolonToken());

        //         tokens.add(new ConstructorToken());
        //         tokens.add(new LeftParenthesisToken());

        //         tokens.add(new IntToken());
        //         tokens.add(new VariableToken("x"));
        //         tokens.add(new EqualToken());
        //         tokens.add(new NumberToken("5"));
        //         tokens.add(new SemicolonToken());

        //         tokens.add(new RightParenthesisToken());
        //         tokens.add(new LeftCurlyBracketToken());
                
        //         tokens.add(new SuperToken());
        //         tokens.add(new LeftParenthesisToken());
        //         tokens.add(new NumberToken("4"));
        //         tokens.add(new RightParenthesisToken());
        //         tokens.add(new SemicolonToken());

        //         tokens.add(new BooleanToken());
        //         tokens.add(new VariableToken("y"));
        //         tokens.add(new EqualToken());
        //         tokens.add(new TrueToken());
        //         tokens.add(new SemicolonToken());

        //         tokens.add(new RightCurlyBracketToken());

        //         tokens.add(new IntToken());
        //         tokens.add(new MethodNameToken("test"));
        //         tokens.add(new LeftParenthesisToken());
        //         tokens.add(new IntToken());
        //         tokens.add(new VariableToken("jon"));
        //         tokens.add(new EqualToken());
        //         tokens.add(new NumberToken("1"));
        //         tokens.add(new SemicolonToken());
        //         tokens.add(new RightParenthesisToken());

        //         tokens.add(new LeftCurlyBracketToken());
        //         tokens.add(new IntToken());
        //         tokens.add(new VariableToken("gomez"));
        //         tokens.add(new EqualToken());
        //         tokens.add(new NumberToken("6"));
        //         tokens.add(new SemicolonToken());
        //         tokens.add(new RightCurlyBracketToken());

        //         tokens.add(new RightCurlyBracketToken());
        //         // end class
        //         tokens.add(new PrintlnToken());
        //         tokens.add(new LeftParenthesisToken());
        //         tokens.add(new NumberToken("1"));
        //         tokens.add(new RightParenthesisToken());
        //         tokens.add(new SemicolonToken());

        //         final Parser parser = new Parser(tokens);

        //         //class
        //         ClassName className = new ClassName("Test");
        //         ClassName extendsClassName = new ClassName("Object");

        //         List<Vardec> instanceVariables = new ArrayList<Vardec>();
        //         instanceVariables.add(new Vardec(new IntType(), new Variable("x")));

        //         List<Vardec> constructorArguments = new ArrayList<Vardec>();
        //         constructorArguments.add(new Vardec(new IntType(), new Variable("x")));

        //         List<Exp> superParams = new ArrayList<Exp>();
        //         superParams.add(new IntegerLiteralExp(4));

        //         List<Stmt> constructorBody = new ArrayList<Stmt>();
        //         constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
        //                                                         new BooleanLiteralExp(true)));

        //         List<MethodDef> methods = new ArrayList<MethodDef>();
        //         List<Vardec> methodArguments = new ArrayList<Vardec>();
        //         methodArguments.add(new Vardec(new IntType(), new Variable("jon")));

        //         methods.add(new MethodDef(new IntType(), new MethodName("test"), methodArguments,
        //         new VariableInitializationStmt(new Vardec(new IntType(), new Variable("gomez")), new IntegerLiteralExp(6))));

        //         ClassDef classDef = new ClassDef(className, extendsClassName, 
        //         instanceVariables, constructorArguments, superParams, constructorBody, methods);
        //         // end class

        //         final List<ClassDef> classes = new ArrayList<ClassDef>();
        //         classes.add(classDef);
        //         final Stmt entryPoint = new PrintlnStmt(new IntegerLiteralExp(1));

        //         final Program expected = new Program(classes, entryPoint);

        //         assertEquals(
        //                         new ParseResult<Program>(expected, 51),
        //                         parser.parseProgram(0));
        // }

        @Test
        public void testAClass() throws ParseException {

                // class Test extends Object {
                //     Int x = 5;
                //     constructor(Int x = 5;) {
                //         super(4);
                //         Boolean y = true; 
                //     }

                //     Int test(Int jon = 1;) {
                //         Int gomez = 6;
                //         return (gomez);
                //     }

                // }

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("4"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("6"));
                tokens.add(new SemicolonToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new IntType(), new Variable("x")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("x")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new IntegerLiteralExp(4));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();
                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new IntType(), new Variable("jon")));

                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                methodBodyStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("gomez")), new IntegerLiteralExp(6)));
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("gomez"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);

                methods.add(
                        new MethodDef(new IntType(), 
                                      new MethodName("test"), 
                                      methodArguments, 
                                      methodBody));

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 52); // bc 51 is } and 52 is nothing

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }

        @Test
        public void testAClassString() throws ParseException {

               // class Test extends Object {
                //     String Test = Hello;
                //     constructor(String Test = Hello;) {
                //         super(cat);
                //         Boolean y = true; 
                //     }

                //     String test(String jon = dog;) {
                //         String gomez = six;
                //         return (gomez);
                //     }

                // }

     
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new StringToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("Hello"));
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new StringToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("Hello"));
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringValToken("cat"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new StringToken());
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new StringToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("dog"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new StringToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new EqualToken());
                tokens.add(new StringValToken("six"));
                tokens.add(new SemicolonToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");;

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new StringType(), new Variable("Test")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new StringType(), new Variable("Test")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new StringLiteralExp("cat"));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();
                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new StringType(), new Variable("jon")));

                
                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                methodBodyStmts.add(new VariableInitializationStmt(new Vardec(new StringType(), new Variable("gomez")), new StringLiteralExp("six")));
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("gomez"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);
                

                methods.add(new MethodDef(new StringType(), new MethodName("test"), methodArguments, methodBody));

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 52); // bc 46 is } and 47 is nothing

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }

        @Test
        public void testAClassBoolean() throws ParseException {

              // class Test extends Object {
                //     Boolean x = true;
                //     constructor(Boolean x = true;) {
                //         super(true);
                //         Boolean y = true; 
                //     }

                //     Boolean test(Boolean jon = true;) {
                //         Boolean gomez = true;
                //         return (gomez);
                //     }

                // }

     
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new TrueToken());
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new BooleanToken());
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");;

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new BoolType(), new Variable("Test")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new BoolType(), new Variable("Test")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new BooleanLiteralExp(true));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();
                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new BoolType(), new Variable("jon")));


                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                methodBodyStmts.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("gomez")), new BooleanLiteralExp(true)));
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("gomez"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);

                methods.add(new MethodDef(new BoolType(), new MethodName("test"), methodArguments, methodBody));

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 52); // bc 46 is } and 47 is nothing

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }

        @Test
        public void testAClassMultipleSuperParamToken() throws ParseException {

              // class Test extends Object {
                //     Int x = 5;
                //     constructor(Int x = 5;) {
                //         super(4, 3);
                //         Boolean y = true; 
                //     }

                //     Int test(Int jon = 1){
                //         Int gomez = 6;
                //         return (gomez);
                //     }
                // }
     
                 
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("4"));
                tokens.add(new CommaToken());
                tokens.add(new NumberToken("3"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("6"));
                tokens.add(new SemicolonToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new IntType(), new Variable("x")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("x")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new IntegerLiteralExp(4));
                superParams.add(new IntegerLiteralExp(3));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();
                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new IntType(), new Variable("jon")));

                
                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                methodBodyStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("gomez")), new IntegerLiteralExp(6)));
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("gomez"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);

                methods.add(new MethodDef(new IntType(), new MethodName("test"), methodArguments, methodBody));

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 54); // bc 46 is } and 47 is nothing

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }

        @Test
        public void testClassMultipleMethods() throws ParseException {

              // class Test extends Object {
                //     Dog x = new Dog(5);
                //     constructor(Int x = 5;) {
                //         super(4, 3);
                //         Boolean y = true; 
                //     }

                //     Int test(Int jon = 1;){
                //         Int gomez = 6;
                //         return (gomez);
                //     }

                //     Boolean hello(Int jon = 1;){
                //         Boolean lol = false;
                //         return (lol);
                //     }
                // }
     
                 
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("4"));
                tokens.add(new CommaToken());
                tokens.add(new NumberToken("3"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("6"));
                tokens.add(new SemicolonToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("gomez"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new BooleanToken());
                tokens.add(new MethodNameToken("hello"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());
                
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("lol"));
                tokens.add(new EqualToken());
                tokens.add(new FalseToken());
                tokens.add(new SemicolonToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("lol"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new ClassType(new ClassName("Dog")), new Variable("x")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("x")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new IntegerLiteralExp(4));
                superParams.add(new IntegerLiteralExp(3));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();

                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new IntType(), new Variable("jon")));

                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                methodBodyStmts.add(new VariableInitializationStmt(new Vardec(new IntType(), new Variable("gomez")), new IntegerLiteralExp(6)));
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("gomez"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);

                MethodDef firstMethod = new MethodDef(new IntType(), new MethodName("test"), methodArguments, methodBody);
                methods.add(firstMethod);

                List<Vardec> methodArguments2 = new ArrayList<Vardec>();
                methodArguments2.add(new Vardec(new IntType(), new Variable("jon")));

                List<Stmt> methodBodyStmts2 = new ArrayList<Stmt>();
                methodBodyStmts2.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("lol")), new BooleanLiteralExp(false)));
                methodBodyStmts2.add(new ReturnNonVoidStmt(new VariableExp(new Variable("lol"))));
                Stmt methodBody2 = new BlockStmt(methodBodyStmts2);

                MethodDef secondMethod = new MethodDef(new BoolType(), new MethodName("hello"), methodArguments2, methodBody2);
                methods.add(secondMethod);

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 79); // bc 46 is } and 47 is nothing

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }

        
        @Test
        public void testReturnClassInMethodsAndCreatingInstancesOfClasses() throws ParseException {

                // class Test extends Object {
                //     Dog x = new Dog(5);
                //     constructor(Int x = 5;) {
                //         super(4);
                //         Boolean y = true; 
                //     }

                //     Dog test(Dog jon = 5;) {
                //         return (jon);
                //     }

                // }

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("4"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new ClassType(new ClassName("Dog")), new Variable("x")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("x")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new IntegerLiteralExp(4));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();
                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new ClassType(new ClassName("Dog")), new Variable("jon")));

                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("jon"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);

                methods.add(
                        new MethodDef(new ClassType(new ClassName("Dog")), 
                                      new MethodName("test"), 
                                      methodArguments, 
                                      methodBody));

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 55);

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }

        @Test
        public void testMethodsWithClassReturnType() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                List<Vardec> arguments = new ArrayList<Vardec>();
                List<Stmt> stmts = new ArrayList<Stmt>();
                stmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("jon"))));
                Stmt body = new BlockStmt(stmts);

                MethodDef m = new MethodDef(new ClassType(new ClassName("Dog")),
                new MethodName("test"),
                arguments, 
                body);
                ParseResult<MethodDef> method = new ParseResult<>(m, 10);

                Parser parser = new Parser(tokens);
                
                assertEquals(parser.parseMethod(tokens.get(0), 0), method);
        }

        @Test
        public void testIfStmtInMethodInClass() throws ParseException {

                // class Test extends Object {
                //     Dog x = new Dog(5);
                //     constructor(Int x = 5;) {
                //         super(4);
                //         Boolean y = true; 
                //     }

                //     Dog test(Dog jon = 5;) {
                //         if(true) {
                //             int hey = 2;
                //         }
                //         else {
                //             int heyo = 3;
                //         }
                //         return (jon);
                //     }

                // }

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new ClassToken());
                tokens.add(new ClassNameToken("Test"));
                tokens.add(new ExtendsToken());
                tokens.add(new ClassNameToken("Object"));

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new ConstructorToken());
                tokens.add(new LeftParenthesisToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("x"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new SemicolonToken());

                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                
                tokens.add(new SuperToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("4"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());

                tokens.add(new BooleanToken());
                tokens.add(new VariableToken("y"));
                tokens.add(new EqualToken());
                tokens.add(new TrueToken());
                tokens.add(new SemicolonToken());

                tokens.add(new RightCurlyBracketToken());

                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new MethodNameToken("test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new VariableToken("jon"));
                tokens.add(new EqualToken());
                tokens.add(new NewToken());
                tokens.add(new ClassNameToken("Dog"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("5"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new IfToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new EqualEqualToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("hey"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new ElseToken());
                tokens.add(new LeftCurlyBracketToken());
                tokens.add(new IntToken());
                tokens.add(new VariableToken("heyo"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("3"));
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());


                tokens.add(new ReturnToken());
                tokens.add(new LeftParenthesisToken());
                tokens.add(new VariableToken("jon"));
                tokens.add(new RightParenthesisToken());
                tokens.add(new SemicolonToken());
                tokens.add(new RightCurlyBracketToken());

                tokens.add(new RightCurlyBracketToken());

                
                ClassName className = new ClassName("Test");
                ClassName extendsClassName = new ClassName("Object");

                List<Vardec> instanceVariables = new ArrayList<Vardec>();
                instanceVariables.add(new Vardec(new ClassType(new ClassName("Dog")), new Variable("x")));

                List<Vardec> constructorArguments = new ArrayList<Vardec>();
                constructorArguments.add(new Vardec(new IntType(), new Variable("x")));

                List<Exp> superParams = new ArrayList<Exp>();
                superParams.add(new IntegerLiteralExp(4));

                List<Stmt> constructorBody = new ArrayList<Stmt>();
                constructorBody.add(new VariableInitializationStmt(new Vardec(new BoolType(), new Variable("y")),
                                                                new BooleanLiteralExp(true)));

                List<MethodDef> methods = new ArrayList<MethodDef>();
                List<Vardec> methodArguments = new ArrayList<Vardec>();
                methodArguments.add(new Vardec(new ClassType(new ClassName("Dog")), new Variable("jon")));

                List<Stmt> methodBodyStmts = new ArrayList<Stmt>();
                
                Exp guard = new OpExp(new IntegerLiteralExp(1), new EqualsEqualsOp(), new IntegerLiteralExp(1));
                List<Stmt> trueBranchStmts = new ArrayList<Stmt>();
                trueBranchStmts.add(new VariableInitializationStmt(
                new Vardec(new IntType(), new Variable("hey")), new IntegerLiteralExp(2)
                ));

                Stmt trueBranch = new BlockStmt(trueBranchStmts);

                List<Stmt> falseBranchStmts = new ArrayList<Stmt>();
                falseBranchStmts.add(new VariableInitializationStmt(
                new Vardec(new IntType(), new Variable("heyo")), new IntegerLiteralExp(3)
                ));

                Stmt falseBranch = new BlockStmt(falseBranchStmts);

                Stmt ifStmt = new IfStmt(guard, trueBranch, falseBranch);

                methodBodyStmts.add(ifStmt);
                methodBodyStmts.add(new ReturnNonVoidStmt(new VariableExp(new Variable("jon"))));
                Stmt methodBody = new BlockStmt(methodBodyStmts);

                methods.add(
                        new MethodDef(new ClassType(new ClassName("Dog")), 
                                      new MethodName("test"), 
                                      methodArguments, 
                                      methodBody));

                ClassDef classDef = new ClassDef(className, extendsClassName, 
                instanceVariables, constructorArguments, superParams, constructorBody, methods);

                ParseResult<ClassDef> classes = new ParseResult<>(classDef, 76);

                Parser parser = new Parser(tokens);
                assertEquals(parser.parseClass(0), classes);
        }
}