package com.jamascript;

public class StringToken {
    public boolean equals(final Object other){
        return other instanceof StringToken;
    }
    public int hashCode(){
        return 19;
    }
    public String toString(){
        return "String";
    }
}
