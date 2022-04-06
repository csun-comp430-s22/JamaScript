package com.jamascript.parser.expressions;
import com.jamascript.parser.*;

public class VariableExp implements Exp {
    public final Variable variable;

    public VariableExp(final Variable variable) {
        this.variable = variable;
    }

    public boolean equals(final Object other) {
        return (other instanceof VariableExp &&
                variable.equals(((VariableExp)other).variable));
    }

    public int hashCode() {
        return variable.hashCode();
    }

    public String toString() {
        return "VariableExp(" + variable.toString() + ")";
    }
}