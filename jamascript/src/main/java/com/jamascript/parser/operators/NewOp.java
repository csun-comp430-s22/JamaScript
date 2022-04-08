package com.jamascript.parser.operators;

public class NewOp implements Op{
    public NewOp() {}

    public int hashCode() {
        return 12;
    }

    public boolean equals(final Object other) {
        return other instanceof NewOp;
    }

    public String toString() {
        return "NewOp";
    }
}
