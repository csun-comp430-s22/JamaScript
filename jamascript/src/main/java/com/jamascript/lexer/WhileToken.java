package com.jamascript.lexer;

public class WhileToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof WhileToken;
    }

    public int hashCode() {
        return 35;
    }

    public String toString() {
        return "while";
    }

}
