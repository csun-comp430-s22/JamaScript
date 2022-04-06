package com.jamascript.parser.operators;

public class MultiplyOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof MultiplyOp;
    }

    public int hashCode() {
        return 9;
    }

    public String toString() {
        return "MultiplyOp";
    }
}