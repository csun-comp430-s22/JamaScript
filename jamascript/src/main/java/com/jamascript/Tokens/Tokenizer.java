package com.jamascript.Tokens;

import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
    private final String input;
    private int offset;

    public Tokenizer(final String input) {
        this.input = input;
        offset = 0;
    }

    // Checks whether the offset is less than the string
    // Checks also whether the current character that I'm at in the string is whitespace at the offset
    public void skipWhiteSpace() {
        while ((offset < input.length()) && Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }

    // returns null if it wasn't an integer token
    public NumberToken tryTokenizeNumber() {
        skipWhiteSpace();

        // 12345
        String number = "";

        while (offset < input.length() &&
               Character.isDigit(input.charAt(offset))) {
            number += input.charAt(offset);
            offset++;
        }

        if (number.length() > 0) {
            // convert string to an integer
            return new NumberToken(number);
        } else {
            return null;
        }
    }

    public Token tryTokenizeVariableOrKeyword() {
        skipWhiteSpace();
        String name = "";

        // if the offset is less than length of string
        // and if the current character in the string is a letter
        if((offset < input.length()) && Character.isLetter(input.charAt(offset))) {
            name += input.charAt(offset);
            offset++;

            
            while((offset < input.length()) && Character.isLetterOrDigit(input.charAt(offset))) {
                name += input.charAt(offset);
                offset++;
            }

            if(name.equals("true")) {
                return new TrueToken();
            } else if(name.equals("false")) {
                return new FalseToken();
            } else if(name.equals("if")) {
                return new IfToken();
            } else if(name.equals("else")) {
                return new ElseToken();
            } else if(name.equals("Boolean")) {
                return new BooleanToken();
            } else if(name.equals("class")) {
                return new ClassToken();
            } else if(name.equals("extends")) {
                return new ExtendsToken();
            } else if(name.equals("Int")) {
                return new IntToken();
            } else if(name.equals("new")) {
                return new NewToken();
            } else if(name.equals("return")) {
                return new ReturnToken();
            } else if(name.equals("String")) {
                return new StringToken();
            } else if(name.equals("while")) {
                return new WhileToken();
            } else {
                return new VariableToken(name);
            }
        } else {
            return null;
        }
    }

    // returns null if it couldn't read in a symbol
    public Token tryTokenizeSymbol() {
        skipWhiteSpace();
        Token retval = null;
 
        if (input.startsWith("(", offset)) {
            offset += 1;
            retval = new LeftParenthesisToken();
        } else if (input.startsWith(")", offset)) {
            offset += 1;
            retval = new RightParenthesisToken();
        }  else if (input.startsWith("{", offset)) {
            offset += 1;
            retval = new LeftCurlyBracketToken();
        } else if (input.startsWith("}", offset)) {
            offset += 1;
            retval = new RightCurlyBracketToken();
        } else if(input.startsWith("[", offset)) {
            offset += 1;
            retval = new LeftSquaredBracketToken();
        } else if(input.startsWith("]", offset)) {
            offset += 1;
            retval = new RightSquaredBracketToken();
        } else if(input.startsWith("/", offset)) {
            offset += 1;
            retval = new DivideToken();
        } else if(input.startsWith(",", offset)) {
            offset += 1;
            retval = new CommaToken();
        } else if(input.startsWith(".", offset)) {
            offset += 1;
            retval = new DotToken();
        } else if(input.startsWith(">", offset)) {
            offset += 1;
            retval = new GreaterThanToken();
        } else if(input.startsWith("<", offset)) {
            offset += 1;
            retval = new LessThanToken();
        } else if(input.startsWith("-", offset)) {
            offset += 1;
            retval = new MinusToken();
        } else if(input.startsWith("*", offset)) {
            offset += 1;
            retval = new MultiplyToken();
        } else if(input.startsWith("!", offset)) {
            offset += 1;
            retval = new NotToken();
        } else if(input.startsWith("+", offset)) {
            offset += 1;
            retval = new PlusToken();
        } else if(input.startsWith("\"", offset)) {
            offset += 1;
            retval = new QuotationMarkToken();
        } else if(input.startsWith(";", offset)) {
            offset += 1;
            retval = new SemicolonToken();
        } else if(input.startsWith("=", offset)) {
            offset += 1;
            retval = new EqualToken();
        }
        return retval;
}

    // If no more tokens left, returns NULL
    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhiteSpace();

        if(offset < input.length() &&
            (retval = tryTokenizeVariableOrKeyword()) == null &&
            (retval = tryTokenizeNumber()) == null &&
            (retval = tryTokenizeSymbol()) == null) {
                throw new TokenizerException();
        }
        return retval;
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