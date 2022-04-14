package com.jamascript.parser;

import java.util.List;

public class Program {
    public final List<Mdef> methods;

    public Program(final List<Mdef> methods) {
        this.methods = methods;
    }

    public int hashCode() {
        return methods.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof Program &&
                methods.equals(((Program)other).methods));
    }

    public String toString() {
        return "Program(" + methods.toString() + ")";
    }
}


