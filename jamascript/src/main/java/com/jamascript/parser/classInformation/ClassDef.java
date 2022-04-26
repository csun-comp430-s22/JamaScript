package com.jamascript.parser.classInformation;
import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.methodInformation.MethodDef;
import com.jamascript.parser.statements.*;

import java.util.List;

public class ClassDef {
    public final ClassName className;
    public final ClassName extendsClassName;
    public final List<Vardec> instanceVariables;
    public final List<Vardec> constructorArguments;
    public final List<Exp> superParams;
    public final List<Stmt> constructorBody;
    public final List<MethodDef> methods;

    public ClassDef(final ClassName className,
                    final ClassName extendsClassName,
                    final List<Vardec> instanceVariables,
                    final List<Vardec> constructorArguments,
                    final List<Exp> superParams,
                    final List<Stmt> constructorBody,
                    final List<MethodDef> methods) {
        this.className = className;
        this.extendsClassName = extendsClassName;
        this.instanceVariables = instanceVariables;
        this.constructorArguments = constructorArguments;
        this.superParams = superParams;
        this.constructorBody = constructorBody;
        this.methods = methods;
    }

    public int hashCode() {
        return (className.hashCode() +
                extendsClassName.hashCode() +
                instanceVariables.hashCode() +
                constructorArguments.hashCode() +
                superParams.hashCode() +
                constructorBody.hashCode() +
                methods.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassDef) {
            final ClassDef otherClass = (ClassDef)other;
            return (className.equals(otherClass.className) &&
                    extendsClassName.equals(otherClass.extendsClassName) &&
                    instanceVariables.equals(otherClass.instanceVariables) &&
                    constructorArguments.equals(otherClass.constructorArguments) &&
                    superParams.equals(otherClass.superParams) &&
                    constructorBody.equals(otherClass.constructorBody) &&
                    methods.equals(otherClass.methods));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassDef(" + className.toString() + ", " +
                extendsClassName.toString() + ", " +
                instanceVariables.toString() + ", " +
                constructorArguments.toString() + ", " +
                superParams.toString() + ", " +
                constructorBody.toString() + ", " +
                methods.toString());
    }
}
