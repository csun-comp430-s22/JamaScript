package com.jamascript.lexer;

public class ClassNameToken implements Token {
    
    public final String className;

    public ClassNameToken(final String className) {
        this.className = className;
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassNameToken) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return className.hashCode();
    }

    public String toString() {
        return "Classname(" + className + ")";
    }
    
}
