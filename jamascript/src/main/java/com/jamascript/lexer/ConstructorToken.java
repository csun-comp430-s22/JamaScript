package com.jamascript.lexer;

public class ConstructorToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ConstructorToken;
    }

    public int hashCode() {
        return 36;
    }

    public String toString() {
        return "constructor";
    }
}
