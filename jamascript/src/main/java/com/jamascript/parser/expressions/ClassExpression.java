package com.jamascript.parser.expressions;
import com.jamascript.parser.ClassName;
import java.util.List;

public class ClassExpression {
    public final ClassName cname;
    public final List<Exp> params;

    public ClassExpression(final ClassName cname,
                           final List<Exp> params) {
        this.cname = cname;
        this.params = params;
    }

    public int hashCode() {
        return (cname.hashCode() +
                params.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodCallExp) {
            final ClassExpression asFunc = (ClassExpression) other;
            return (cname.equals(asFunc.cname) &&
                    params.equals(asFunc.params));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassExpression(" + cname.toString() + ", " +
                params.toString() + ")");
    }
}