package com.jamascript.parser;

import com.jamascript.lexer.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.statements.*;

import java.util.List;
import java.util.ArrayList;

public class Parser {
    private final List<Token> tokens;

    public Parser(final List<Token> tokens) {
        this.tokens = tokens;
    }

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

    // primary_exp ::= x | i | `(` exp `)`
    public ParseResult<Exp> parsePrimaryExp(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<Exp>(new VariableExp(new Variable(name)),
                    position + 1);
        } else if (token instanceof NumberToken) {
            final int value = ((NumberToken) token).number;
            return new ParseResult<Exp>(new IntegerExp(value), position + 1);
        } else if (token instanceof LeftParenthesisToken) {
            final ParseResult<Exp> inParens = parseExp(position + 1);
            assertTokenHereIs(inParens.position, new RightParenthesisToken());
            return new ParseResult<Exp>(inParens.result,
                    inParens.position + 1);
        } else {
            throw new ParseException("Expected primary expression; received: " + token);
        }
    } // parsePrimaryExp

    // additive_op ::= + | -
    public ParseResult<Op> parseAdditiveOp(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof PlusToken) {
            return new ParseResult<Op>(new PlusOp(), position + 1);
        } else if (token instanceof MinusToken) {
            return new ParseResult<Op>(new MinusOp(), position + 1);
        } else {
            throw new ParseException("expected + or -; received: " + token);
        }
    } // parseAdditiveOp

    // additive_exp ::= primary_exp (additive_op primary_exp)*
    // 1 + 2
    //
    // 1 + 2
    public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {
        ParseResult<Exp> current = parsePrimaryExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
                final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
                current = new ParseResult<Exp>(new OpExp(current.result,
                        additiveOp.result,
                        anotherPrimary.result),
                        anotherPrimary.position);
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return current;
    } // parseAdditiveExp

    // less_than_exp ::= additive_exp (`<` additive_exp)*
    public ParseResult<Exp> parseLessThanExp(final int position) throws ParseException {
        ParseResult<Exp> current = parseAdditiveExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new LessThanToken());
                final ParseResult<Exp> other = parseAdditiveExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                        new LessThanOp(),
                        other.result),
                        other.position);
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return current;
    } // parseLessThanExp

    // equals_exp ::= less_than_exp (`==` less_than_exp)*
    public ParseResult<Exp> parseEqualsExp(final int position) throws ParseException {
        ParseResult<Exp> current = parseLessThanExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new EqualToken());
                final ParseResult<Exp> other = parseLessThanExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                        new EqualsOp(),
                        other.result),
                        other.position);
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return current;
    } // parseEqualsExp

    // exp ::= equals_exp
    public ParseResult<Exp> parseExp(final int position) throws ParseException {
        return parseEqualsExp(position);
    }

    // stmt ::= if (exp) stmt else stmt | { stmt* } | println(exp);
    public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
        final Token token = getToken(position);
        // if
        if (token instanceof IfToken) {
            assertTokenHereIs(position + 1, new LeftParenthesisToken());
            final ParseResult<Exp> guard = parseExp(position + 2);
            assertTokenHereIs(guard.position, new RightParenthesisToken());
            final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
            assertTokenHereIs(trueBranch.position, new ElseToken());
            final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
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
            throw new ParseException("expected statement; received: " + token);
        }
    } // parseStmt

    // program ::= stmt
    public ParseResult<Program> parseProgram(final int position) throws ParseException {
        final ParseResult<Stmt> stmt = parseStmt(position);
        return new ParseResult<Program>(new Program(stmt.result),
                stmt.position);
    } // parseProgram

    // intended to be called on the top-level
    public Program parseProgram() throws ParseException {
        final ParseResult<Program> program = parseProgram(0);
        // make sure all tokens were read in
        // if any tokens remain, then there is something extra at the end
        // of the program, which should be a syntax error
        if (program.position == tokens.size()) {
            return program.result;
        } else {
            throw new ParseException("Remaining tokens at end");
        }
    }
// parseProgram
}