package com.jamascript;

import com.jamascript.lexer.*;
import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.parser.classInformation.*;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ParserTest {

        @Test
        public void testEqualsOpExp() {
                // 1 + 1 == 1 + 1
                final OpExp first = new OpExp(new IntegerExp(1),
                                new PlusOp(),
                                new IntegerExp(1));
                final OpExp second = new OpExp(new IntegerExp(1),
                                new PlusOp(),
                                new IntegerExp(1));
                assertEquals(first, second);
        }

        @Test
        public void testPrimaryVariable() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new VariableToken("x"));

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")),
                                1),
                                parser.parsePrimaryExp(0));
        }

        @Test
        public void testPrimaryInteger() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("123"));

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1),
                                parser.parsePrimaryExp(0));
        }

        @Test
        public void testPrimaryParens() throws ParseException {
                final Parser parser = new Parser(Arrays.asList(new LeftParenthesisToken(),
                                new NumberToken("123"),
                                new RightParenthesisToken()));
                assertEquals(new ParseResult<Exp>(new IntegerExp(123), 3),
                                parser.parsePrimaryExp(0));
        }

        @Test
        public void testAdditiveOpPlus() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new PlusToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new PlusOp(), 1),
                                parser.parseAdditiveOp(0));
        }

        @Test
        public void testAdditiveOpMinus() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new MinusToken());

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Op>(new MinusOp(), 1),
                                parser.parseAdditiveOp(0));
        }

        @Test
        public void testAdditiveExpOnlyPrimary() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("123"));

                final Parser parser = new Parser(tokens);
                assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1),
                                parser.parseAdditiveExp(0));
        }

        @Test
        public void testAdditiveExpSingleOperator() throws ParseException {
                // 1 + 2

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new PlusToken());
                tokens.add(new NumberToken("2"));

                final Parser parser = new Parser(tokens);

                assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1),
                                new PlusOp(),
                                new IntegerExp(2)),
                                3),
                                parser.parseAdditiveExp(0));
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
                final Exp expected = new OpExp(new OpExp(new IntegerExp(1),
                                new PlusOp(),
                                new IntegerExp(2)),
                                new MinusOp(),
                                new IntegerExp(3));
                assertEquals(new ParseResult<Exp>(expected, 5),
                                parser.parseAdditiveExp(0));
        }

        @Test
        public void testLessThanExpOnlyAdditive() throws ParseException {

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("123"));

                final Parser parser = new Parser(tokens);

                assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1),
                                parser.parseLessThanExp(0));
        }

        @Test
        public void testLessThanSingleOperator() throws ParseException {
                // 1 < 2

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(new IntegerExp(1),
                                new LessThanOp(),
                                new IntegerExp(2));
                assertEquals(new ParseResult<Exp>(expected, 3),
                                parser.parseLessThanExp(0));
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
                final Exp expected = new OpExp(new OpExp(new IntegerExp(1),
                                new LessThanOp(),
                                new IntegerExp(2)),
                                new LessThanOp(),
                                new IntegerExp(3));
                assertEquals(new ParseResult<Exp>(expected, 5),
                                parser.parseLessThanExp(0));
        }

        @Test
        public void testLessThanMixedOperator() throws ParseException {
                // 1 < 2 + 3 ==> 1 < (2 + 3)

                List<Token> tokens = new ArrayList<Token>();
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new PlusToken());
                tokens.add(new NumberToken("3"));

                final Parser parser = new Parser(tokens);
                final Exp expected = new OpExp(new IntegerExp(1),
                                new LessThanOp(),
                                new OpExp(new IntegerExp(2),
                                                new PlusOp(),
                                                new IntegerExp(3)));
                assertEquals(new ParseResult<Exp>(expected, 5),
                                parser.parseLessThanExp(0));
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
                trueStmts.add(new PrintlnStmt(new StringExp("yup")));

                List<Stmt> falseStmts = new ArrayList<Stmt>();
                falseStmts.add(new PrintlnStmt(new StringExp("nah")));

                final IfStmt expected = new IfStmt(
                                new OpExp(new IntegerExp(2),
                                                new GreaterThanOp(), new IntegerExp(1)),
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
                trueStmts.add(new PrintlnStmt(new StringExp("yup")));

                List<Stmt> falseStmts = new ArrayList<Stmt>();
                falseStmts.add(new PrintlnStmt(new StringExp("nah")));

                final IfStmt expected = new IfStmt(
                                new OpExp(new IntegerExp(1),
                                                new EqualsEqualsOp(), new IntegerExp(1)),
                                new BlockStmt(trueStmts),
                                new BlockStmt(falseStmts));

                assertEquals(new ParseResult<Stmt>(expected, 20),
                                parser.parseStmt(0));

        }

        @Test
        public void testClassExpression() throws ParseException {
                // 1 < 2 + 3 ==> 1 < (2 + 3)

                List<Token> tokens = new ArrayList<Token>();
                List<ParseResult<Exp>> parameters = new ArrayList<ParseResult<Exp>>();

                // 'new Test(3, 4)'
                tokens.add(new NewToken());
                tokens.add(new VariableToken("Test"));
                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("3"));
                tokens.add(new CommaToken());
                tokens.add(new NumberToken("4"));
                tokens.add(new RightParenthesisToken());

                parameters.add(new ParseResult<Exp>(new IntegerExp(3), 4));
                parameters.add(new ParseResult<Exp>(new IntegerExp(4), 6));

                final Parser parser = new Parser(tokens);

                final Exp expected = new ClassExpression(new NewOp(),
                                new ClassName("Test"),
                                parameters);

                assertEquals(new ParseResult<Exp>(expected, 7), parser.parseExp(0));

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

                final Exp expression = new OpExp(new IntegerExp(1),
                                new LessThanOp(),
                                new IntegerExp(2));

                final List<Stmt> trueBranchResultStatements = new ArrayList<Stmt>();
                trueBranchResultStatements.add(new PrintlnStmt(new IntegerExp(1)));

                Stmt trueBranchResult = new BlockStmt(trueBranchResultStatements);

                final List<Stmt> falseBranchResultStatements = new ArrayList<Stmt>();
                falseBranchResultStatements.add(new PrintlnStmt(new IntegerExp(2)));

                Stmt falseBranchResult = new BlockStmt(falseBranchResultStatements);

                final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                                new IfStmt(expression, trueBranchResult, falseBranchResult), 20);

                assertEquals(expected, parser.parseStmt(0));
        }

        @Test
        public void testParseStmtVariableDeclaration() throws ParseException {
                List<Token> tokens = new ArrayList<Token>();

                // if(1 < 2){println(1);} else {println(2);}
                tokens.add(new IfToken());

                tokens.add(new LeftParenthesisToken());
                tokens.add(new NumberToken("1"));
                tokens.add(new LessThanToken());
                tokens.add(new NumberToken("2"));
                tokens.add(new RightParenthesisToken());

                tokens.add(new LeftCurlyBracketToken());

                tokens.add(new IntToken());
                tokens.add(new VariableToken("test"));
                tokens.add(new EqualToken());
                tokens.add(new NumberToken("3"));
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

                final Exp expression = new OpExp(new IntegerExp(1),
                                new LessThanOp(),
                                new IntegerExp(2));

                final List<Stmt> trueBranchResultStatements = new ArrayList<Stmt>();

                final Vardec variableDeclaration = new Vardec(
                                new IntType(),
                                new Variable("test"));

                final VardecStmt variableDeclarationStatement = new VardecStmt(variableDeclaration,
                                new ParseResult<Exp>(new IntegerExp(3), 11));

                trueBranchResultStatements.add(variableDeclarationStatement);

                Stmt trueBranchResult = new BlockStmt(trueBranchResultStatements);

                final List<Stmt> falseBranchResultStatements = new ArrayList<Stmt>();
                falseBranchResultStatements.add(new PrintlnStmt(new IntegerExp(2)));

                Stmt falseBranchResult = new BlockStmt(falseBranchResultStatements);

                final ParseResult<Stmt> expected = new ParseResult<Stmt>(
                                new IfStmt(expression, trueBranchResult, falseBranchResult), 20);

                assertEquals(expected, parser.parseStmt(0));
        }

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
                                parser.parseEqualsExp(0));
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

                List<Stmt> bodyStmt = new ArrayList<Stmt>();
                bodyStmt.add(new PrintlnStmt(new StringExp("ahhh")));

                final WhileStmt expected = new WhileStmt(
                                new BooleanLiteralExp(true),
                                new BlockStmt(bodyStmt));

                assertEquals(new ParseResult<Stmt>(expected, 10),
                                parser.parseStmt(0));
        }

}
