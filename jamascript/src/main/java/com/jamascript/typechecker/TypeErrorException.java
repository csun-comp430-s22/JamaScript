package com.jamascript.typechecker;

public class TypeErrorException extends Exception {
    public TypeErrorException(final String message) {
        super(message);
    }
}