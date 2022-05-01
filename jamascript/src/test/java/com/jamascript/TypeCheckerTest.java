package com.jamascript;

import com.jamascript.parser.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.classInformation.*;
import com.jamascript.parser.methodInformation.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.*;
import com.jamascript.typechecker.types.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Map;

import com.jamascript.typechecker.TypeChecking;
import com.jamascript.lexer.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TypeCheckerTest {
    public static final TypeChecking emptyTypechecker =
        new TypeChecking(new Program(new ArrayList<ClassDef>(),
                                    new ExpStmt(new IntegerLiteralExp(0))));

    @Test
    public void testVariableInScope() throws TypeErrorException {
        final Type expectedType = new IntType();
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        typeEnvironment.put(new Variable("x"), new IntType());
        
        final Type receivedType =
            emptyTypechecker.typeofVariable(new VariableExp(new Variable("x")),
                                            typeEnvironment);
        assertEquals(expectedType, receivedType);
    }
    
    @Test(expected = TypeErrorException.class)
    public void testVariableOutOfScope() throws TypeErrorException {
        final Map<Variable, Type> typeEnvironment = new HashMap<Variable, Type>();
        emptyTypechecker.typeofVariable(new VariableExp(new Variable("x")),
                                        typeEnvironment);
    }

    @Test
    public void testThisInClass() throws TypeErrorException {
        assertEquals(new ClassNameType(new ClassName("foo")),
                     emptyTypechecker.typeofThis(new ClassName("foo")));
    }

    @Test(expected = TypeErrorException.class)
    public void testThisNotInClass() throws TypeErrorException {
        emptyTypechecker.typeofThis(null);
    }

    
}
