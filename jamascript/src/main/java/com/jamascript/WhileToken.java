package com.jamascript;

public class WhileToken {
    public boolean equals(final Object other){
        return other instanceof WhileToken;
    }
    public int hashCode(){
        return 10;
    }
    public String toString(){
        return "while";
    }
    
}
