package com.jamascript.parser.expressions;

public class BooleanLiteralExp implements Exp {
    public final boolean value;

    public BooleanLiteralExp(final boolean value) {
        this.value = value;
    }

    public int hashCode() {
        return (value == false) ? 0 : 1;
    }

    public boolean equals(final Object other) {
        return (other instanceof BooleanLiteralExp &&
                value == ((BooleanLiteralExp)other).value);
    }

    public String toString() {
        return "BooleanLiteralExp(" + value + ")";
    }
}