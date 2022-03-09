package com.jamascript.Tokens;

public class LessThanToken implements Token{
    public boolean equals(final Object other){
        return other instanceof LessThanToken;
    }
    public int hashCode(){
        return 12;
    }
    public String toString(){
        return "<";
    }
    
}
