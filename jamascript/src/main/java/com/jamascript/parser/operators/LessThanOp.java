package com.jamascript.parser.operators;

public class LessThanOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof LessThanOp;
    }

    public int hashCode() {
        return 2;
    }

    public String toString() {
        return "LessThanOp";
    }
}
