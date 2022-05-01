package com.jamascript.lexer;

public class DotToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof DotToken;
    }

    public int hashCode() {
        return 5;
    }

    public String toString() {
        return ".";
    }
}
