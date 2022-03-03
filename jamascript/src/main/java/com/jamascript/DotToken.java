package com.jamascript;

public class DotToken implements Token{
    public boolean equals(final Object other){
        return other instanceof DotToken;
    }
    public int hashCode(){
        return 25;
    }
    public String toString(){
        return ".";
    }
}
