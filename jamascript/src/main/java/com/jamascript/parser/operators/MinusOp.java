package com.jamascript.parser.operators;

public class MinusOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof MinusOp;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "MinusOp";
    }
}
