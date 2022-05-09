package com.jamascript.parser.methodInformation;

import java.util.List;

import com.jamascript.typechecker.types.Type;

public class MethodSignature {
    public final MethodName methodName;
    public final List<Type> params;
    
    public MethodSignature(final MethodName methodName, final List<Type> params) {
        this.methodName = methodName;
        this.params = params;
    }

    // public int hashCode() {

    //     return methodName.hashCode() + params.hashCode();
    // }

    // public boolean equals(final MethodSignature other) {
    //     if(other instanceof MethodSignature) {
    //         if(methodName.equals(((MethodSignature)other).methodName) && 
    //             params.equals(((MethodSignature)other).params)) {
    //                 return true;
    //         }
    //         return false;
    //     }
    //     return false;
    // }

    public int hashCode() {
        return methodName.name.hashCode() + params.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof MethodSignature &&
                methodName.name.equals(((MethodSignature)other).methodName.name) &&
                params.equals(((MethodSignature)other).params));
    }

    public String toString() {
        return "MethodName: " + methodName + " params: " + params;
    }
}
