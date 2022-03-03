package com.jamascript;

public class NotToken implements Token {
    public boolean equals(final Object other){
        return other instanceof NotToken;
    }
    public int hashCode(){
        return 24;
    }
    public String toString(){
        return "!";
    }
}
