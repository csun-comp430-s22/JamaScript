package com.jamascript;

public class NumberToken implements Token{
    public final String number;
   
    public NumberToken(String number)
    {
        this.number = number;
    }

    public boolean equals(final Object other){
        if(other instanceof NumberToken) {
            final NumberToken otherNumber = (NumberToken) other;
            return number.equals(otherNumber.number);
        } else {
            return false;
        }
    }

    public int hashCode()
    {
        return number.hashCode();
    }

    public String toString()
    {
        return "Number(" + number + ")";
    }
}
