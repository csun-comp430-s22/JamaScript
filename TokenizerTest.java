import java.util.List;

public class TokenizerTest {
    //Checks for empty strings
    public static void testEmptyString() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("");
        List<Token> tokens = tokenizer.tokenize();
        assert(tokens.size() == 0);
    }

    //checks for white spaces
    public static void testOnlyWhiteSpace() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer("    ");
        List<Token> tokens = tokenizer.tokenize();
        assert(tokens.size() == 0);
    }
    
    public static void main(String[] args) throws TokenizerException {
        testOnlyWhiteSpace();
        testEmptyString();
    }
}