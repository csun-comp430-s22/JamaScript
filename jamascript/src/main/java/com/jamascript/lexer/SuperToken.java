package com.jamascript.lexer;

public class SuperToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof SuperToken;
    }

    public int hashCode() {
        return 37;
    }

    public String toString() {
        return "super";
    }
}
