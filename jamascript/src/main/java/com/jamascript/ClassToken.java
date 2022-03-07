package com.jamascript;

public class ClassToken implements Token{
    public boolean equals(final Object other){
        return other instanceof ClassToken;
    }
    public int hashCode(){
        return 21;
    }
    public String toString(){
        return "class";
    }
}
