package com.jamascript;
public class PlusToken implements Token {
    public boolean equals(final Object other){
        return other instanceof PlusToken;
    }
    public int hashCode(){
        return 13;
    }
    public String toString(){
        return "+";
    }
}
