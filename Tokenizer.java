import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    private final String input;
    private int offset;

    public Tokenizer(final String input) {
        this.input = input;
        offset = 0;
    }

    public void skipWhiteSpace() {
        while (offset < input.length() && Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }

    // If no more tokens left, returns NULL
    public Token tokenizeSingle() throws TokenizerException {
        skipWhiteSpace();
        if(offset < input.length()) {
            if(input.startsWith("true", offset)) {
                offset += 4;
                return new TrueToken();
            } else if(input.startsWith("false", offset)) {
                offset += 5;
                return new FalseToken();
            } else if(input.startsWith("(", offset)) {
                offset += 1;
                return new LeftParenthesisToken();
            } else if (input.startsWith(")", offset)) {
                offset += 1;
                return new RightParenthesisToken();
            } else if(input.startsWith("if", offset)) {
                offset += 2;
                return new IfToken();
            } else if(input.startsWith("else", offset)) {
                offset += 4;
                return new ElseToken();
            }  else if(input.startsWith("{", offset)) {
                offset += 1;
                return new LeftCurlyBracketToken();
            } else if(input.startsWith("}", offset)) {
                offset += 1;
                return new RightCurlyBracketToken();
            } else {
                throw new TokenizerException();
            }
        } else {
            return null;
        }
    }

    public List<Token> tokenize() throws TokenizerException {
        final List<Token> tokens = new ArrayList<Token>();
        Token token = tokenizeSingle();

        while(token != null) {
            tokens.add(token);
            token = tokenizeSingle();
        }
        return tokens;
    }
}