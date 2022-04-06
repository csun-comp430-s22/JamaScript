package com.jamascript.parser.operators;

public class EqualsEqualsOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof EqualsEqualsOp;
    }

    public int hashCode() {
        return 3;
    }

    public String toString() {
        return "EqualsEqualsOp";
    }
}