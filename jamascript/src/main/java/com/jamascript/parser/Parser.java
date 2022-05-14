package com.jamascript.parser;

import com.jamascript.lexer.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.methodInformation.MethodName;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;
import com.jamascript.parser.classInformation.*;
import com.jamascript.parser.methodInformation.MethodDef;

import java.util.List;
import java.util.ArrayList;

public class Parser {
    private final List<Token> tokens;

    public Parser(final List<Token> tokens) {
        this.tokens = tokens;
    }

    // helper functions
    public Token getToken(final int position) throws ParseException {
        if (position >= 0 && position < tokens.size()) {
            return tokens.get(position);
        } else {
            throw new ParseException("Invalid token position: " + position);
        }
    }

    public void assertTokenHereIs(final int position, final Token expected) throws ParseException {
        final Token received = getToken(position);
        if (!expected.equals(received)) {
            throw new ParseException("expected: " + expected + "; received: " + received);
        }
    }

    // op ::= `+` | `-` | `*` | `/` | `>` | `<` | `>=` | `<=` | `==` |
    public ParseResult<Op> parseOp(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof PlusToken) {
            return new ParseResult<Op>(new PlusOp(), position + 1);
        } else if (token instanceof MinusToken) {
            return new ParseResult<Op>(new MinusOp(), position + 1);
        } else if (token instanceof MultiplyToken) {
            return new ParseResult<Op>(new MultiplyOp(), position + 1);
        } else if (token instanceof DivideToken) {
            return new ParseResult<Op>(new DivideOp(), position + 1);
        } else if (token instanceof GreaterThanToken) {
            return new ParseResult<Op>(new GreaterThanOp(), position + 1);
        } else if (token instanceof LessThanToken) {
            return new ParseResult<Op>(new LessThanOp(), position + 1);
        } else if (token instanceof GreaterThanEqualToken) {
            return new ParseResult<Op>(new GreaterThanEqualsOp(), position + 1);
        } else if (token instanceof LessThanEqualToken) {
            return new ParseResult<Op>(new LessThanEqualsOp(), position + 1);
        } else if (token instanceof EqualEqualToken) {
            return new ParseResult<Op>(new EqualsEqualsOp(), position + 1);
        } else {
            throw new ParseException("expected: operator token; received: " + token);
        }
    }

    // exp ::= exp op exp
    public ParseResult<Exp> parseExpOpExp(final int position) throws ParseException {
        ParseResult<Exp> leftExp = parseExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                final ParseResult<Op> operator = parseOp(leftExp.position);
                final ParseResult<Exp> rightExp = parseExp(operator.position);
                leftExp = new ParseResult<Exp>(new OpExp(leftExp.result,
                        operator.result,
                        rightExp.result),
                        rightExp.position);
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return leftExp;
    }

    // exp ::= new classname(exp*)
    public ParseResult<Exp> parseClassExp(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof NewToken) {
            final Token nextToken = getToken(position + 1);
            if (nextToken instanceof ClassNameToken) {
                ClassNameToken cName = (ClassNameToken) nextToken;
                ClassName className = new ClassName(cName.name);

                final List<Exp> params = new ArrayList<Exp>();
                int curPosition = position + 3;
                boolean shouldRun = true;
                while (shouldRun) {
                    try {
                        final ParseResult<Exp> exp = parseExp(curPosition);
                        params.add(exp.result);
                        curPosition = exp.position + 1; // was exp.position
                    } catch (final ParseException e) {
                        shouldRun = false;
                    }
                }

                return new ParseResult<Exp>(
                        new NewExp(className, params),
                        curPosition);
            } else {
                throw new ParseException("expected: Class Name token; received: " + nextToken);
            }
        } else {
            throw new ParseException("expected: New token; received: " + token);
        }
    }
    // exp ::= exp.methodname(exp*)

    public ParseResult<Exp> parseMethodCallExp(final int position) throws ParseException {
        ParseResult<Exp> variable = parseExp(position);
        assertTokenHereIs(position + 1, new DotToken());
        final Token token = getToken(position + 2);
        MethodNameToken mName = (MethodNameToken) token;
        MethodName methodName = new MethodName(mName.name);

        assertTokenHereIs(position + 3, new LeftParenthesisToken());

        final List<Exp> params = new ArrayList<Exp>();
        int curPosition = position + 4;
        boolean shouldRun = true;
        while (shouldRun) {
            try {
                final ParseResult<Exp> exp = parseExp(curPosition);
                params.add(exp.result);
                curPosition = exp.position;
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return new ParseResult<Exp>(
                new MethodCallExp(variable.result, methodName, params),
                curPosition);
    }

    // comma_exp ::= [equals_exp (`,` equals_exp)*]
    public ParseResult<List<Exp>> parseCommaExp(int position) throws ParseException {
        final List<Exp> exps = new ArrayList<Exp>();

        try {
            ParseResult<Exp> currentExp = parseExp(position);
            exps.add(currentExp.result);
            position = currentExp.position;
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    assertTokenHereIs(position, new CommaToken());
                    currentExp = parseExp(currentExp.position + 1);
                    exps.add(currentExp.result);
                    position = currentExp.position;
                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
        } catch (final ParseException e) {
        }

        return new ParseResult<List<Exp>>(exps, position);
    }

    // exp ::= var | int | string | true| false |
    public ParseResult<Exp> parseExp(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<Exp>(new VariableExp(new Variable(name)),
                    position + 1);
        } else if (token instanceof NumberToken) {
            final int value = ((NumberToken) token).number;
            return new ParseResult<Exp>(new IntegerLiteralExp(value), position + 1);
        } else if (token instanceof StringValToken) {
            final String value = ((StringValToken) token).value;
            return new ParseResult<Exp>(new StringLiteralExp(value), position + 1);
        } else if (token instanceof TrueToken) {
            return new ParseResult<Exp>(new BooleanLiteralExp(true), position + 1);
        } else if (token instanceof FalseToken) {
            return new ParseResult<Exp>(new BooleanLiteralExp(false), position + 1);
        } else {
            throw new ParseException("Expected: expression token; received: " + token);
        }
    }

    // stmt ::= vardec '=' exp
    public ParseResult<Stmt> parseVarInit(final int position) throws ParseException {
        Token token = getToken(position);
        if (token instanceof IntToken) {
            final ParseResult<Vardec> v = parseVardec(position);
            final Vardec vardec = v.result;
            final ParseResult<Exp> exp = parseExp(position + 3);
            final Token nextToken = getToken(exp.position - 1);
            if (nextToken instanceof NumberToken) {
                return new ParseResult<Stmt>(
                        new VariableInitializationStmt(vardec, exp.result), // was exp.position
                        exp.position);
            } else {
                throw new ParseException("Expected: NumberToken; received : " + nextToken);
            }
        } else if (token instanceof StringToken) {
            final ParseResult<Vardec> v = parseVardec(position);
            final Vardec vardec = v.result;
            final ParseResult<Exp> exp = parseExp(position + 3);
            final Token nextToken = getToken(exp.position - 1);
            if (nextToken instanceof StringValToken) {
                return new ParseResult<Stmt>(
                        new VariableInitializationStmt(vardec, exp.result),
                        exp.position);
            } else {
                throw new ParseException("Expected: StringValToken; received : " + nextToken);
            }
        } else if (token instanceof BooleanToken) {
            final ParseResult<Vardec> v = parseVardec(position);
            final Vardec vardec = v.result;
            final ParseResult<Exp> exp = parseExp(position + 3);
            final Token nextToken = getToken(exp.position - 1);
            if (nextToken instanceof TrueToken || nextToken instanceof FalseToken) {
                return new ParseResult<Stmt>(
                        new VariableInitializationStmt(vardec, exp.result),
                        exp.position);
            } else {
                throw new ParseException("Expected: Boolean Token; received : " + nextToken);
            }
        } else if (token instanceof ClassNameToken) {
            final ParseResult<Vardec> v = parseVardec(position);
            final Vardec vardec = v.result;

            assertTokenHereIs(position + 2, new EqualToken());

            final Token nextToken = getToken(position + 3);
            if (nextToken instanceof NewToken) {
                final ParseResult<Exp> exp = parseClassExp(position + 3);
                return new ParseResult<Stmt>(
                        new VariableInitializationStmt(vardec, exp.result),
                        exp.position);
            } else {
                throw new ParseException("Expected: NewToken; received : " + nextToken);
            }
        } else {
            throw new ParseException("Expected: type token; received : " + token);
        }
    }

    // stmt ::= while (exp) stmt | if (exp)stmt else stmt; | { stmt* } |
    // println(exp);| return (exp) |
    public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof WhileToken) {
            assertTokenHereIs(position + 1, new LeftParenthesisToken());
            final ParseResult<Exp> guard = parseExp(position + 2);
            assertTokenHereIs(guard.position, new RightParenthesisToken());
            final ParseResult<Stmt> body = parseStmt(guard.position + 1);
            return new ParseResult<Stmt>(new WhileStmt(guard.result, body.result), body.position);
        } else if (token instanceof IfToken) {
            assertTokenHereIs(position + 1, new LeftParenthesisToken());
            final ParseResult<Exp> guard = parseExpOpExp(position + 2);
            assertTokenHereIs(guard.position, new RightParenthesisToken());
            final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
            assertTokenHereIs(trueBranch.position + 1, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 2);
            assertTokenHereIs(falseBranch.position, new RightCurlyBracketToken());
            return new ParseResult<Stmt>(new IfStmt(guard.result,
                    trueBranch.result,
                    falseBranch.result),
                    falseBranch.position);
        } else if (token instanceof LeftCurlyBracketToken) {
            final List<Stmt> stmts = new ArrayList<Stmt>();
            int curPosition = position + 1;
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    final ParseResult<Stmt> stmt = parseStmt(curPosition);
                    stmts.add(stmt.result);
                    curPosition = stmt.position + 1; // was stmt.position

                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
            return new ParseResult<Stmt>(new BlockStmt(stmts),
                    curPosition); // was curPosition + 1
        } else if (token instanceof PrintlnToken) {
            assertTokenHereIs(position + 1, new LeftParenthesisToken());
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenthesisToken());
            assertTokenHereIs(exp.position + 1, new SemicolonToken());
            return new ParseResult<Stmt>(new PrintlnStmt(exp.result),
                    exp.position + 1);
        } else if (token instanceof ReturnToken) {
            assertTokenHereIs(position + 1, new LeftParenthesisToken());
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenthesisToken());
            assertTokenHereIs(exp.position + 1, new SemicolonToken());
            return new ParseResult<Stmt>(new ReturnNonVoidStmt(exp.result),
                    exp.position + 1);
        } else if (token instanceof IntToken || token instanceof StringToken
                || token instanceof BooleanToken || token instanceof ClassNameToken) {
            return parseVarInit(position);
        } else {
            throw new ParseException("expected: statement token; received: " + token);
        }
    }

    // vardec ::= type x
    public ParseResult<Vardec> parseVardec(final int position) throws ParseException {
        final ParseResult<Type> type = parseType(position);
        final ParseResult<Exp> v = parseExp(type.position);
        final VariableExp var = (VariableExp) v.result;
        return new ParseResult<Vardec>(new Vardec(type.result, new Variable(((Variable) var.variable).name)),
                v.position);
    }

    // type ::= int | bool | void | classname
    public ParseResult<Type> parseType(int position) throws ParseException {
        final Token token = getToken(position);
        Type type = null;
        if (token instanceof IntToken) {
            type = new IntType();
            position++;
        } else if (token instanceof StringToken) {
            type = new StringType();
            position++;
        } else if (token instanceof BooleanToken) {
            type = new BoolType();
            position++;
        } else {
            ClassNameToken cName = (ClassNameToken) token;
            ClassName className = new ClassName(cName.name);
            type = new ClassType(className);
            position++;
        }

        return new ParseResult<Type>(type, position);
    }

    // vardecs_comma ::= [vardec (`,` vardec)*]
    public ParseResult<List<Vardec>> parseVardecsComma(int position) throws ParseException {
        final List<Vardec> vardecs = new ArrayList<Vardec>();

        try {
            ParseResult<Vardec> vardec = parseVardec(position);
            vardecs.add(vardec.result);
            position = vardec.position;
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    assertTokenHereIs(position, new CommaToken());
                    vardec = parseVardec(position + 1);
                    vardecs.add(vardec.result);
                    position = vardec.position;
                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
        } catch (final ParseException e) {
        }

        return new ParseResult<List<Vardec>>(vardecs, position);
    }

    // vardecs_semicolon ::= (vardec `;`)*
    public ParseResult<List<Vardec>> parseVardecsSemicolon(int position) throws ParseException {
        // final List<Vardec> vardecs = new ArrayList<Vardec>();
        // boolean shouldRun = true;
        // while (shouldRun) {
        //     try {
        //         final ParseResult<Vardec> vardec = parseVardec(position);
        //         assertTokenHereIs(vardec.position, new SemicolonToken());
        //         vardecs.add(vardec.result);
        //         position = vardec.position + 1;
        //     } catch (final ParseException e) {
        //         shouldRun = false;
        //     }
        // }

        // return new ParseResult<List<Vardec>>(vardecs, position);
        final List<Vardec> vardecs = new ArrayList<Vardec>();

        try {
            ParseResult<Vardec> vardec = parseVardec(position);
            vardecs.add(vardec.result);
            position = vardec.position;
            boolean shouldRun = true;
            while (shouldRun) {
                try {
                    assertTokenHereIs(position, new SemicolonToken());
                    vardec = parseVardec(position + 1);
                    vardecs.add(vardec.result);
                    position = vardec.position;
                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
        } catch (final ParseException e) {
        }

        return new ParseResult<List<Vardec>>(vardecs, position);
    }

    // methoddef ::= type methodname(vardecs*) stmt
    public ParseResult<MethodDef> parseMethodDef(final int position) throws ParseException {
        final ParseResult<Type> type = parseType(position);
        final Token token = getToken(type.position);
        if (token instanceof MethodNameToken) {
            MethodNameToken mName = (MethodNameToken) token;
            MethodName methodName = new MethodName(mName.name);
            // final ParseResult<MethodName> methodName = parseMethodName(type.position);
            assertTokenHereIs(position + 2, new LeftParenthesisToken());
            final ParseResult<List<Vardec>> arguments = parseVardecsComma(position + 3);
            assertTokenHereIs(arguments.position, new RightParenthesisToken());
            final ParseResult<Stmt> body = parseStmt(arguments.position + 1);
            return new ParseResult<MethodDef>(new MethodDef(type.result,
                    methodName,
                    arguments.result,
                    body.result),
                    body.position);
        } else {
            throw new ParseException("Expected MethodNameToken; Received: " + token);
        }
    }

    public ParseResult<List<MethodDef>> parseMethodDefs(int position) throws ParseException {
        final List<MethodDef> methodDefs = new ArrayList<MethodDef>();
        boolean shouldRun = true;
        while (shouldRun) {
            try {
                final ParseResult<MethodDef> methodDef = parseMethodDef(position);
                methodDefs.add(methodDef.result);
                position = methodDef.position;
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return new ParseResult<List<MethodDef>>(methodDefs, position);
    }

    // classdef ::= class classname extends classname {
    // vardec*
    // constructor(vardec*){
    // super(exp*)| used for initializing
    // superclasses
    // stmt* vars are comma-separated
    // }
    // methoddef*
    // }
    public ParseResult<ClassDef> parseClassDef(final int position) throws ParseException {
        // header
        assertTokenHereIs(position, new ClassToken());
        // final ParseResult<ClassName> className = parseClassName(position + 1);
        final Token token = getToken(position + 1);
        ClassNameToken cName = (ClassNameToken) token;
        ClassName className = new ClassName(cName.name);

        assertTokenHereIs(position + 2, new ExtendsToken());
        // final ParseResult<ClassName> extendsClassName = parseClassName(position + 2);
        final Token token2 = getToken(position + 3);
        ClassNameToken ecName = (ClassNameToken) token2;
        ClassName extendsClassName = new ClassName(ecName.name);

        assertTokenHereIs(position + 4, new LeftCurlyBracketToken());

        // instance variables
        final ParseResult<List<Vardec>> instanceVariables = parseVardecsSemicolon(position + 5);

        // constructor header
        assertTokenHereIs(instanceVariables.position, new ConstructorToken());
        assertTokenHereIs(instanceVariables.position + 1, new LeftParenthesisToken());
        final ParseResult<List<Vardec>> constructorArguments = parseVardecsComma(instanceVariables.position + 2);
        assertTokenHereIs(constructorArguments.position, new RightParenthesisToken());
        assertTokenHereIs(constructorArguments.position + 1, new LeftCurlyBracketToken());

        // constructor body
        assertTokenHereIs(constructorArguments.position + 2, new SuperToken());
        assertTokenHereIs(constructorArguments.position + 3, new LeftParenthesisToken());
        final ParseResult<List<Exp>> superParams = parseCommaExp(constructorArguments.position + 4);
        assertTokenHereIs(superParams.position, new RightParenthesisToken());
        assertTokenHereIs(superParams.position + 1, new SemicolonToken());

        // check////////////////////////////////
        final ParseResult<Stmt> constructorBody = parseStmt(superParams.position + 2);
        final BlockStmt constructorStmts = (BlockStmt) constructorBody.result;

        assertTokenHereIs(constructorBody.position, new RightCurlyBracketToken());

        // methods
        final ParseResult<List<MethodDef>> methodDefs = parseMethodDefs(constructorBody.position + 1);
        assertTokenHereIs(methodDefs.position, new RightCurlyBracketToken());

        return new ParseResult<ClassDef>(new ClassDef(className,
                extendsClassName,
                instanceVariables.result,
                constructorArguments.result,
                superParams.result,
                constructorStmts.stmts,
                methodDefs.result),
                methodDefs.position + 1);
    }

    // program ::= classdef* stmt
    // public ParseResult<Program> parseProgram(final int position) throws
    // ParseException{
    // final List<ClassDef> classes = new ArrayList<ClassDef>();
    // int currPosition = position;
    // boolean shouldRun = true;
    // while (shouldRun) {
    // try {
    // final ParseResult<ClassDef> exp = parseClass(currPosition);
    // classes.add(exp.result);
    // currPosition = exp.position;
    // // throw new ParseException("no classes");
    // } catch (final ParseException e) {
    // shouldRun = false;
    // }
    // }
    // final ParseResult<Stmt> stmt = parseStmt(currPosition);

    // return new ParseResult<Program>(new Program(classes, stmt.result),
    // stmt.position);
    // }

    // public Program parseProgram() throws ParseException {
    // final ParseResult<Program> program = parseProgram(0);
    // // make sure all tokens were read in
    // // if any tokens remain, then there is something extra at the end
    // // of the program, which should be a syntax error
    // if (program.position == tokens.size()) {
    // return program.result;
    // } else {
    // throw new ParseException("Remaining tokens at end");
    // }
    // }
}