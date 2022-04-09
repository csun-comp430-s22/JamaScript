package com.jamascript.parser.statements;
import com.jamascript.parser.Vardec;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.ParseResult;

public class VardecStmt implements Stmt {
    public final Vardec vardec;
    public final ParseResult<Exp> exp;

    public VardecStmt(final Vardec vardec,
                      final ParseResult<Exp> exp) {
        this.vardec = vardec;
        this.exp = exp;
    }

    public int hashCode() {
        return vardec.hashCode() + exp.hashCode();
    }
    
    public boolean equals(final Object other) {
        return (other instanceof VardecStmt &&
                exp.equals(((VardecStmt) other).exp));
    }

    public String toString() {
        return "VardecStmt(" + vardec.toString() + " = " + exp.toString() + ";)";
    }
}