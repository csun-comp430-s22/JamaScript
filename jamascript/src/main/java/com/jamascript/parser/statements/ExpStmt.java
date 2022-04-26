package com.jamascript.parser.statements;

import com.jamascript.parser.expressions.*;

public class ExpStmt implements Stmt{
    public final Exp exp;

    public ExpStmt(final Exp exp) {
        this.exp = exp;
    }

    public int hashCode() { return exp.hashCode(); }

    public boolean equals(final Object other) {
        return (other instanceof ExpStmt &&
                exp.equals(((ExpStmt)other).exp));
    }

    public String toString() {
        return "ExpStmt(" + exp.toString() + ")";
    }
}
