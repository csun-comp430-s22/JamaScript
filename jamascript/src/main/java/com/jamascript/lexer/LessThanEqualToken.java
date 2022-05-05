package com.jamascript.lexer;

public class LessThanEqualToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LessThanEqualToken;
    }

    public int hashCode() {
        return 18;
    }

    public String toString() {
        return "<=";
    }
}