package com.jamascript;

public class DivideToken {
    public boolean equals(final Object other){
        return other instanceof DivideToken;
    }
    public int hashCode(){
        return 16;
    }
    public String toString(){
        return "/";
    }
}
