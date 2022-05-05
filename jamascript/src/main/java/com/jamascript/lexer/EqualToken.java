package com.jamascript.lexer;

public class EqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof EqualToken;
    }

    public int hashCode() {
        return 7;
    }

    public String toString() {
        return "=";
    }
}
