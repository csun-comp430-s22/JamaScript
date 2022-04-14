package com.jamascript.parser.expressions;

public class StringLiteralExp implements Exp{
    public final String value;

    public StringLiteralExp(final String value) {
        this.value = value;
    }

    public boolean equals(final Object other) {
        return (other instanceof StringLiteralExp &&
                value == ((StringLiteralExp) other).value);
    }

    public int hashCode() {
        return 2;
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }
}
