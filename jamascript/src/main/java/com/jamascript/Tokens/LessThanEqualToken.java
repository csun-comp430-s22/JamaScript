package com.jamascript.Tokens;

public class LessThanEqualToken implements Token{
    public boolean equals(final Object other){
        return other instanceof LessThanEqualToken;
    }
    public int hashCode(){
        return 13;
    }
    public String toString(){
        return "<=";
    }
}