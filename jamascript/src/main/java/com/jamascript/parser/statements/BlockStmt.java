package com.jamascript.parser.statements;

import java.util.List;

public class BlockStmt implements Stmt {
    public final List<Stmt> stmts;

    public BlockStmt(final List<Stmt> stmts) {
        this.stmts = stmts;
    }

    public boolean equals(final Object other) {
        if (other instanceof BlockStmt) {
            final BlockStmt otherStmt = (BlockStmt) other;

            // if both of their lists are the same size
            if(stmts.size() == otherStmt.stmts.size()) {
                for(int i = 0; i < stmts.size(); i++) {

                    // if we find an element that is not equal in both lists
                    // then the BlockStmts are not equal
                    if(!(stmts.get(i).equals(otherStmt.stmts.get(i)))) {
                        return false;
                    }   
                }
            } else {
                return false;
            }

            // if we can't find an element that isn't equal in both lists
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int hashC = 0;
        for(int i = 0; i < stmts.size(); i++) {
            hashC += stmts.get(i).hashCode();
        }

        return hashC;
    }

    public String toString() {

        String s = "Block statement {";

        for(int i = 0; i < stmts.size(); i++) {
            s += stmts.get(i).toString() + "\n";
        }
        s += "}";

        return s;
    }
}
