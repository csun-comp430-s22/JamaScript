package com.jamascript.parser.statements;

import java.util.List;

public class BlockStmt implements Stmt {
    public final List<Stmt> stmts;

    public BlockStmt(final List<Stmt> stmts) {
        this.stmts = stmts;
    }
}
