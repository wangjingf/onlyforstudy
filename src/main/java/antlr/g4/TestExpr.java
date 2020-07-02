package antlr.g4;


import antlr.g4.expr.ExprLexer;
import antlr.g4.expr.ExprListener;
import antlr.g4.expr.ExprParser;
import junit.framework.TestCase;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TestExpr extends TestCase {
    public static void main(String[] args){

    }
    public void testExpr(){
        //CharStreams.fromString("hello world");
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("1+2*3/1+1"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree =  parser.expr();
        for (ParseTree child : ((ExprParser.ExprContext) tree).children) {
            System.out.println(child.getText());
        }

    }
    public void testExpr1(){
        //CharStreams.fromString("hello world");
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("a?b:c?d:e"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree =  parser.expr();
        for (ParseTree child : ((ExprParser.ExprContext) tree).children) {
            System.out.println(child.getText());
        }
    }
    public void testExpr3(){
        //CharStreams.fromString("hello world");
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("(1+2)*3"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree =  parser.expr();
        for (ParseTree child : ((ExprParser.ExprContext) tree).children) {
            System.out.println(child.getText());
        }

    }
    public void testExprMulti(){
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("-1-2/3"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree =  parser.expr();
        for (ParseTree child : ((ExprParser.ExprContext) tree).children) {
            System.out.println(child.getText());
        }
    }
    public void testStringExpr(){
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("ddd"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree =  parser.expr();
        for (ParseTree child : ((ExprParser.ExprContext) tree).children) {
            System.out.println(child.getText());
        }
    }
    // todo 如何完成解释器工作??
    public void testInterceptor(){
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("-1-2/3"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ExprParser.ExprContext context =  parser.expr();
        context.enterRule(new ExprListener() {
            @Override
            public void enterUnaryOp(ExprParser.UnaryOpContext ctx) {

            }

            @Override
            public void exitUnaryOp(ExprParser.UnaryOpContext ctx) {

            }

            @Override
            public void enterIdentifier(ExprParser.IdentifierContext ctx) {

            }

            @Override
            public void exitIdentifier(ExprParser.IdentifierContext ctx) {

            }

            @Override
            public void enterNegation(ExprParser.NegationContext ctx) {

            }

            @Override
            public void exitNegation(ExprParser.NegationContext ctx) {

            }

            @Override
            public void enterInt(ExprParser.IntContext ctx) {

            }

            @Override
            public void exitInt(ExprParser.IntContext ctx) {

            }

            @Override
            public void enterParen(ExprParser.ParenContext ctx) {

            }

            @Override
            public void exitParen(ExprParser.ParenContext ctx) {

            }

            @Override
            public void enterBinaryOp(ExprParser.BinaryOpContext ctx) {
               int left =  Integer.valueOf(ctx.expr(0).getText());
               int right  =Integer.valueOf(ctx.expr(1).getText());

            }

            @Override
            public void exitBinaryOp(ExprParser.BinaryOpContext ctx) {

            }

            @Override
            public void enterTernaryOp(ExprParser.TernaryOpContext ctx) {

            }

            @Override
            public void exitTernaryOp(ExprParser.TernaryOpContext ctx) {

            }

            @Override
            public void visitTerminal(TerminalNode node) {

            }

            @Override
            public void visitErrorNode(ErrorNode node) {

            }

            @Override
            public void enterEveryRule(ParserRuleContext ctx) {

            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {

            }
        });

    }
}
