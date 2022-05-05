package com.jamascript.lexer;

public class RightSquaredBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof RightSquaredBracketToken;
    }

    public int hashCode() {
        return 29;
    }

    public String toString() {
        return "]";
    }

}
