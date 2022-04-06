package com.jamascript.parser.statements;
import com.jamascript.parser.expressions.*;

public class WhileStmt implements Stmt {
    public final Exp guard;
    public final Stmt body;

    public WhileStmt(final Exp guard,
                     final Stmt body) {
        this.guard = guard;
        this.body = body;
    }

    public int hashCode() {
        return guard.hashCode() + body.hashCode();
    }
    
    public boolean equals(final Object other) {
        if (other instanceof WhileStmt) {
            final WhileStmt otherWhile = (WhileStmt)other;
            return (guard.equals(otherWhile.guard) &&
                    body.equals(otherWhile.body));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("WhileStmt(" + guard.toString() + ", " +
                body.toString() + ")");
    }
}