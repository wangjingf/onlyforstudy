package compiler.expr;

import compiler.expr.ast.ASTNode;
import compiler.expr.ast.BinOp;
import compiler.expr.ast.Num;
import io.study.reflect.ReflectHelper;

public class Interpreter {
    public Object visit(ASTNode node){
        return ReflectHelper.invokeMethod(this,"visit"+node.getType(),node);
    }
    public Object visitBinOp(BinOp binOp){
        TokenType tokenType = binOp.getOp().getTokenType();
         Integer leftValue = (Integer) visit(binOp.getLeft());
        Integer rightValue = (Integer) visit(binOp.getRight());
        if(TokenType.PLUS.equals(tokenType)){
            return leftValue+rightValue;
        }else if(TokenType.SUB.equals(tokenType)){
            return leftValue-rightValue;
        }else if(TokenType.MUL.equals(tokenType)){
            return leftValue*rightValue;
        }else if(TokenType.DIV.equals(tokenType)){
            return leftValue/rightValue;
        }
        return null;
    }
    public Object visitNum(Num num){
        return num.getValue();
    }
    public static void main(String[] args){
        ExprParser parser = new ExprParser("1+2*3-1");
        ASTNode node = parser.expr();
        Interpreter interpreter = new Interpreter();
        Object result =  interpreter.visit(node);
        System.out.println(result);
    }
}
