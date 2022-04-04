package com.jamascript.parser.statements;

import java.util.List;

public class BlockStmt {
    public final List<Stmt> stmts;

    public BlockStmt(final List<Stmt> stmts) {
        this.stmts = stmts;
    }
}
