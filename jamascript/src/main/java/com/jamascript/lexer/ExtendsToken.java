package com.jamascript.lexer;

public class ExtendsToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ExtendsToken;
    }

    public int hashCode() {
        return 9;
    }

    public String toString() {
        return "extends";
    }
}
