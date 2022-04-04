package com.jamascript.parser.expressions;

public class IntegerExp implements Exp {
    public final int value;

    public IntegerExp(final int value) {
        this.value = value;
    }

    public boolean equals(final Object other) {
        return (other instanceof IntegerExp &&
                value == ((IntegerExp) other).value);
    }

    public int hashCode() {
        return value;
    }

    public String toString() {
        return "IntegerExp(" + value + ")";
    }
}
