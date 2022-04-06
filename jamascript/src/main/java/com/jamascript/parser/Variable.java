package com.jamascript.parser;

public class Variable {
    public final String name;

    public Variable(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }
    
    public boolean equals(final Object other) {
        return (other instanceof Variable &&
                name.equals(((Variable)other).name));
    }

    public String toString() {
        return "Variable(" + name + ")";
    }
}