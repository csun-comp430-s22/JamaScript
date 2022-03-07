package com.jamascript;

public class CommaToken implements Token {
    public boolean equals(final Object other){
        return other instanceof CommaToken;
    }
    public int hashCode(){
        return 26;
    }
    public String toString(){
        return ",";
    }
}
