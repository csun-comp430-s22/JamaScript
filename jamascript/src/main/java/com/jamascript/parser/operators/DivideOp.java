package com.jamascript.parser.operators;

public class DivideOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof DivideOp;
    }

    public int hashCode() {
        return 2;
    }

    public String toString() {
        return "DivideOp";
    }
}