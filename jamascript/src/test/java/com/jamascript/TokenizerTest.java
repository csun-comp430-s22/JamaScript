package com.jamascript;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TokenizerTest {

    public void assertTokenizes(final String input, final Token[] expected) {
        try {
            final Tokenizer tokenizer = new Tokenizer(input);
            final List<Token> received = tokenizer.tokenize();
            assertArrayEquals(expected, received.toArray(new Token[received.size()]));
        } catch(final TokenizerException e) {
            fail("Tokenizer threw exception");
        }
    }
    
    //Check for empty string
    @Test
    public void testEmptyString() {
        assertTokenizes("", new Token[0]);
    }

    @Test
    public void testOnlyWhitespace() {
        assertTokenizes("    ", new Token[0]);
    }

    /* Different cases for true/false
     * test true tokens
     * "true" -- pass                * " true" -- pass
     * "true " -- pass               * " true " -- pass
     * "truefalse" -- fail           * " truefalse" -- fail
     * "truefalse " -- fail          * " truefalse " -- fail
     * "true false" -- pass          * "(true)" -- pass
     * "(true" -- pass               * "true)" -- pass
     */
    
    @Test
    public void testTrueByItself() {
        assertTokenizes("true", new Token[] {new TrueToken()});
    }

    //space before
    @Test
    public void test_True() {
        assertTokenizes(" true", new Token[] {new TrueToken()});
    }

    //space after
    @Test
    public void testTrue_() {
        assertTokenizes("true ", new Token[] {new TrueToken()});
    }

    //space before and after
    @Test
    public void test_True_() {
        assertTokenizes(" true ", new Token[] {new TrueToken()});
    }

    //truetrue
    @Test
    public void testTrueTrueIsVariable() {
        assertTokenizes("truetrue", new Token[] {new VariableToken("truetrue")});
    }

    //true true
    @Test
    public void testTrue_TrueAreTrueTokens() {
        assertTokenizes("true true", new Token[] {new TrueToken(), new TrueToken()});
    }

    //test "true false"
    @Test
    public void testTrue_False() {
        assertTokenizes("true false", new Token[] {new TrueToken(), new FalseToken()});
    }
    
    //tests "(true)"
    @Test
    public void testTrueWithBothParantheses() {
        assertTokenizes("(true)", new Token[] {new LeftParenthesisToken(), new TrueToken(), new RightParenthesisToken()});
    }

    //tests "(true"
    @Test
    public void testTrueWithLeftParanthesis() {
        assertTokenizes("(true", new Token[] {new LeftParenthesisToken(), new TrueToken()});
    }

    //tests "true)"
    @Test
    public void testTrueWithRightParanthesis() {
        assertTokenizes("true)", new Token[] {new TrueToken(), new RightParenthesisToken()});
    }

    //test ALL brackets/parantheses, if, else, false
    @Test
    public void testAllRemainingBasics() {
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

    //Test all single symbol token
    @Test
    public void testAllRemainingSymbols() {
        assertTokenizes(", / . > < - * ! + \" ;", 
                        new Token[] {
                            new CommaToken(),
                            new DivideToken(),
                            new DotToken(),
                            new GreaterThanToken(),
                            new LessThanToken(),
                            new MinusToken(),
                            new MultiplyToken(),
                            new NotToken(),
                            new PlusToken(), 
                            new QuotationMarkToken(),
                            new SemicolonToken()
                        });
    }

    //test all the words
    @Test
    public void testAllRemainingWords() {
        assertTokenizes("Boolean class extends Int new return String while", 
                        new Token[] {
                            new BooleanToken(),
                            new ClassToken(),
                            new ExtendsToken(),
                            new IntToken(),
                            new NewToken(),
                            new ReturnToken(),
                            new StringToken(),
                            new WhileToken()
                        });
    }

    /* Tests Needed:
    Single Number, and any new Tokens added
    */
}