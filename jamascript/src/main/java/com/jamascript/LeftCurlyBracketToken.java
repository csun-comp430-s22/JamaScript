package com.jamascript;

public class LeftCurlyBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LeftCurlyBracketToken;
    }

    public int hashCode() {
        return 6;
    }

    public String toString() {
        return "{";
    }
}
