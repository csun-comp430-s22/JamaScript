package com.jamascript.parser;

public class IntType implements Type{
    public IntType() {}

    public int hashCode() {
        return 2;
    }

    public boolean equals(final Object other) {
        return other instanceof IntType;
    }

    public String toString() {
        return "IntType";
    }
}
