package com.jamascript.parser.statements;

import com.jamascript.parser.Vardec;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.ParseResult;

public class VardecStmt implements Stmt {
    public final Vardec vardec;
    public final Exp exp;

    public VardecStmt(final Vardec vardec,
            final Exp exp) {
        this.vardec = vardec;
        this.exp = exp;
    }

    public int hashCode() {
        return vardec.hashCode() + exp.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VardecStmt) {
            final VardecStmt otherVar = (VardecStmt) other;
            return (vardec.equals(otherVar.vardec) &&
                    exp.equals(otherVar.exp));
        } else {
            return false;
        }
    }

    public String toString() {
        return "VardecStmt(" + vardec.toString() + " = " + exp.toString() + ";)";
    }
}