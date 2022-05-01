package com.jamascript.lexer;

public class LeftParenthesisToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LeftParenthesisToken;
    }

    public int hashCode() {
        return 16;
    }

    public String toString() {
        return "(";
    }
}
