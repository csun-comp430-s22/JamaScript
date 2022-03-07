package com.jamascript;

public class ExtendsToken implements Token{
    public boolean equals(final Object other){
        return other instanceof ExtendsToken;
    }
    public int hashCode(){
        return 27;
    }
    public String toString(){
        return "extends";
    }
}
