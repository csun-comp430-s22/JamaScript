package com.jamascript.lexer;

public class MultiplyToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof MultiplyToken;
    }

    public int hashCode() {
        return 21;
    }

    public String toString() {
        return "*";
    }
}
