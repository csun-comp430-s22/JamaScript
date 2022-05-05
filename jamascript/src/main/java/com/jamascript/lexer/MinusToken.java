package com.jamascript.lexer;

public class MinusToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof MinusToken;
    }

    public int hashCode() {
        return 19;
    }

    public String toString() {
        return "-";
    }
}
