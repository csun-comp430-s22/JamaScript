package com.jamascript.Tokens;

public class GreaterThanEqualToken implements Token{
    public boolean equals(final Object other){
        return other instanceof GreaterThanEqualToken;
    }
    public int hashCode(){
        return 12;
    }
    public String toString(){
        return ">=";
    }
}