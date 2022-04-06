package com.jamascript.parser.expressions;
import com.jamascript.parser.MethodName;
import java.util.List;

public class MethodCallExp implements Exp {
    public final MethodName mname;
    public final List<Exp> params;

    public MethodCallExp(final MethodName mname,
                           final List<Exp> params) {
        this.mname = mname;
        this.params = params;
    }

    public int hashCode() {
        return (mname.hashCode() +
                params.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodCallExp) {
            final MethodCallExp asFunc = (MethodCallExp)other;
            return (mname.equals(asFunc.mname) &&
                    params.equals(asFunc.params));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("MethodCallExp(" + mname.toString() + ", " +
                params.toString() + ")");
    }
}