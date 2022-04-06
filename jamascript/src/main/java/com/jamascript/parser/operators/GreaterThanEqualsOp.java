package com.jamascript.parser.operators;

public class GreaterThanEqualsOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof GreaterThanEqualsOp;
    }

    public int hashCode() {
        return 5;
    }

    public String toString() {
        return "GreaterThanEqualsOp";
    }
}