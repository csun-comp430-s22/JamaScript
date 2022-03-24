package com.jamascript.parser.expressions;

public class VariableExp implements Exp {
    public final String name;

    public VariableExp(final String name) {
        this.name = name;
    }

    public boolean equals(final Object other) {
        return (other instanceof VariableExp &&
                name.equals(((VariableExp) other).name));
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return "VariableExp(" + name + ")";
    }
}