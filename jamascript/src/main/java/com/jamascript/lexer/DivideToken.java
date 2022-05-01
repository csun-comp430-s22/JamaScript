package com.jamascript.lexer;

public class DivideToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof DivideToken;
    }

    public int hashCode() {
        return 4;
    }

    public String toString() {
        return "/";
    }
}
