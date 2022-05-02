package com.jamascript.lexer;

public class QuotationMarkToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof QuotationMarkToken;
    }

    public int hashCode() {
        return 26;
    }

    public String toString() {
        return "\"";
    }
}
