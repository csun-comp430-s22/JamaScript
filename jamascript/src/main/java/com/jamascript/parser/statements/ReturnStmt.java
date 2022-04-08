package com.jamascript.parser.statements;
import com.jamascript.parser.expressions.*;

public class ReturnStmt {
    public final Exp exp;

    public ReturnStmt(final Exp exp) {
        this.exp = exp;
    }

    public int hashCode() {
        return exp.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof ReturnStmt &&
                exp.equals(((ReturnStmt) other).exp));
    }

    public String toString() {
        return "Return(" + exp.toString() + ")";
    }
}
