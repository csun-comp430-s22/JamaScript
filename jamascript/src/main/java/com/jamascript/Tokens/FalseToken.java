package com.jamascript.Tokens;

public class FalseToken implements Token{
    public boolean equals(final Object other){
        return other instanceof FalseToken;
    }
    public int hashCode(){
        return 1;
    }
    public String toString(){
        return "false";
    }
}