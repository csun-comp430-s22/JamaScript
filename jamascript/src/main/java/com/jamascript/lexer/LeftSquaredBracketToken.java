package com.jamascript.lexer;

public class LeftSquaredBracketToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LeftSquaredBracketToken;
    }

    public int hashCode() {
        return 16;
    }

    public String toString() {
        return "[";
    }

}
