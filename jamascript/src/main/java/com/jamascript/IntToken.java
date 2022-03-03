package com.jamascript;

public class IntToken {
    public boolean equals(final Object other){
        return other instanceof IntToken;
    }
    public int hashCode(){
        return 20;
    }
    public String toString(){
        return "Int";
    }
}
