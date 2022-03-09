package com.jamascript.Tokens;

public class StringToken implements Token {
    public boolean equals(final Object other){
        return other instanceof StringToken;
    }
    public int hashCode(){
        return 19;
    }
    public String toString(){
        return "String";
    }
}
