package com.jamascript;

public class SemicolonToken implements Token {
    public boolean equals(final Object other){
        return other instanceof SemicolonToken;
    }
    public int hashCode(){
        return 8;
    }
    public String toString(){
        return ";";
    }
}
