package com.jamascript.typechecker.types;

import com.jamascript.parser.classInformation.*;

public class ClassType implements Type{
    public final ClassName className;

    public ClassType(final ClassName className) {
        this.className = className;
    }

    public int hashCode() { return className.hashCode(); }

    public boolean equals(final Object other) {
        return (other instanceof ClassType &&
                className.equals(((ClassType)other).className));
    }

    public String toString() {
        return "ClassNameType(" + className.toString() + ")";
    }
}
