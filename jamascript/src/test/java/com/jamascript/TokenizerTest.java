package com.jamascript;

import java.util.List;
import com.jamascript.lexer.*;

import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.fail;
import org.junit.Test;

public class TokenizerTest {

    public void assertTokenizes(final String input, final Token[] expected) throws TokenizerException {
        final Tokenizer tokenizer = new Tokenizer(input);
        final List<Token> received = tokenizer.tokenize();
        assertArrayEquals(expected, received.toArray(new Token[received.size()]));

        // Can use this instead of we remove the testInvalid function later on
        /*
         * try {
         * final Tokenizer tokenizer = new Tokenizer(input);
         * final List<Token> received = tokenizer.tokenize();
         * assertArrayEquals(expected, received.toArray(new Token[received.size()]));
         * } catch(final TokenizerException e) {
         * fail("Tokenizer threw exception");
         * }
         */
    }

    // Check for empty string
    @Test
    public void testEmptyString() throws TokenizerException {
        assertTokenizes("", new Token[0]);
    }

    @Test
    public void testOnlyWhitespace() throws TokenizerException {
        assertTokenizes("    ", new Token[0]);
    }

    /*
     * Different cases for true/false
     * test true tokens
     * "true" -- pass * " true" -- pass
     * "true " -- pass * " true " -- pass
     * "truefalse" -- fail * " truefalse" -- fail
     * "truefalse " -- fail * " truefalse " -- fail
     * "true false" -- pass * "(true)" -- pass
     * "(true" -- pass * "true)" -- pass
     */

    @Test
    public void testTrueByItself() throws TokenizerException {
        assertTokenizes("true", new Token[] { new TrueToken() });
    }

    // space before
    @Test
    public void test_True() throws TokenizerException {
        assertTokenizes(" true", new Token[] { new TrueToken() });
    }

    // space after
    @Test
    public void testTrue_() throws TokenizerException {
        assertTokenizes("true ", new Token[] { new TrueToken() });
    }

    // space before and after
    @Test
    public void test_True_() throws TokenizerException {
        assertTokenizes(" true ", new Token[] { new TrueToken() });
    }

    // truetrue
    @Test
    public void testTrueTrueIsVariable() throws TokenizerException {
        assertTokenizes("truetrue", new Token[] { new VariableToken("truetrue") });
    }

    // true true
    @Test
    public void testTrue_TrueAreTrueTokens() throws TokenizerException {
        assertTokenizes("true true", new Token[] { new TrueToken(), new TrueToken() });
    }

    // test "true false"
    @Test
    public void testTrue_False() throws TokenizerException {
        assertTokenizes("true false", new Token[] { new TrueToken(), new FalseToken() });
    }

    // tests "(true)"
    @Test
    public void testTrueWithBothParantheses() throws TokenizerException {
        assertTokenizes("(true)",
                new Token[] { new LeftParenthesisToken(), new TrueToken(), new RightParenthesisToken() });
    }

    // tests "(true"
    @Test
    public void testTrueWithLeftParanthesis() throws TokenizerException {
        assertTokenizes("(true", new Token[] { new LeftParenthesisToken(), new TrueToken() });
    }

    // tests "true)"
    @Test
    public void testTrueWithRightParanthesis() throws TokenizerException {
        assertTokenizes("true)", new Token[] { new TrueToken(), new RightParenthesisToken() });
    }

    // test ALL brackets/parantheses, if, else, false
    @Test
    public void testAllRemainingBasics() throws TokenizerException {
        assertTokenizes("(){}[] else if false",
                new Token[] {
                        new LeftParenthesisToken(),
                        new RightParenthesisToken(),
                        new LeftCurlyBracketToken(),
                        new RightCurlyBracketToken(),
                        new LeftSquaredBracketToken(),
                        new RightSquaredBracketToken(),
                        new ElseToken(),
                        new IfToken(),
                        new FalseToken()
                });
    }

    // Test all single symbol token
    @Test
    public void testAllRemainingSymbols() throws TokenizerException {
        assertTokenizes(", / . == = >= > <= < - * ! + \" ;",
                new Token[] {
                        new CommaToken(),
                        new DivideToken(),
                        new DotToken(),
                        new EqualEqualToken(),
                        new EqualToken(),
                        new GreaterThanEqualToken(),
                        new GreaterThanToken(),
                        new LessThanEqualToken(),
                        new LessThanToken(),
                        new MinusToken(),
                        new MultiplyToken(),
                        new NotToken(),
                        new PlusToken(),
                        new QuotationMarkToken(),
                        new SemicolonToken()
                });
    }

    // test all the words
    @Test
    public void testAllRemainingWords() throws TokenizerException {
        assertTokenizes("Boolean class extends Int new println return String while",
                new Token[] {
                        new BooleanToken(),
                        new ClassToken(),
                        new ExtendsToken(),
                        new IntToken(),
                        new NewToken(),
                        new PrintlnToken(),
                        new ReturnToken(),
                        new StringToken(),
                        new WhileToken()
                });
    }

    @Test
    public void testNumbers() throws TokenizerException {
        assertTokenizes("12312 123123",
                new Token[] {
                        new NumberToken("12312"),
                        new NumberToken("123123")
                });
    }

    @Test
    public void testNumbersAndLetters() throws TokenizerException {
        assertTokenizes(" true 12312 123123 222  { } [ ] Boolean",
                new Token[] {
                        new TrueToken(),
                        new NumberToken("12312"),
                        new NumberToken("123123"),
                        new NumberToken("222"),
                        new LeftCurlyBracketToken(),
                        new RightCurlyBracketToken(),
                        new LeftSquaredBracketToken(),
                        new RightSquaredBracketToken(),
                        new BooleanToken()
                });
    }

    // test all the numbers
    @Test
    public void testSingleNumber() throws TokenizerException {
        assertTokenizes("0 1 2 3 4 5 6 7 8 9",
                new Token[] {
                        new NumberToken("0"),
                        new NumberToken("1"),
                        new NumberToken("2"),
                        new NumberToken("3"),
                        new NumberToken("4"),
                        new NumberToken("5"),
                        new NumberToken("6"),
                        new NumberToken("7"),
                        new NumberToken("8"),
                        new NumberToken("9"),
                });
    }

    @Test
    public void testSingleNumberAndMultiNumbers() throws TokenizerException {
        assertTokenizes("0123423 1 2 234233 4 5 6 7234 8 9",
                new Token[] {
                        new NumberToken("0123423"),
                        new NumberToken("1"),
                        new NumberToken("2"),
                        new NumberToken("234233"),
                        new NumberToken("4"),
                        new NumberToken("5"),
                        new NumberToken("6"),
                        new NumberToken("7234"),
                        new NumberToken("8"),
                        new NumberToken("9"),
                });
    }

    @Test(expected = TokenizerException.class)
    public void testInvalid() throws TokenizerException {
        assertTokenizes("$", null);
    }

    

    // WORKING ON IT
    // @Test
    // public void testNumberFollowedByLetter() throws TokenizerException {

    // assertTokenizes("1abc",
    // new Token[] {
    // new NumberToken("1abc"),

    // });

    // }
}