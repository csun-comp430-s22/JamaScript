package com.jamascript.parser;

public class ParseResult<A> {
    public final A result;
    public final int position;

    public ParseResult(final A result, final int position) {
        this.result = result;
        this.position = position;
    }
}
