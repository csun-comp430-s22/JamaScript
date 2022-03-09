package com.jamascript.Tokens;

public class LeftSquaredBracketToken implements Token{
    public boolean equals(final Object other){
        return other instanceof LeftSquaredBracketToken;
    }
    public int hashCode(){
        return 28;
    }
    public String toString(){
        return "[";
    }
    
}
