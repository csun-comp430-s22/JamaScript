package com.jamascript;

public class ReturnToken {
    
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
