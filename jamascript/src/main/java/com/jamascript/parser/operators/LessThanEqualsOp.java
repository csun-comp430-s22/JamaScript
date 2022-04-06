package com.jamascript.parser.operators;

public class LessThanEqualsOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof LessThanEqualsOp;
    }

    public int hashCode() {
        return 6;
    }

    public String toString() {
        return "LessThanEqualsOp";
    }
}