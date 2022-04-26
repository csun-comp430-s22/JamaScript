package com.jamascript.parser;

import com.jamascript.lexer.*;
import com.jamascript.parser.operators.*;
import com.jamascript.parser.expressions.*;
import com.jamascript.parser.statements.*;
import com.jamascript.typechecker.types.*;
import com.jamascript.parser.classInformation.*;

import java.util.List;
import java.util.ArrayList;

public class ParserOld {
    private final List<Token> tokens;

    public ParserOld(final List<Token> tokens) {
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

    // primary_exp ::= var | string | int | exp op exp
    public ParseResult<Exp> parsePrimaryExp(final int position) throws ParseException {

        // get the current Token from our list of tokens at this position
        final Token token = getToken(position);

        // var
        if (token instanceof VariableToken) {
            final String name = ((VariableToken) token).name;
            return new ParseResult<Exp>(new VariableExp(new Variable(name)),
                    position + 1);

        }
        // string
        else if (token instanceof StringValToken) { // if the current token is a String Val token
            // Get string val of the StringValToken
            final String value = ((StringValToken) token).value;

            // Create and return a new ParseResult with the StringExpression and the
            // current position + 1
            return new ParseResult<Exp>(new StringLiteralExp(value),
                    position + 1);

        }
        // int
        else if (token instanceof NumberToken) { // if the current token is a Number token
            // Get the number of the Number token
            final int value = ((NumberToken) token).number;

            // Create and return a new ParseResult with the IntegerExpression and the
            // current position + 1
            return new ParseResult<Exp>(new IntegerLiteralExp(value),
                    position + 1);

        }
        // '(' exp ')'
        else if (token instanceof LeftParenthesisToken) { // if the current token is a Left Parenthesis

            final ParseResult<Exp> inParens = parseExp(position + 1);
            assertTokenHereIs(inParens.position, new RightParenthesisToken());
            return new ParseResult<Exp>(inParens.result,
                    inParens.position + 1);

        }
        // new classname(exp*)
        else if (token instanceof NewToken) {
            ClassName className = (ClassName) getToken(position + 1);
            final ParseResult<Exp> params = parseExp(position + 2);
            final List<Exp> param = (List<Exp>) parseExp(position + 2);
            assertTokenHereIs(params.position, new RightParenthesisToken());
            return new ParseResult<Exp>(new NewExp(className,
                    param),
                    params.position);
        }
        // idk yet
        else if (token instanceof TrueToken) {
            return new ParseResult<Exp>(new BooleanLiteralExp(true), position + 1);
        } else if (token instanceof FalseToken) {
            return new ParseResult<Exp>(new BooleanLiteralExp(false), position + 1);
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
    public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {

        // Parse Exception thrown if primary expression isn't a Number, Variable, or
        // Left Parenthesis
        ParseResult<Exp> current = parsePrimaryExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                // Gets a parse result of PlusOp or MinusOp based on if it is a PlusToken or
                // MinusToken
                final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);

                // Gets whatever is to the right of the operator (number, variable, '(calls
                // checks for expressions again)')
                final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);

                // primaryExpression ::= Number, Variable, (other expression)
                // OpExp(primaryExpression value, Operator(Plus or Minus), primaryExpression
                // value)
                // anotherPrimary.position is the last offset from the last evaluated expression
                current = new ParseResult<Exp>(new OpExp(current.result,
                        additiveOp.result,
                        anotherPrimary.result),
                        anotherPrimary.position);
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return current;
    }

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

    // greater_than_exp ::= LessThan_exp (`>` Lessthan_exp)*
    public ParseResult<Exp> parseGreaterThanExp(final int position) throws ParseException {
        ParseResult<Exp> current = parseLessThanExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                assertTokenHereIs(current.position, new GreaterThanToken());
                final ParseResult<Exp> other = parseLessThanExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                        new GreaterThanOp(),
                        other.result),
                        other.position);
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }

        return current;
    } // parseGreaterThanExp

    // equals_exp ::= greater_than_exp (`==` greater_than_exp)*
    public ParseResult<Exp> parseEqualsExp(final int position) throws ParseException {
        ParseResult<Exp> current = parseGreaterThanExp(position);
        boolean shouldRun = true;

        while (shouldRun) {
            try {

                // if the current token we are looking at is an Equals Token '='
                assertTokenHereIs(current.position, new EqualEqualToken());

                final ParseResult<Exp> other = parseGreaterThanExp(current.position + 1);
                current = new ParseResult<Exp>(new OpExp(current.result,
                        new EqualsEqualsOp(),
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

    // stmt ::= if (exp) stmt else stmt | { stmt* } | println(exp) | while (exp)
    // stmt;
    public ParseResult<Stmt> parseStmt(final int position) throws ParseException {

        // get the current token at the current position
        final Token token = getToken(position);

        // if the token we are looking at is an if Token
        if (token instanceof IfToken) {

            // if the next token is '(' then we have 'if('
            assertTokenHereIs(position + 1, new LeftParenthesisToken());

            // 'if(exp'
            final ParseResult<Exp> guard = parseExp(position + 2);

            // 'if(exp)'
            assertTokenHereIs(guard.position, new RightParenthesisToken());

            // 'if(exp){ evaluate what is in here'
            final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);

            // 'if(exp){ evaluate what is in here }'
            assertTokenHereIs(trueBranch.position, new RightCurlyBracketToken());

            // 'if(exp){ evaluate what is in here } else'
            assertTokenHereIs(trueBranch.position + 1, new ElseToken());

            // else { evaluate what is in here
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

                    // loops through all statements in block
                    final ParseResult<Stmt> stmt = parseStmt(curPosition);
                    stmts.add(stmt.result);
                    curPosition = stmt.position;

                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
            return new ParseResult<Stmt>(new BlockStmt(stmts),
                    curPosition);
        } else if (token instanceof PrintlnToken) { // returns println(evaluated expressions);
            assertTokenHereIs(position + 1, new LeftParenthesisToken());

            final ParseResult<Exp> exp = parseExp(position + 2);

            assertTokenHereIs(exp.position, new RightParenthesisToken());

            assertTokenHereIs(exp.position + 1, new SemicolonToken());

            return new ParseResult<Stmt>(new PrintlnStmt(exp.result),
                    exp.position + 2);
        } else if (token instanceof WhileToken) { // if while token

            // 'while('
            assertTokenHereIs(position + 1, new LeftParenthesisToken());

            // 'while(exp'
            final ParseResult<Exp> guard = parseExp(position + 2);

            // 'while(exp)'
            assertTokenHereIs(guard.position, new RightParenthesisToken());

            // 'while(exp){ evaluate what is in here'
            final ParseResult<Stmt> body = parseStmt(guard.position + 1);

            // 'while(exp){ evaluate what is in here}'
            assertTokenHereIs(body.position, new RightCurlyBracketToken());

            return new ParseResult<Stmt>(new WhileStmt(guard.result,
                    body.result),
                    body.position);
        } /*
           * else if (token instanceof IntToken) { // returns}
           * 
           * // Int
           * Token nextToken = getToken(position + 1);
           * if (nextToken instanceof VariableToken) {
           * 
           * // Int test
           * VariableToken varToken = (VariableToken) nextToken;
           * String variableName = varToken.name;
           * 
           * // Int test =
           * assertTokenHereIs(position + 2, new EqualToken());
           * 
           * // Int hello = parseHere
           * // we can have:
           * // Int hello = 5 + 2;
           * // Int hello2 = 3 + hello;
           * final ParseResult<Exp> exp = parseExp(position + 3);
           * 
           * // Int hello = exp;
           * assertTokenHereIs(exp.position, new SemicolonToken());
           * 
           * final Vardec variableDeclaration = new Vardec(
           * new IntType(),
           * new Variable(variableName));
           * 
           * final VardecStmt variableDeclarationStatement = new
           * VardecStmt(variableDeclaration, exp);
           * 
           * return new ParseResult<Stmt>(variableDeclarationStatement, exp.position + 1);
           * 
           * } else {
           * throw new ParseException("Expected Variable Token;");
           * }
           * } else if (token instanceof StringToken) { // returns}
           * 
           * // String
           * Token nextToken = getToken(position + 1);
           * if (nextToken instanceof VariableToken) {
           * 
           * // String test
           * VariableToken varToken = (VariableToken) nextToken;
           * String variableName = varToken.name;
           * 
           * // String test =
           * assertTokenHereIs(position + 2, new EqualToken());
           * 
           * // String hello = parseHere
           * // we can have:
           * // String hello = 5 + 2;
           * // String hello2 = 3 + hello;
           * final ParseResult<Exp> exp = parseExp(position + 3);
           * 
           * // String hello = exp;
           * assertTokenHereIs(exp.position, new SemicolonToken());
           * 
           * final Vardec variableDeclaration = new Vardec(
           * new StringType(),
           * new Variable(variableName));
           * 
           * final VardecStmt variableDeclarationStatement = new
           * VardecStmt(variableDeclaration, exp);
           * 
           * return new ParseResult<Stmt>(variableDeclarationStatement, exp.position + 1);
           * 
           * }else {
           * throw new ParseException("Expected Variable Token;");
           * }
           * }
           */ else {
            throw new ParseException("expected statement; received: " + token);
        }
    } // parseStmt

    // program ::= stmt
    /*public ParseResult<ProgramStmt> parseProgram(final int position) throws ParseException {
        final ParseResult<Stmt> stmt = parseStmt(position);
        return new ParseResult<ProgramStmt>(new ProgramStmt(stmt.result),
                stmt.position);
    } // parseProgram

    // intended to be called on the top-level
    public ProgramStmt parseProgram() throws ParseException {
        final ParseResult<ProgramStmt> program = parseProgram(0);
        // make sure all tokens were read in
        // if any tokens remain, then there is something extra at the end
        // of the program, which should be a syntax error
        if (program.position == tokens.size()) {
            return program.result;
        } else {
            throw new ParseException("Remaining tokens at end");
        }
    }*/
    // parseProgram
}

/////////////////////////////////////////////////////////////////////////////////////////
/*
 * public String getMethodName(int position) throws ParseException {
 * Token currToken = getToken(position);
 * 
 * if (currToken instanceof VariableToken) {
 * VariableToken varToken = (VariableToken) currToken;
 * return varToken.name;
 * } else {
 * throw new ParseException("Expected Variable Token; got: " + currToken);
 * }
 * }
 * 
 * public ParseResult<Exp> parseMethodCallExp(MethodName methodName,
 * ParseResult<Exp> varExp, int position)
 * throws ParseException {
 * List<ParseResult<Exp>> parameters = getParameters(position);
 * 
 * int currPosition = parameters.get(parameters.size() - 1).position + 1;
 * assertTokenHereIs(currPosition, new SemicolonToken());
 * 
 * ParseResult<Exp> result = new ParseResult<Exp>(
 * new ExpDotMethodCallExp(varExp, new MethodCallExp(methodName, parameters)),
 * currPosition + 1);
 * 
 * return result;
 * }
 */

// additive_exp ::= primary_exp (additive_op primary_exp)*
// 1 + 2
//
// 1 + 2

// public ParseResult<Op> parseNewOp(final int position) throws ParseException {
// final Token token = getToken(position);

// if(token instanceof NewToken) {
// return new ParseResult<Op>(new NewOp(), position + 1);
// } else {
// throw new ParseException("expected 'new'; received: " + token);
// }
// }

// public List<ParseResult<Exp>> getParameters(int position) throws
// ParseException {
// List<ParseResult<Exp>> parameters = new ArrayList<ParseResult<Exp>>();
// boolean commasExist = true;

// // we want format 'exp, exp, exp)'
// while (commasExist) {

// ParseResult<Exp> currentExpression = parseExp(position);

// System.out.println(currentExpression);

// parameters.add(currentExpression);
// position = currentExpression.position;

// System.out.println("After evaluate expression in get param: " + position);

// // if the next token after the expression is done identifying is not a comma
// // token
// Token currentToken = getToken(position);

// if (!(currentToken instanceof CommaToken)) {

// // if 'new TestClassName(exp,exp)'
// if (currentToken instanceof RightParenthesisToken) {
// commasExist = false;
// } else {
// throw new ParseException("Expected RightParenthesisToken.");
// }
// }
// position++;
// }
// return parameters;
// }

// public ParseResult<Exp> parseClassExp(final Op newOp, final ClassName
// className, final int position)
// throws ParseException {
// System.out.println("pos class exp: " + position);
// List<ParseResult<Exp>> parameters = getParameters(position);

// try {

// return new ParseResult<Exp>(new ClassExpression(newOp, className,
// parameters),
// parameters.get(parameters.size() - 1).position + 1);
// // position + 1 because we want the token after ')' in 'new
// // TestClassName(exp,exp)'

// } catch (final ParseException e) {
// throw new ParseException("Parse Class Expression failed.");
// }

// }