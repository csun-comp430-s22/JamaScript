package com.jamascript.parser.statements;

import com.jamascript.parser.Node;

public class ProgramStmt implements Node {
    public final Stmt stmt;

    public ProgramStmt(final Stmt stmt) {
        this.stmt = stmt;
    }
}
