package com.jamascript.Tokens;

public class RightCurlyBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightCurlyBracketToken;
    }

    public int hashCode() {
        return 7;
    }

    public String toString() {
        return "}";
    }
}
