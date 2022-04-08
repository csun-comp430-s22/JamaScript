package com.jamascript;
import com.jamascript.lexer.*;
import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.parser.classInformation.*;

import static org.junit.Assert.assertTrue;
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
        tokens.add(new VariableToken("123"));

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

    // @Test
    // public void testClassExpression() throws ParseException {
    //     // 1 < 2 + 3 ==> 1 < (2 + 3)

    //     List<Token> tokens = new ArrayList<Token>();
    //     List<Exp> parameters = new ArrayList<Exp>();

    //     tokens.add(new NewToken());
    //     tokens.add(new ClassNameToken("Test"));
    //     tokens.add(new LeftParenthesisToken());
    //     tokens.add(new NumberToken("3"));
    //     tokens.add(new RightParenthesisToken());
        
    //     final Parser parser = new Parser(tokens);

    //     final Exp expected = new ClassExpression(new NewOp(), 
    //                                              new ClassName("Test"),
    //                                              parameters);
        
    //     assertEquals(new ParseResult<Exp>(expected, 5),
    //                  parser.parseClassExpression)

    //     // final Parser parser = new Parser(tokens);
    //     // final Exp expected = new ClassExpression(new IntegerExp(1),
    //     //                                new LessThanOp(),
    //     //                                new OpExp(new IntegerExp(2),
    //     //                                          new PlusOp(),
    //     //                                          new IntegerExp(3)));
    //     // assertEquals(new ParseResult<Exp>(expected, 5),
    //     //              parser.parseLessThanExp(0));
    // }

}
