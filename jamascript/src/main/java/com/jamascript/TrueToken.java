package com.jamascript;

public class TrueToken implements Token{
    public boolean equals(final Object other){
        return other instanceof TrueToken;
    }
    public int hashCode(){
        return 0;
    }
    public String toString(){
        return "true";
    }
}