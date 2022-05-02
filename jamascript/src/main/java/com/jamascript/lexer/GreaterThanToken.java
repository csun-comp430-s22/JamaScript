package com.jamascript.lexer;

public class GreaterThanToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanToken;
    }

    public int hashCode() {
        return 12;
    }

    public String toString() {
        return ">";
    }
}
