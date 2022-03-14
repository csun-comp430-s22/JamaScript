package com.jamascript.lexer;

public class StringToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof StringToken;
    }

    public int hashCode() {
        return 30;
    }

    public String toString() {
        return "String";
    }
}
