package com.jamascript;

public class MultiplyToken implements Token{
    public boolean equals(final Object other){
        return other instanceof MultiplyToken;
    }
    public int hashCode(){
        return 15;
    }
    public String toString(){
        return "*";
    }
}