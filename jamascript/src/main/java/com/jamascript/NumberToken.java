package com.jamascript;

public class NumberToken implements Token{
    public final int number;
   
    public NumberToken(final int number)
    {
        this.number = number;
    }

    public boolean equals(final Object other){
        if(other instanceof NumberToken) {
            final NumberToken otherNumber = (NumberToken) other;
            return number == otherNumber.number;
        } else {
            return false;
        }
    }

    public int hashCode()
    {
        return number;
    }

    public String toString()
    {
        return "Number(" + number + ")";
    }
}
