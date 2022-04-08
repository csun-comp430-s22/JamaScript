package com.jamascript.parser;

// represents success in parsing
public class ParseResult<A> {
    public final A result;
    public final int position;

    public ParseResult(final A result,
                       final int position) {
        this.result = result;
        this.position = position;
    }

    public int hashCode() {
        return result.hashCode() + position;
    }

    public boolean equals(final Object other) {
        if (other instanceof ParseResult) {
            final ParseResult<A> otherResult = (ParseResult<A>)other;
            return (result.equals(otherResult.result) &&
                    position == otherResult.position);
        } else {
            return false;
        }
    }

    public String toString() {
        return "ParseResult(" + result.toString() + ", " + position + ")";
    }
}
