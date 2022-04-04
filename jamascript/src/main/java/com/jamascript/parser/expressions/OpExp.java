package com.jamascript.parser.expressions;
import com.jamascript.parser.operators.*;

public class OpExp {
    public final Exp left;
    public final Op op;
    public final Exp right;

    public OpExp(final Exp left,
                 final Op op,
                 final Exp right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public boolean equals(final Object other) {
        if (other instanceof OpExp) {
            final OpExp otherExp = (OpExp)other;
            return (left.equals(otherExp.left) &&
                    op.equals(otherExp.op) &&
                    right.equals(otherExp.right));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (left.hashCode() +
                op.hashCode() +
                right.hashCode());
    }
    
    public String toString() {
        return ("OpExp(" +
                left.toString() + ", " +
                op.toString() + ", " +
                right.toString() + ")");
    }
    
}
