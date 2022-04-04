package com.jamascript.lexer;

public class RightCurlyBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightCurlyBracketToken;
    }

    public int hashCode() {
        return 26;
    }

    public String toString() {
        return "}";
    }
}
