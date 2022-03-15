package com.jamascript.parser.statements;
import com.jamascript.parser.expressions.*;

public class IfStmt implements Stmt {
    public final Exp guard;
    public final Stmt trueBranch;
    public final Stmt falseBranch;

    public IfStmt(final Exp guard,
            final Stmt trueBranch,
            final Stmt falseBranch) {
        this.guard = guard;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public boolean equals(final Object other) {
        if (other instanceof IfStmt) {
            final IfStmt otherStmt = (IfStmt) other;
            return (guard.equals(otherStmt.guard) &&
                    trueBranch.equals(otherStmt.trueBranch) &&
                    falseBranch.equals(otherStmt.falseBranch));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (guard.hashCode() +
                trueBranch.hashCode() +
                falseBranch.hashCode());
    }

    public String toString() {
        return ("IfStmt(" +
                guard.toString() + ", " +
                trueBranch.toString() + ", " +
                falseBranch.toString() + ")");
    }
}
