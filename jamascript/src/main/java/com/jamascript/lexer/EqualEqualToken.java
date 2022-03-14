package com.jamascript.lexer;

public class EqualEqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof EqualEqualToken;
    }

    public int hashCode() {
        return 6;
    }

    public String toString() {
        return "==";
    }
}