package com.jamascript.lexer;

public class ClassNameToken implements Token {
    public final String name;

    public ClassNameToken(final String name){
        this.name = name;
    }

    public boolean equals(final Object other) {
        if(other instanceof ClassNameToken){
            final ClassNameToken asString = (ClassNameToken)other;
            return name.equals(asString.name);
        }else{
            return false;
        }
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "ClassNameToken("+name+")";
    }
}
