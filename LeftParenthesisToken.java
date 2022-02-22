public class LeftParenthesisToken implements Token {
    public boolean equals(final Object other){
        return other instanceof LeftParenthesisToken;
    }
    public int hashCode(){
        return 2;
    }
    public String toString(){
        return "(";
    }
}
