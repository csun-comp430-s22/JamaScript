package com.jamascript.parser.statements;
import com.jamascript.parser.Vardec;
import com.jamascript.parser.expressions.*;

public class VardecStmt implements Stmt {
    public final Vardec vardec;
    public final Exp exp;

    public VardecStmt(final Vardec vardec,
                      final Exp exp) {
        this.vardec = vardec;
        this.exp = exp;
    }
}