package com.jamascript.lexer;

public class RightSquaredBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightSquaredBracketToken;
    }

    public int hashCode() {
        return 30;
    }

    public String toString() {
        return "]";
    }

}
