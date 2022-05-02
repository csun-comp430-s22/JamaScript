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
                        curPosition = exp.position;
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
                    curPosition = stmt.position;
                } catch (final ParseException e) {
                    shouldRun = false;
                }
            }
            return new ParseResult<Stmt>(new BlockStmt(stmts),
                    curPosition + 1);
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

//         classdef ::= class classname extends classname {
// 			    vardec*
// 			    constructor(vardec*){
//           	    super(exp*)| used for initializing                              
//                                superclasses
//                	stmt* vars are comma-separated
//              }
//              methoddef*
//          }


    public ParseResult<List<Vardec>> returnMethodParameters(Token currToken, int currPosition) throws ParseException{
        List<Vardec> parameters = new ArrayList<Vardec>();
        while(!(currToken instanceof RightParenthesisToken)) {
            ParseResult<Stmt> parameter = parseStmt(currPosition);
            if(parameter.result instanceof VariableInitializationStmt) {
                // int test(int x = 5;) -> skips after ; -> expects comma or RightParen
                // int test2(int x = 6; , int y = 3;)
                final Token commaOrRightParen = getToken(parameter.position + 1);
                if(commaOrRightParen instanceof CommaToken) {
                    VariableInitializationStmt currInitStmt = (VariableInitializationStmt) parameter.result;
                    parameters.add(currInitStmt.vardec); 
                    currPosition = parameter.position + 2;
                    currToken = getToken(currPosition);
                } else if(commaOrRightParen instanceof RightParenthesisToken) {
                    VariableInitializationStmt currInitStmt = (VariableInitializationStmt) parameter.result;
                    parameters.add(currInitStmt.vardec); 
                    currPosition = parameter.position + 2;
                    currToken = getToken(currPosition - 1);

                } else {
                    throw new ParseException("Expected right paren or comma. Recieved: " + commaOrRightParen);
                }   
            } else {
                throw new ParseException("Invalid constructor parameter. Expected VariableInitializationStmt. Recieved: " + parameter.result);
            }
        }
        // constructor(int x = 4;, int y = 5;, int z = 6;) {
        
        return new ParseResult<List<Vardec>>(parameters, currPosition);
    }

    public ParseResult<MethodDef> parseMethod(Token currToken, int currPosition) throws ParseException {
        
        MethodName mname = null;
        List<Vardec> arguments = new ArrayList<Vardec>();
        Stmt body = null;
        
        if(currToken instanceof StringToken) {
            currToken = getToken(currPosition + 1);
            if(currToken instanceof MethodNameToken) {
                final MethodNameToken currMethodNameToken = (MethodNameToken) currToken;
                mname = new MethodName(currMethodNameToken.name);
                
                assertTokenHereIs(currPosition + 2, new LeftParenthesisToken());
                currToken = getToken(currPosition + 3);

                final ParseResult<List<Vardec>> argumentResults = returnMethodParameters(currToken, currPosition + 3);
                assertTokenHereIs(argumentResults.position, new LeftCurlyBracketToken());
                arguments = argumentResults.result;

                ParseResult<Stmt> bodyResults = parseStmt(argumentResults.position + 1);
                body = bodyResults.result;

                return new ParseResult<MethodDef>(new MethodDef(new StringType(), mname, arguments, body), bodyResults.position + 1);
            }
            throw new ParseException("Expected MethodNameToken. Recieved " + currToken);
        }
        if(currToken instanceof BooleanToken) {
            currToken = getToken(currPosition + 1);
            if(currToken instanceof MethodNameToken) {
                final MethodNameToken currMethodNameToken = (MethodNameToken) currToken;
                mname = new MethodName(currMethodNameToken.name);
                
                assertTokenHereIs(currPosition + 2, new LeftParenthesisToken());
                currToken = getToken(currPosition + 3);

                final ParseResult<List<Vardec>> argumentResults = returnMethodParameters(currToken, currPosition + 3);
                assertTokenHereIs(argumentResults.position, new LeftCurlyBracketToken());
                arguments = argumentResults.result;

                ParseResult<Stmt> bodyResults = parseStmt(argumentResults.position + 1);
                body = bodyResults.result;
                
                return new ParseResult<MethodDef>(new MethodDef(new BoolType(), mname, arguments, body), bodyResults.position + 1);
            }
            throw new ParseException("Expected MethodNameToken. Recieved " + currToken);
        }
        if(currToken instanceof IntToken) {
            currToken = getToken(currPosition + 1);
            if(currToken instanceof MethodNameToken) {
                final MethodNameToken currMethodNameToken = (MethodNameToken) currToken;
                mname = new MethodName(currMethodNameToken.name);
                
                assertTokenHereIs(currPosition + 2, new LeftParenthesisToken());
                currToken = getToken(currPosition + 3);

                final ParseResult<List<Vardec>> argumentResults = returnMethodParameters(currToken, currPosition + 3);
                assertTokenHereIs(argumentResults.position, new LeftCurlyBracketToken());
                arguments = argumentResults.result;

                ParseResult<Stmt> bodyResults = parseStmt(argumentResults.position + 1);
                body = bodyResults.result;
                return new ParseResult<MethodDef>(new MethodDef(new IntType(), mname, arguments, body), bodyResults.position + 1);
            }
            throw new ParseException("Expected MethodNameToken. Recieved " + currToken);
        }
        if(currToken instanceof ClassNameToken) {
            ClassNameToken classNameType = (ClassNameToken) currToken;
            ClassName className = new ClassName(classNameType.name);

            currToken = getToken(currPosition + 1);
            if(currToken instanceof MethodNameToken) {
                final MethodNameToken currMethodNameToken = (MethodNameToken) currToken;
                mname = new MethodName(currMethodNameToken.name);
                
                assertTokenHereIs(currPosition + 2, new LeftParenthesisToken());
                currToken = getToken(currPosition + 3);

                final ParseResult<List<Vardec>> argumentResults = returnMethodParameters(currToken, currPosition + 3);
                assertTokenHereIs(argumentResults.position, new LeftCurlyBracketToken());
                arguments = argumentResults.result;

                ParseResult<Stmt> bodyResults = parseStmt(argumentResults.position + 1);
                body = bodyResults.result;
                return new ParseResult<MethodDef>(new MethodDef(new ClassNameType(className), mname, arguments, body), bodyResults.position + 1);
            }
            throw new ParseException("Expected MethodNameToken. Recieved " + currToken);
        }
        throw new ParseException("Expected method type. Recieved: " + currToken);
    }

    public ParseResult<List<MethodDef>> parseClassMethods(final int position) throws ParseException {

        List<MethodDef> methodDefs = new ArrayList<MethodDef>();

        int currPosition = position;
        Token currToken = getToken(currPosition);

        while(!(currToken instanceof RightCurlyBracketToken)) {
            ParseResult<MethodDef> methodDefResult = parseMethod(currToken, currPosition);
            methodDefs.add(methodDefResult.result);
            assertTokenHereIs(methodDefResult.position, new RightCurlyBracketToken());

            // pos: } or type methodname(vardec*)
            currPosition = methodDefResult.position + 1;
            currToken = getToken(currPosition);
        }

        return new ParseResult<List<MethodDef>>(methodDefs, currPosition);
    }

    public ParseResult<List<Stmt>> returnConstructorBody(Token currToken, int currPosition) throws ParseException {
        List<Stmt> constructorBody = new ArrayList<Stmt>();

        while(!(currToken instanceof RightCurlyBracketToken)) {
            ParseResult<Stmt> stmt = parseStmt(currPosition);   
            constructorBody.add(stmt.result);
            
            currPosition = stmt.position + 1;
            currToken = getToken(currPosition); 
        }

        return new ParseResult<List<Stmt>>(constructorBody, currPosition + 1);
    }

    public ParseResult<List<Exp>> returnSuperParams(Token currToken, int currPosition) throws ParseException {
        List<Exp> superParams = new ArrayList<Exp>();
        while(!(currToken instanceof RightParenthesisToken)) {
            ParseResult<Exp> param = parseExp(currPosition);
            final Token commaOrRightParen = getToken(param.position);
            if(commaOrRightParen instanceof CommaToken) {
                Exp currExp = (Exp) param.result;
                superParams.add(currExp); 
                currPosition = param.position + 1;
                currToken = getToken(currPosition);
            } else if(commaOrRightParen instanceof RightParenthesisToken) {
                Exp currExp = (Exp) param.result;
                superParams.add(currExp); 
                currPosition = param.position + 1;
                currToken = commaOrRightParen;
            } else {
                throw new ParseException("Expected right paren or comma. Recieved: " + commaOrRightParen);
            }  
        }
        return new ParseResult<List<Exp>>(superParams, currPosition);
    }

    public ParseResult<List<Vardec>> returnConstructorParameters(Token currToken, int currPosition) throws ParseException{
        List<Vardec> constructorArguments = new ArrayList<Vardec>();
    
        while(!(currToken instanceof RightParenthesisToken)) {
            ParseResult<Stmt> classField = parseStmt(currPosition);
            if(classField.result instanceof VariableInitializationStmt) {
                // constructor()
                // constructor(int x = 5;) -> skips after ; -> expects comma or RightParen
                // constructor(int x = 6; , int y = 3;)
                final Token commaOrRightParen = getToken(classField.position + 1);
                if(commaOrRightParen instanceof CommaToken) {
                    VariableInitializationStmt currInitStmt = (VariableInitializationStmt) classField.result;
                    constructorArguments.add(currInitStmt.vardec); 
                    currPosition = classField.position + 2;
                    currToken = getToken(currPosition);
                } else if(commaOrRightParen instanceof RightParenthesisToken) {
                    VariableInitializationStmt currInitStmt = (VariableInitializationStmt) classField.result;
                    constructorArguments.add(currInitStmt.vardec); 
                    currPosition = classField.position + 1;
                    currToken = getToken(currPosition);

                } else {
                    throw new ParseException("Expected right paren or comma. Recieved: " + commaOrRightParen);
                }   
            } else {
                throw new ParseException("Invalid constructor parameter. Expected VariableInitializationStmt. Recieved: " + classField.result);
            }
            
        }
        // constructor(int x = 4;, int y = 5;, int z = 6;) {
        
        return new ParseResult<List<Vardec>>(constructorArguments, currPosition + 1);
    }

    public ParseResult<List<Vardec>> returnInstanceVariables(Token currToken, int currPosition) throws ParseException {
        List<Vardec> instanceVariables = new ArrayList<Vardec>();
        while(!(currToken instanceof ConstructorToken)) {
            ParseResult<Stmt> classField = parseStmt(currPosition);
            if(classField.result instanceof VariableInitializationStmt) {
                VariableInitializationStmt currInitStmt = (VariableInitializationStmt) classField.result;
                instanceVariables.add(currInitStmt.vardec);
            } else {
                throw new ParseException("Expected VariableInitializationStmt. Recieved: " + classField.result);
            }
            currPosition = classField.position + 1;
            currToken = getToken(currPosition);
        }

        return new ParseResult<List<Vardec>>(instanceVariables, currPosition);
    }

    public ParseResult<ClassDef> parseClass(final int position) throws ParseException {
        ClassName className = null;
        ClassName extendsClassName = null;
        List<Vardec> instanceVariables = new ArrayList<Vardec>();
        List<Vardec> constructorArguments = new ArrayList<Vardec>();
        List<Exp> superParams = new ArrayList<Exp>();
        List<Stmt> constructorBody = new ArrayList<Stmt>();
        List<MethodDef> methods = new ArrayList<MethodDef>();
        
        // class classname
        assertTokenHereIs(position, new ClassToken());
        Token currToken = getToken(position + 1);
        if(currToken instanceof ClassNameToken) {
            ClassNameToken classNameToken = (ClassNameToken) currToken;
            className = new ClassName(classNameToken.name);
        }

        // extends classname {
        assertTokenHereIs(position + 2, new ExtendsToken());
        currToken = getToken(position + 3);
        if(currToken instanceof ClassNameToken) {
            ClassNameToken extendsClassNameToken = (ClassNameToken) currToken;
            extendsClassName = new ClassName(extendsClassNameToken.name);
        }
        assertTokenHereIs(position + 4, new LeftCurlyBracketToken());

        // vardec*
        int currPosition = position + 5;
        currToken = getToken(currPosition);
        ParseResult<List<Vardec>> instanceVariablesResult = returnInstanceVariables(currToken, currPosition);
        instanceVariables = instanceVariablesResult.result;
        currPosition = instanceVariablesResult.position;

        // constructor(vardec*) {
        assertTokenHereIs(currPosition, new ConstructorToken());
        assertTokenHereIs(currPosition + 1, new LeftParenthesisToken());
        currPosition = currPosition + 2;
        currToken = getToken(currPosition);
        ParseResult<List<Vardec>> constructorParamsResult = returnConstructorParameters(currToken, currPosition);
        constructorArguments = constructorParamsResult.result;
        assertTokenHereIs(constructorParamsResult.position, new LeftCurlyBracketToken());
        currPosition = constructorParamsResult.position + 1;
        

        // super(exp*);
        assertTokenHereIs(currPosition, new SuperToken());
        assertTokenHereIs(currPosition + 1, new LeftParenthesisToken());
        currPosition = currPosition + 2;
        currToken = getToken(currPosition);
        ParseResult<List<Exp>> superParamsResult = returnSuperParams(currToken, currPosition);
        assertTokenHereIs(superParamsResult.position, new SemicolonToken());
        superParams = superParamsResult.result;
        currPosition = superParamsResult.position + 1;

        // stmt*
        currToken = getToken(currPosition);
        ParseResult<List<Stmt>> constructorBodyResult = returnConstructorBody(currToken, currPosition);
        constructorBody = constructorBodyResult.result;
        currPosition = constructorBodyResult.position;

        // methoddef*
        ParseResult<List<MethodDef>> methodDefResult = parseClassMethods(currPosition);
        methods = methodDefResult.result;
        assertTokenHereIs(methodDefResult.position, new RightCurlyBracketToken());
        currPosition = methodDefResult.position + 1;

        return new ParseResult<ClassDef>(
            new ClassDef(className, extendsClassName, instanceVariables, 
                            constructorArguments, superParams, 
                                constructorBody, methods), currPosition);
    }

    // TODO: Check for duplicate variable initialization/declaration
    // TODO: Create class def for each class

    // program ::= classdef* stmt
    public ParseResult<Program> parseProgram(final int position) throws ParseException{
        final ParseResult<Stmt> stmt = parseStmt(position);
        final List<ClassDef> classes = new ArrayList<ClassDef>();
        int currPosition = position;
        boolean shouldRun = true;
        while (shouldRun) {
            try {
                final ParseResult<ClassDef> exp = parseClass(currPosition);
                classes.add(exp.result);
                currPosition = exp.position;
                // throw new ParseException("no classes");
            } catch (final ParseException e) {
                shouldRun = false;
            }
        }
        
        return new ParseResult<Program>(new Program(classes, stmt.result), stmt.position);
    }

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
}