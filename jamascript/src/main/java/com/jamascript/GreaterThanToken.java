package com.jamascript;
public class GreaterThanToken {
    public boolean equals(final Object other){
        return other instanceof GreaterThanToken;
    }
    public int hashCode(){
        return 11;
    }
    public String toString(){
        return ">";
    }
}
