package com.jamascript.Tokens;

public class EqualToken implements Token{
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
