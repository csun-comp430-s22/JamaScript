package com.jamascript.parser.operators;

public class EqualsOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof EqualsOp;
    }

    public int hashCode() {
        return 3;
    }

    public String toString() {
        return "EqualsOp";
    }
}
