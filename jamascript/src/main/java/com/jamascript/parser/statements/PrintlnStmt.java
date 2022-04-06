package com.jamascript.parser.statements;
import com.jamascript.parser.expressions.*;

public class PrintlnStmt implements Stmt{
    public final Exp exp;

    public PrintlnStmt(final Exp exp) {
        this.exp = exp;
    }

    public int hashCode() {
        return exp.hashCode();
    }
    
    public boolean equals(final Object other) {
        return (other instanceof PrintlnStmt &&
                exp.equals(((PrintlnStmt)other).exp));
    }

    public String toString() {
        return "Println(" + exp.toString() + ")";
    }
}
