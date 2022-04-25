package com.jamascript.parser.methodInformation;
import java.util.List;

import com.jamascript.parser.Vardec;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;


public class MethodDef {
    public final Type returnType;
    public final MethodName mname;
    public final List<Vardec> arguments;
    public final Stmt body;

    public MethodDef(final Type returnType,
                final MethodName mname,
                final List<Vardec> arguments,
                final Stmt body) {
        this.returnType = returnType;
        this.mname = mname;
        this.arguments = arguments;
        this.body = body;
    }

    public int hashCode() {
        return (returnType.hashCode() +
                mname.hashCode() +
                arguments.hashCode() +
                body.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodDef) {
            final MethodDef otherDef = (MethodDef)other;
            return (returnType.equals(otherDef.returnType) &&
                    mname.equals(otherDef.mname) &&
                    arguments.equals(otherDef.arguments) &&
                    body.equals(otherDef.body));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("Mdef(" + returnType.toString() + ", " +
                mname.toString() + ", " +
                arguments.toString() + ", " +
                body.toString() + ")");
    }
}