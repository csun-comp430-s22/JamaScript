package com.jamascript.lexer;

public class PlusToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof PlusToken;
    }

    public int hashCode() {
        return 24;
    }

    public String toString() {
        return "+";
    }
}
