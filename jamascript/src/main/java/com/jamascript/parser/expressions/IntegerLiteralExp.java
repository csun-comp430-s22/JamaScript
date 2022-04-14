package com.jamascript.parser.expressions;

public class IntegerLiteralExp implements Exp {
    public final int value;

    public IntegerLiteralExp(final int value) {
        this.value = value;
    }

    public boolean equals(final Object other) {
        return (other instanceof IntegerLiteralExp &&
                value == ((IntegerLiteralExp) other).value);
    }

    public int hashCode() {
        return value;
    }

    public String toString() {
        return "IntegerExp(" + value + ")";
    }
}
