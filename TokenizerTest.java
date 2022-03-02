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

    //Checks for empty strings
    @Test
    public void testEmptyString() {
        assertTokenizes("", new Token[0]);
    }

    //checks for white spaces
    @Test
    public void testOnlyWhiteSpace() {
        assertTokenizes("    ", new Token[0]);
    }
    
    @Test
    public void testTrueByItself() {
        assertTokenizes("true", new Token[] {new TrueToken()});
    }

    //true true
    @Test
    public void testTrueSpaceTrueAreTrueTokens() {
        assertTokenizes("true true", new Token[] {new TrueToken(), new TrueToken()});
    }
}