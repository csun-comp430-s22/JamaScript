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

    // Checks whether the offset is less than the string
    // Checks also whether the current character that I'm at in the string is whitespace at the offset
    public void skipWhiteSpace() {
        while ((offset < input.length()) && Character.isWhitespace(input.charAt(offset))) {
            offset++;
        }
    }


    public boolean isNumber(String number) {
        boolean isANumber = true;
        for(char c : number.toCharArray()) {
            if(!Character.isDigit(c)) {
                isANumber = false;
            }
        }
        return isANumber;
    }

    public Token tryTokenizeVariableOrKeyword() {
        skipWhiteSpace();
        String name = "";

        // if the offset is less than length of string
        // and if the current character in the string starts with a digit, it must be a digit
        // EX: 1212321 OK
        // EX: 1abc NOT OK
        // EX: 12312a NOT OK
        if(offset < input.length() && Character.isDigit(input.charAt(offset))) {

            // Parse through the string until something that isn't a Letter or a Letter is encountered
            while(Character.isDigit(input.charAt(offset)) && !Character.isWhitespace(input.charAt(offset))) {
                name += input.charAt(offset);
                offset++;
            }

            // might need to throw error if I encounter anything that's not a digit in this case

            return new NumberToken(name);
        }

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
            }  else {
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