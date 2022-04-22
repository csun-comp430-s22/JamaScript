package com.jamascript.parser.statements;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.*;

public class VariableInitializationStmt implements Stmt {
    public final Vardec vardec;
    public final Exp exp;

    public VariableInitializationStmt(final Vardec vardec,
                                      final Exp exp) {
        this.vardec = vardec;
        this.exp = exp;
    }

    public int hashCode() {
        return vardec.hashCode() + exp.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof VariableInitializationStmt) {
            final VariableInitializationStmt otherVar =
                (VariableInitializationStmt)other;
            return (vardec.equals(otherVar.vardec) &&
                    exp.equals(otherVar.exp));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("VariableInitializationStmt(" +
                vardec.toString() + ", " +
                exp.toString() + ")");
    }
}