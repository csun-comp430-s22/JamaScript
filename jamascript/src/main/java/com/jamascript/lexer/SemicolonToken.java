package com.jamascript.lexer;

public class SemicolonToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof SemicolonToken;
    }

    public int hashCode() {
        return 31;
    }

    public String toString() {
        return ";";
    }
}
