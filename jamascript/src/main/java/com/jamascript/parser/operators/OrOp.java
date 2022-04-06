package com.jamascript.parser.operators;

public class OrOp implements Op {
    public OrOp() {}

    public int hashCode() {
        return 10;
    }

    public boolean equals(final Object other) {
        return other instanceof OrOp;
    }

    public String toString() {
        return "OrOp";
    }
}