package com.jamascript;
import com.jamascript.lexer.*;
import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

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
        final Parser parser = new Parser(Arrays.asList(new VariableToken("x")));
        assertEquals(new ParseResult<Exp>(new VariableExp(new Variable("x")),
                                          1),
                     parser.parsePrimaryExp(0));
    }

    @Test
    public void testPrimaryInteger() throws ParseException {
        final Parser parser = new Parser(Arrays.asList(new NumberToken("123")));
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
        final Parser parser = new Parser(Arrays.asList(new PlusToken()));
        assertEquals(new ParseResult<Op>(new PlusOp(), 1),
                     parser.parseAdditiveOp(0));
    }
                                                       
    @Test
    public void testAdditiveOpMinus() throws ParseException {
        final Parser parser = new Parser(Arrays.asList(new MinusToken()));
        assertEquals(new ParseResult<Op>(new MinusOp(), 1),
                     parser.parseAdditiveOp(0));
    }

    @Test
    public void testAdditiveExpOnlyPrimary() throws ParseException {
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1),
                     parser.parseAdditiveExp(0));
    }

    @Test
    public void testAdditiveExpSingleOperator() throws ParseException {
        // 1 + 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new PlusToken(),
                                                       new IntegerToken(2)));
        assertEquals(new ParseResult<Exp>(new OpExp(new IntegerExp(1),
                                                    new PlusOp(),
                                                    new IntegerExp(2)),
                                          3),
                     parser.parseAdditiveExp(0));
    }

    @Test
    public void testAdditiveExpMultiOperator() throws ParseException {
        // 1 + 2 - 3 ==> (1 + 2) - 3
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new PlusToken(),
                                                       new IntegerToken(2),
                                                       new MinusToken(),
                                                       new IntegerToken(3)));
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
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(123)));
        assertEquals(new ParseResult<Exp>(new IntegerExp(123), 1),
                     parser.parseLessThanExp(0));
    }

    @Test
    public void testLessThanSingleOperator() throws ParseException {
        // 1 < 2
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new LessThanToken(),
                                                       new IntegerToken(2)));
        final Exp expected = new OpExp(new IntegerExp(1),
                                       new LessThanOp(),
                                       new IntegerExp(2));
        assertEquals(new ParseResult<Exp>(expected, 3),
                     parser.parseLessThanExp(0));
    }

    @Test
    public void testLessThanMultiOperator() throws ParseException {
        // 1 < 2 < 3 ==> (1 < 2) < 3
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new LessThanToken(),
                                                       new IntegerToken(2),
                                                       new LessThanToken(),
                                                       new IntegerToken(3)));
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
        final Parser parser = new Parser(Arrays.asList(new IntegerToken(1),
                                                       new LessThanToken(),
                                                       new IntegerToken(2),
                                                       new PlusToken(),
                                                       new IntegerToken(3)));
        final Exp expected = new OpExp(new IntegerExp(1),
                                       new LessThanOp(),
                                       new OpExp(new IntegerExp(2),
                                                 new PlusOp(),
                                                 new IntegerExp(3)));
        assertEquals(new ParseResult<Exp>(expected, 5),
                     parser.parseLessThanExp(0));
    }

}
