package com.jamascript.parser.classInformation;

public class ClassName {
    public final String name;

    public ClassName(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof ClassName &&
                name.equals(((ClassName)other).name));
    }
    
    public String toString() {
        return "ClassName(" + name + ")";
    }
}
