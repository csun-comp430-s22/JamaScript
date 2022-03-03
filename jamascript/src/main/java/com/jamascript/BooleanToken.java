package com.jamascript;

public class BooleanToken {
    public boolean equals(final Object other){
        return other instanceof BooleanToken;
    }
    public int hashCode(){
        return 18;
    }
    public String toString(){
        return "Boolean";
    }
    
}
