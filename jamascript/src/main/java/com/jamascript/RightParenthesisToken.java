package com.jamascript;

public class RightParenthesisToken implements Token {
    public boolean equals(final Object other){
        return other instanceof RightParenthesisToken;
    }
    public int hashCode(){
        return 3;
    }
    public String toString(){
        return ")";
    }
}
