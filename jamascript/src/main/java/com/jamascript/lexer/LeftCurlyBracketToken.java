package com.jamascript.lexer;

public class LeftCurlyBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LeftCurlyBracketToken;
    }

    public int hashCode() {
        return 15;
    }

    public String toString() {
        return "{";
    }
}
