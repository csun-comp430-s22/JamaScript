package com.jamascript.parser.expressions;

import java.util.List;

import com.jamascript.parser.methodInformation.MethodName;

public class MethodCallExp implements Exp {
    public final Exp target;
    public final MethodName mname;
    public final List<Exp> params;

    public MethodCallExp(final Exp target, final MethodName mname,
            final List<Exp> params) {
        this.target = target;
        this.mname = mname;
        this.params = params;
    }

    public int hashCode() {
        return (target.hashCode() +
                mname.hashCode() +
                params.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodCallExp) {
            final MethodCallExp call = (MethodCallExp) other;
            return (target.equals(call.target) &&
                    mname.equals(call.mname) &&
                    params.equals(call.params));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("MethodCallExp(" + target.toString() + ", " +
                mname.toString() + ", " +
                params.toString() + ")");
    }
}