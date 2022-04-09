package com.jamascript.parser;

import com.jamascript.parser.expressions.*;

public class ExpDotMethodCallExp implements Exp{
    public final ParseResult<Exp> exp;
    public final MethodCallExp method;

    public ExpDotMethodCallExp(ParseResult<Exp> exp, MethodCallExp method) {
        this.exp = exp;
        this.method = method;
    }

    public int hashCode() {
        return (exp.hashCode() +
                method.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof ExpDotMethodCallExp) {
            final ExpDotMethodCallExp otherExp = (ExpDotMethodCallExp) other;
            return (exp.equals(otherExp.exp) &&
                    method.equals(otherExp.method));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ExpDotMethodCallExp(" + exp.toString() + ", " +
                method.toString() + ")");
    }
}
