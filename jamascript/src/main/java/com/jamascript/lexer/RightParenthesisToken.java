package com.jamascript.lexer;

public class RightParenthesisToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightParenthesisToken;
    }

    public int hashCode() {
        return 29;
    }

    public String toString() {
        return ")";
    }
}
