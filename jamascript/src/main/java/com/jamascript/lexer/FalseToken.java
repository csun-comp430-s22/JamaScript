package com.jamascript.lexer;

public class FalseToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof FalseToken;
    }

    public int hashCode() {
        return 9;
    }

    public String toString() {
        return "false";
    }
}