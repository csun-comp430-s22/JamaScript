package com.jamascript.lexer;

public interface Token {
    public boolean equals(final Object other);

    public int hashCode();

    public String toString();
}