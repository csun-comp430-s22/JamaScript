package com.jamascript.Tokens;

public class QuotationMarkToken implements Token {
    public boolean equals(final Object other){
        return other instanceof QuotationMarkToken;
    }
    public int hashCode(){
        return 23;
    }
    public String toString(){
        return "\"";
    }
}
