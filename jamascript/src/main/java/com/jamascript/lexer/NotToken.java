package com.jamascript.lexer;

public class NotToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof NotToken;
    }

    public int hashCode() {
        return 22;
    }

    public String toString() {
        return "!";
    }
}
