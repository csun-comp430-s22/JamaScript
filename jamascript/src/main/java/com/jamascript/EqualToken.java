package com.jamascript;

public class EqualToken {
    public boolean equals(final Object other){
        return other instanceof EqualToken;
    }
    public int hashCode(){
        return 17;
    }
    public String toString(){
        return "=";
    }
}
