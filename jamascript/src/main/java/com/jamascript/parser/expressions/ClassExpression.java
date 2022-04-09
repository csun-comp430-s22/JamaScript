package com.jamascript.parser.expressions;
import com.jamascript.parser.classInformation.ClassName;
import com.jamascript.parser.operators.NewOp;
import com.jamascript.parser.operators.Op;
import com.jamascript.parser.ParseException;
import com.jamascript.parser.ParseResult;
import java.util.List;

public class ClassExpression implements Exp{
    public final Op op;
    public final ClassName cname;
    public final List<ParseResult<Exp>> params;

    public ClassExpression(final Op op, 
                           final ClassName cname,
                           final List<ParseResult<Exp>> params) throws ParseException {
        this.cname = cname;
        this.params = params;

        checkOperatorIsValid(op);
        this.op = op;
    }

    public void checkOperatorIsValid(Op op) throws ParseException {
        if(!(op instanceof NewOp)) {
            throw new ParseException("Invalid Operator for class: " + op.toString());
        }
        
    }

    public int hashCode() {
        return (cname.hashCode() +
                params.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassExpression) {
            final ClassExpression asFunc = (ClassExpression) other;
            return (cname.equals(asFunc.cname) &&
                    params.equals(asFunc.params));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassExpression(" + cname.toString() + ", " +
                params.toString() + ")");
    }
}