package com.jamascript;
import java.util.List;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenizerTest {
    //Checks for empty strings
    @Test
    public void testEmptyString() throws TokenizerException{
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assertTrue(tokens.size() == 0);
    }

    @Test
    //checks for white spaces
    public void testOnlyWhiteSpace() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("    ");
        List<Token> tokens = tokenizer.tokenize();
        assertTrue(tokens.size() == 0);
    }

}