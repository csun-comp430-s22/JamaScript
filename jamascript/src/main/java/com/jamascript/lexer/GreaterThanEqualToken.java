package com.jamascript.lexer;

public class GreaterThanEqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanEqualToken;
    }

    public int hashCode() {
        return 10;
    }

    public String toString() {
        return ">=";
    }
}