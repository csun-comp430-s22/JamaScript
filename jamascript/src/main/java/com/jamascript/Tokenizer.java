package com.jamascript;

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
        while ((offset < input.length()) && Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }

    public Token tryTokenizeVariableOrKeyword() {
        skipWhiteSpace();
        String name = "";

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
            } else {
                return new VariableToken(name);
            }
        } else {
            return null;
        }
    }
    
    // If no more tokens left, returns NULL
    public Token tokenizeSingle() throws TokenizerException {
        Token retval = null;
        skipWhiteSpace();

        if(offset < input.length()) {
            retval = tryTokenizeVariableOrKeyword();
            if(retval == null) {
                if(input.startsWith("(", offset)) {
                    offset += 1;
                    retval = new LeftParenthesisToken();
                } else if(input.startsWith(")", offset)) {
                    offset += 1;
                    retval = new RightParenthesisToken();
                } else if(input.startsWith("{", offset)) {
                    offset += 1;
                    retval = new LeftCurlyBracketToken();
                } else if(input.startsWith("}", offset)) {
                    offset += 1;
                    retval = new RightCurlyBracketToken();
                } else {
                    throw new TokenizerException();
                }
            }
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