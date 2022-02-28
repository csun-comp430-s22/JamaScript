import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenizerTest {
    //Checks for empty strings
    @Test
    public void testEmptyString() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(0, tokens.size());
        //assert(tokens.size() == 0);
    }

    //checks for white spaces
    @Test
    public void testOnlyWhiteSpace() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("    ");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(0, tokens.size());
        //assert(tokens.size() == 0);
    }
    
    @Test
    public void testTrueByItself() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("true");
        List<Token> tokens = tokenizer.tokenize();
        assertEquals(1, tokens.size());
        Token truetoken = tokens.get(0);
        assertTrue(truetoken instanceof TrueToken);
    }

    /*public static void main(String[] args) throws TokenizerException {
        testOnlyWhiteSpace();
        testEmptyString();
    }*/
}