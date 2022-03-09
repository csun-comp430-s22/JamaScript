package com.jamascript.Tokens;

public class ReturnToken implements Token {
    
    public boolean equals(final Object other){
        return other instanceof ReturnToken;
    }
    public int hashCode(){
        return 9;
    }
    public String toString(){
        return "return";
    }
}
