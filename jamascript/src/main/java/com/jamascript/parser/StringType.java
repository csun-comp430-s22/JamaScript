package com.jamascript.parser;

public class StringType implements Type{
    public StringType() {}

    public int hashCode() {
        return 3;
    }

    public boolean equals(final Object other) {
        return other instanceof StringType;
    }

    public String toString() {
        return "StringType";
    }
}
