package com.jamascript.parser;
import com.jamascript.parser.classInformation.*;
import com.jamascript.parser.statements.*;

import java.util.List;

public class Program {
    public final List<ClassDef> classes;
    public final Stmt entryPoint;

    public Program(final List<ClassDef> classes,
                   final Stmt entryPoint) {
        this.classes = classes;
        this.entryPoint = entryPoint;
    }

    public int hashCode() {
        return classes.hashCode() + entryPoint.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof Program) {
            final Program otherProgram = (Program)other;
            return (classes.equals(otherProgram.classes) &&
                    entryPoint.equals(otherProgram.entryPoint));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("Program(" + classes.toString() + ", " +
                entryPoint.toString() + ")");
    }
}


