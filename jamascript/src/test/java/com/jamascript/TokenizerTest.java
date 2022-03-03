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

    @Test
    public void testTrueByItself() {
        assertTokenizes("true", new Token[] {new TrueToken()});
    }

    //foo
    @Test
    public void testVariable() {
        assertTokenizes("foo", new Token[] {new VariableToken("foo")});
    }

    //truetrue
    @Test
    public void testTrueTrueIsVariable() {
        assertTokenizes("truetrue", new Token[] {new VariableToken("truetrue")});
    }

    //true true
    @Test
    public void testTrueSpaceTrueAreTrueTokens() {
        assertTokenizes("true true", new Token[] {new TrueToken(), new TrueToken()});
    }

    @Test
    public void testAllRemaining() {
        assertTokenizes("(){}else if false", 
                        new Token[] {
                            new LeftParenthesisToken(),
                            new RightParenthesisToken(),
                            new LeftCurlyBracketToken(),
                            new RightCurlyBracketToken(),
                            new ElseToken(),
                            new IfToken(),
                            new FalseToken()
                        });
    }

}