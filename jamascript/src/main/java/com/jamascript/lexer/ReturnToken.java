package com.jamascript.lexer;

public class ReturnToken implements Token {

    public boolean equals(final Object other) {
        return other instanceof ReturnToken;
    }

    public int hashCode() {
        return 27;
    }

    public String toString() {
        return "return";
    }
}
