package com.jamascript.lexer;

public class LessThanToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LessThanToken;
    }

    public int hashCode() {
        return 18;
    }

    public String toString() {
        return "<";
    }

}
