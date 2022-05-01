package com.jamascript.lexer;

public class EqualEqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof EqualEqualToken;
    }

    public int hashCode() {
        return 7;
    }

    public String toString() {
        return "==";
    }
}