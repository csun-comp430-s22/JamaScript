package com.jamascript.parser;
import com.jamascript.parser.expressions.ClassExpression;

public class ClassInstance {
    public final ClassType NEW;
    public final ClassExpression classExpression;

    public ClassInstance(final ClassType NEW, 
                         final ClassExpression classExpression) {
        this.NEW = NEW;
        this.classExpression = classExpression;
    }

    public int hashCode() {
        return NEW.hashCode() + classExpression.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassInstance) {
            final ClassInstance otherClassInstance = (ClassInstance) other;
            return (NEW.equals(otherClassInstance.NEW) &&
                    classExpression.equals(otherClassInstance.classExpression));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassInstance(" + NEW.toString() + ", " +
                    classExpression.toString() + ")");
    }
}
