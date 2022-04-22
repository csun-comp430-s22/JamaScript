package com.jamascript.typechecker.types;

public class VoidType implements Type {
    public int hashCode() { return 4; }
    public boolean equals(final Object other) {
        return other instanceof VoidType;
    }
    public String toString() { return "VoidType"; }
}