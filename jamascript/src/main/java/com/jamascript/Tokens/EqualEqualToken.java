package com.jamascript.Tokens;

public class EqualEqualToken implements Token{
    public boolean equals(final Object other){
        return other instanceof EqualEqualToken;
    }
    public int hashCode(){
        return 14;
    }
    public String toString(){
        return "==";
    }
}