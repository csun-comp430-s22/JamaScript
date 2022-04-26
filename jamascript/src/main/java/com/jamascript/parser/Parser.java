package com.jamascript.parser;

import com.jamascript.lexer.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;
import com.jamascript.parser.classInformation.*;

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
                while(shouldRun){
                    try{
                        final ParseResult<Exp> exp = parseExp(curPosition);
                        params.add(exp.result);
                        curPosition = exp.position;
                    }catch(final ParseException e){
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
    /*
     * public ParseResult<Exp> parseMethodCall(final int position) throws
     * ParseException{
     * ParseResult<Exp> variable = parseExp(position);
     * assertTokenHereIs(position + 1, new DotToken());
     * final Token token = getToken(position + 2);
     * MethodName methodName = (MethodName) token;
     * assertTokenHereIs(position + 3, new LeftParenthesisToken());
     * ParseResult<Exp> parameters = parseExp(position + 4);
     * assertTokenHereIs(parameters.position, new RightParenthesisToken());
     * 
     * return new ParseResult<Exp>(new MethodCallExp(variable.result, methodName,
     * (List<Exp>)parameters.result), parameters.position);
     * }
     */

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
            final VariableExp varExp = (VariableExp) (parseExp(position + 1).result);
            final Variable var = varExp.variable;
            final Vardec vardec = new Vardec(new IntType(), var);
            final ParseResult<Exp> exp = parseExp(position + 3);
            final Token nextToken = getToken(exp.position - 1);
            if (nextToken instanceof NumberToken) {
                return new ParseResult<Stmt>(
                        new VariableInitializationStmt(vardec, exp.result),
                        exp.position);
            } else {
                throw new ParseException("Expected: NumberToken; received : " + nextToken);
            }
        } else if (token instanceof StringToken) {
            final VariableExp varExp = (VariableExp) (parseExp(position + 1).result);
            final Variable var = varExp.variable;
            final Vardec vardec = new Vardec(new StringType(), var);
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
            final VariableExp varExp = (VariableExp) (parseExp(position + 1).result);
            final Variable var = varExp.variable;
            final Vardec vardec = new Vardec(new BoolType(), var);
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
            final ClassNameToken cName = (ClassNameToken) token;
            final VariableExp varExp = (VariableExp) (parseExp(position + 1).result);
            // Test
            final Variable var = varExp.variable;
            // Dog
            final ClassName name = new ClassName(cName.name);
            // Dog Test
            final Vardec vardec = new Vardec(new ClassType(name), var);
            // ("Sparky")
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

    // while (exp) stmt | if (exp)stmt else stmt; | { stmt*
    // } |
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
            assertTokenHereIs(guard.position, new RightParenthesisToken()); // good
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
                    curPosition = stmt.position;
                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
            return new ParseResult<Stmt>(new BlockStmt(stmts),
                    curPosition);
        } else if (token instanceof PrintlnToken) {
            assertTokenHereIs(position + 1, new LeftParenthesisToken());
            final ParseResult<Exp> exp = parseExp(position + 2);
            assertTokenHereIs(exp.position, new RightParenthesisToken());
            assertTokenHereIs(exp.position + 1, new SemicolonToken());
            return new ParseResult<Stmt>(new PrintlnStmt(exp.result),
                    exp.position + 2);
        } else {
            throw new ParseException("expected: statement token; received: " + token);
        }
    }

    // program ::= classDef
}