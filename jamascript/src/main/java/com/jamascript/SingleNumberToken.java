package com.jamascript;

public class SingleNumberToken implements Token{
    public final String number;
   
    public SingleNumberToken(String number)
    {
        // this.number = number;
        this.number = evaluateCorrectNumber(number);
    }

    private String evaluateCorrectNumber(String num) {
        switch(num) {
            case "0": return "0";
            case "1": return "1";
            case "2": return "2";
            case "3": return "3";
            case "4": return "4";
            case "5": return "5";
            case "6": return "6";
            case "7": return "7";
            case "8": return "8";
            case "9": return "9";
            default: return "Invalid entry";
        }
    }

    public boolean equals(final Object other){
        if(other instanceof SingleNumberToken) {
            final SingleNumberToken otherNumber = (SingleNumberToken) other;
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
