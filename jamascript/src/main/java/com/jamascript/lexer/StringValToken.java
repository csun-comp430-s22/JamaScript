package com.jamascript.lexer;

public class StringValToken implements Token{
    public final String value;

    public StringValToken(final String value){
        this.value = value;
    }
    public boolean equals(final Object other){
        if(other instanceof StringValToken){
            final StringValToken asString = (StringValToken)other;
            return value.equals(asString.value);
        }else{
            return false;
        }
    }

    public int hashCode(){
        return 33 + value.hashCode();
    }

    public String toString(){
        return "StringValueToken("+value+")";
    }
}
