package com.jamascript;

public class ElseToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof ElseToken;
    }

    public int hashCode() {
        return 5;
    }

    public String toString() {
        return "else";
    }
}
