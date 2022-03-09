package com.jamascript;

import java.util.List;

import com.jamascript.Tokens.BooleanToken;
import com.jamascript.Tokens.ClassToken;
import com.jamascript.Tokens.CommaToken;
import com.jamascript.Tokens.DivideToken;
import com.jamascript.Tokens.DotToken;
import com.jamascript.Tokens.ElseToken;
import com.jamascript.Tokens.EqualToken;
import com.jamascript.Tokens.ExtendsToken;
import com.jamascript.Tokens.FalseToken;
import com.jamascript.Tokens.GreaterThanToken;
import com.jamascript.Tokens.IfToken;
import com.jamascript.Tokens.IntToken;
import com.jamascript.Tokens.LeftCurlyBracketToken;
import com.jamascript.Tokens.LeftParenthesisToken;
import com.jamascript.Tokens.LeftSquaredBracketToken;
import com.jamascript.Tokens.LessThanToken;
import com.jamascript.Tokens.MinusToken;
import com.jamascript.Tokens.MultiplyToken;
import com.jamascript.Tokens.NewToken;
import com.jamascript.Tokens.NotToken;
import com.jamascript.Tokens.NumberToken;
import com.jamascript.Tokens.PlusToken;
import com.jamascript.Tokens.QuotationMarkToken;
import com.jamascript.Tokens.ReturnToken;
import com.jamascript.Tokens.RightCurlyBracketToken;
import com.jamascript.Tokens.RightParenthesisToken;
import com.jamascript.Tokens.RightSquaredBracketToken;
import com.jamascript.Tokens.SemicolonToken;
import com.jamascript.Tokens.StringToken;
import com.jamascript.Tokens.Token;
import com.jamascript.Tokens.Tokenizer;
import com.jamascript.Tokens.TokenizerException;
import com.jamascript.Tokens.TrueToken;
import com.jamascript.Tokens.VariableToken;
import com.jamascript.Tokens.WhileToken;

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

    //Test all single symbol token
    @Test
    public void testAllRemainingSymbols() {
        assertTokenizes(", / . > < - * ! + \" ; =", 
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
                            new SemicolonToken(),
                            new EqualToken()
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

    @Test
    public void testNumbers() {
        assertTokenizes("12312 123123", 
                new Token[] {
                    new NumberToken("12312"), 
                    new NumberToken("123123")
                });
    }

    @Test
    public void testNumbersAndLetters() {
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
    
    @Test
    public void testSingleNumber() {
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
    public void testSingleNumberAndMultiNumbers() {
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

    // WORKING ON IT
    // @Test
    // public void testNumberFollowedByLetter() throws TokenizerException {
    
    //     assertTokenizes("1abc", 
    //     new Token[] {
    //         new NumberToken("1abc"),
            
    //     });
       
        
    // }
}