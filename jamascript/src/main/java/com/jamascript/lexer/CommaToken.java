package com.jamascript.lexer;

public class CommaToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof CommaToken;
    }

    public int hashCode() {
        return 3;
    }

    public String toString() {
        return ",";
    }
}
