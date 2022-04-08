package com.jamascript.lexer;

public class MethodNameToken implements Token {
    public final String methodName;

    public MethodNameToken(final String methodName) {
        this.methodName = methodName;
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodNameToken) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return methodName.hashCode();
    }

    public String toString() {
        return "MethodName(" + methodName + ")";
    }
}
