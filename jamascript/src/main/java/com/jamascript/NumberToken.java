package com.jamascript;

public class NumberToken implements Token{
    public final int number;
   
    public NumberToken(String number)
    {
        this.number = Integer.parseInt(number);
    }

    public boolean equals(final Object other){
        if(other instanceof NumberToken) {
            final NumberToken otherNumber = (NumberToken) other;
            return otherNumber.number == number;
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
