package com.jamascript.lexer;

public class MethodNameToken implements Token{
    public final String name;

    public MethodNameToken(final String name){
        this.name = name;
    }

    public boolean equals(final Object other) {
        if(other instanceof MethodNameToken){
            final MethodNameToken asString = (MethodNameToken)other;
            return name.equals(asString.name);
        }else{
            return false;
        }
    }

    public int hashCode() {
        return 20 + name.hashCode();
    }

    public String toString() {
        return "MethodNameToken("+name+")";
    }
}
