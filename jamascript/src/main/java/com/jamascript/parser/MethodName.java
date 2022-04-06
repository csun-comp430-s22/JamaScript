package com.jamascript.parser;

public class MethodName {
    public final String name;

    public MethodName(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof MethodName &&
                name.equals(((MethodName)other).name));
    }
    
    public String toString() {
        return "MethodName(" + name + ")";
    }
}