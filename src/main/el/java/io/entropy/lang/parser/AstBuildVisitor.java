package io.entropy.lang.parser;

import com.fr.third.org.hibernate.jpa.criteria.expression.LiteralExpression;
import compiler.expr.SyntaxException;
import io.entropy.lang.Comment;
import io.entropy.lang.ComputedPropertyExpressionAssignment;
import io.entropy.lang.SourceLocation;
import io.entropy.lang.ast.*;
import io.entropy.lang.ast.expression.*;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AstBuildVisitor implements ExprParserVisitor<ASTNode>{
    private final CommonTokenStream tokens;
    public AstBuildVisitor(CommonTokenStream tokens) {
        this.tokens = tokens;
    }
    private void newNode(ASTNode abstractNode, ParserRuleContext parserRuleContext) {
        List<Comment> comments = getComments(parserRuleContext);
        if (!comments.isEmpty()) {
            abstractNode.setComments(comments);
        }

        abstractNode.setSourceLocation(getSourceLocation(parserRuleContext));
    }
    private SourceLocation getSourceLocation(ParserRuleContext parserRuleContext) {
        return new SourceLocation(parserRuleContext.getStart().getLine(), parserRuleContext.getStart().getCharPositionInLine() + 1);
    }
    private List<Comment> getComments(ParserRuleContext ctx) {
        Token start = ctx.getStart();
        if (start != null) {
            int tokPos = start.getTokenIndex();
            List<Token> refChannel = tokens.getHiddenTokensToLeft(tokPos, 2);
            if (refChannel != null) {
                return getCommentOnChannel(refChannel);
            }
        }
        return Collections.emptyList();
    }
    private List<Comment> getCommentOnChannel(List<Token> refChannel) {
        List<Comment> comments = new ArrayList<>();
        for (Token refTok : refChannel) {
            String text = refTok.getText();
            // we strip the leading hash # character but we don't trim because we don't
            // know the "comment markup".  Maybe its space sensitive, maybe its not.  So
            // consumers can decide that
            if (text == null) {
                continue;
            }
            text = text.replaceFirst("^#", "");
            comments.add(new Comment(text, new SourceLocation(refTok.getLine(), refTok.getCharPositionInLine())));
        }
        return comments;
    }
    @Override
    public ASTNode visitProgram(ExprParser.ProgramContext ctx) {
        Program program = new Program();
        newNode(program,ctx);
        for (ExprParser.SourceElementContext sourceElementContext : ctx.sourceElements().sourceElement()) {
            program.getSources().add((Source) visit(sourceElementContext));
        }
        return program;
    }

    @Override
    public ASTNode visitSourceElement(ExprParser.SourceElementContext ctx) {
        Source source = new Source();
        newNode(source,ctx);
        source.setStatement((Statement) visitStatement(ctx.statement()));
        return source;
    }

    @Override
    public ASTNode visitStatement(ExprParser.StatementContext ctx) {
        if(ctx.block()!=null){
            return visitBlock(ctx.block());
        }else if(ctx.variableStatement()!=null){
            return visitVariableStatement(ctx.variableStatement());
        }else if(ctx.expressionStatement()!=null){
            return visitExpressionStatement(ctx.expressionStatement());
        }else if(ctx.labelledStatement()!=null){
            return visitLabelledStatement(ctx.labelledStatement());
        }else if(ctx.ifStatement()!=null){
            return visitIfStatement(ctx.ifStatement());
        }else if(ctx.iterationStatement()!=null){
            return visit(ctx.iterationStatement());
        }else if(ctx.continueStatement()!=null){
            return visitContinueStatement(ctx.continueStatement());
        }else if(ctx.breakStatement()!=null){
            return visitBreakStatement(ctx.breakStatement());
        }else if(ctx.returnStatement()!=null){
            return visitReturnStatement(ctx.returnStatement());
        }else if(ctx.switchStatement()!=null){
            return visitSwitchStatement(ctx.switchStatement());
        }else if(ctx.throwStatement()!=null){
            return visitThrowStatement(ctx.throwStatement());
        }else if(ctx.tryStatement()!=null){
            return visitTryStatement(ctx.tryStatement());
        }else if(ctx.functionDeclaration()!=null){
            return visitFunctionDeclaration(ctx.functionDeclaration());
        }
        return null;
    }

    @Override
    public ASTNode visitBlock(ExprParser.BlockContext ctx) {
        BlockStatement block = new BlockStatement();
        newNode(block,ctx);
        for (ExprParser.StatementContext statementContext : ctx.statementList().statement()) {
            block.getStatements().add((Statement) visitStatement(statementContext));
        }
        return block;
    }

    @Override
    public ASTNode visitStatementList(ExprParser.StatementListContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitVariableStatement(ExprParser.VariableStatementContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitVariableDeclarationList(ExprParser.VariableDeclarationListContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitVariableDeclaration(ExprParser.VariableDeclarationContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitEmptyStatement(ExprParser.EmptyStatementContext ctx) {
        EmptyStatement emptyStatement = new EmptyStatement();
        newNode(emptyStatement,ctx);
        return emptyStatement;
    }

    @Override
    public ASTNode visitExpressionStatement(ExprParser.ExpressionStatementContext ctx) {
        ExpressionStatement statement = new ExpressionStatement();
        for (ExprParser.SingleExpressionContext context : ctx.expressionSequence().singleExpression()) {
            statement.getExpressions().add((SingleExpression) visitSingleExpression(context));
        }
        newNode(statement,ctx);
        return statement;
    }
    public ASTNode visitSingleExpression(ExprParser.SingleExpressionContext ctx){
        if(ctx instanceof ExprParser.FunctionExpressionContext){
            return visitFunctionExpression((ExprParser.FunctionExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.MemberIndexExpressionContext){
            return visitMemberIndexExpression((ExprParser.MemberIndexExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.MemberDotExpressionContext){
            return visitMemberDotExpression((ExprParser.MemberDotExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.ArgumentsExpressionContext){
            return visitArgumentsExpression((ExprParser.ArgumentsExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.NewExpressionContext){
            return visitNewExpression((ExprParser.NewExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.PostIncrementExpressionContext){
           return unaryOpExpression(((ExprParser.PostIncrementExpressionContext) ctx).singleExpression(),((ExprParser.PostIncrementExpressionContext) ctx).PlusPlus().getText());
        }else if(ctx instanceof ExprParser.PostDecreaseExpressionContext){
            return unaryOpExpression(((ExprParser.PostDecreaseExpressionContext) ctx).singleExpression(),((ExprParser.PostDecreaseExpressionContext) ctx).MinusMinus().getText());
        }else if(ctx instanceof ExprParser.PreIncrementExpressionContext){
            return unaryOpExpression(((ExprParser.PreIncrementExpressionContext) ctx).singleExpression(),((ExprParser.PreIncrementExpressionContext) ctx).PlusPlus().getText());
        }else if(ctx instanceof ExprParser.PreDecreaseExpressionContext){
            return unaryOpExpression(((ExprParser.PreDecreaseExpressionContext) ctx).singleExpression(),((ExprParser.PreDecreaseExpressionContext) ctx).MinusMinus().getText());
        }else if(ctx instanceof ExprParser.UnaryPlusExpressionContext){
            return unaryOpExpression(((ExprParser.UnaryPlusExpressionContext) ctx).singleExpression(),((ExprParser.UnaryPlusExpressionContext) ctx).Plus().getText());
        }else if(ctx instanceof ExprParser.UnaryMinusExpressionContext){
            return unaryOpExpression(((ExprParser.UnaryMinusExpressionContext) ctx).singleExpression(),((ExprParser.UnaryMinusExpressionContext) ctx).Minus().getText());
        }else if(ctx instanceof ExprParser.BitNotExpressionContext){
            return unaryOpExpression(((ExprParser.BitNotExpressionContext) ctx).singleExpression(),((ExprParser.BitNotExpressionContext) ctx).BitNot().getText());
        }else if(ctx instanceof ExprParser.NotExpressionContext){
            return unaryOpExpression(((ExprParser.NotExpressionContext) ctx).singleExpression(),"!");
        }else if(ctx instanceof ExprParser.MultiplicativeExpressionContext){
            if(((ExprParser.MultiplicativeExpressionContext) ctx).Multiply()!=null)
               return binaryExpression(((ExprParser.MultiplicativeExpressionContext) ctx).singleExpression(),"*");
            else if(((ExprParser.MultiplicativeExpressionContext) ctx).Divide()!=null)
                return binaryExpression(((ExprParser.MultiplicativeExpressionContext) ctx).singleExpression(),"/");
            else if(((ExprParser.MultiplicativeExpressionContext) ctx).Modulus()!=null)
                return binaryExpression(((ExprParser.MultiplicativeExpressionContext) ctx).singleExpression(),"%");
        }else if(ctx instanceof ExprParser.AdditiveExpressionContext){
            if(((ExprParser.AdditiveExpressionContext) ctx).Plus()!=null)
                return binaryExpression(((ExprParser.AdditiveExpressionContext) ctx).singleExpression(),"+");
            else if(((ExprParser.AdditiveExpressionContext) ctx).Minus()!=null)
                return binaryExpression(((ExprParser.MultiplicativeExpressionContext) ctx).singleExpression(),"-");
        }else if(ctx instanceof ExprParser.BitShiftExpressionContext){
            if(((ExprParser.BitShiftExpressionContext) ctx).LeftShiftArithmetic()!=null)
                return binaryExpression(((ExprParser.BitShiftExpressionContext) ctx).singleExpression(),"<<");
            else if(((ExprParser.BitShiftExpressionContext) ctx).RightShiftArithmetic()!=null)
                return binaryExpression(((ExprParser.BitShiftExpressionContext) ctx).singleExpression(),">>");
            else if(((ExprParser.BitShiftExpressionContext) ctx).RightShiftLogical()!=null)
                return binaryExpression(((ExprParser.BitShiftExpressionContext) ctx).singleExpression(),">>>");
        }else if(ctx instanceof ExprParser.RelationalExpressionContext){
            if(((ExprParser.RelationalExpressionContext) ctx).LessThan()!=null)
                return binaryExpression(((ExprParser.RelationalExpressionContext) ctx).singleExpression(),"<");
            else if(((ExprParser.RelationalExpressionContext) ctx).MoreThan()!=null)
                return binaryExpression(((ExprParser.RelationalExpressionContext) ctx).singleExpression(),">");
            else if(((ExprParser.RelationalExpressionContext) ctx).GreaterThanEquals()!=null)
                return binaryExpression(((ExprParser.RelationalExpressionContext) ctx).singleExpression(),">=");
            else
                return binaryExpression(((ExprParser.RelationalExpressionContext) ctx).singleExpression(),"<=");
        }else if(ctx instanceof ExprParser.EqualityExpressionContext){
            if(((ExprParser.EqualityExpressionContext) ctx).Equals_()!=null)
                return binaryExpression(((ExprParser.EqualityExpressionContext) ctx).singleExpression(),"==");
            else if(((ExprParser.EqualityExpressionContext) ctx).NotEquals()!=null)
                return binaryExpression(((ExprParser.EqualityExpressionContext) ctx).singleExpression(),"!=");
        }else if(ctx instanceof ExprParser.BitAndExpressionContext){
            return binaryExpression(((ExprParser.BitAndExpressionContext) ctx).singleExpression(),"&");
        }else if(ctx instanceof ExprParser.BitXOrExpressionContext){
            return binaryExpression(((ExprParser.BitXOrExpressionContext) ctx).singleExpression(),"~");
        }else if(ctx instanceof ExprParser.BitAndExpressionContext){
            return binaryExpression(((ExprParser.BitAndExpressionContext) ctx).singleExpression(),"^");
        }else if(ctx instanceof ExprParser.BitOrExpressionContext){
            return binaryExpression(((ExprParser.BitOrExpressionContext) ctx).singleExpression(),"|");
        }else if(ctx instanceof ExprParser.LogicalAndExpressionContext){
            return binaryExpression(((ExprParser.LogicalAndExpressionContext) ctx).singleExpression(),"&&");
        }else if(ctx instanceof ExprParser.LogicalOrExpressionContext){
            return binaryExpression(((ExprParser.LogicalOrExpressionContext) ctx).singleExpression(),"||");
        } else if(ctx instanceof ExprParser.LiteralExpressionContext){
            return visitLiteralExpression((ExprParser.LiteralExpressionContext)ctx);
        }else if(ctx instanceof ExprParser.ArrayLiteralExpressionContext){
            return visitArrayLiteralExpression((ExprParser.ArrayLiteralExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.ObjectLiteralExpressionContext){
            return visitObjectLiteralExpression((ExprParser.ObjectLiteralExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.ParenthesizedExpressionContext){
            return visitParenthesizedExpression((ExprParser.ParenthesizedExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.TernaryExpressionContext){
            return visitTernaryExpression((ExprParser.TernaryExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.AssignmentExpressionContext){
            return visitAssignmentExpression((ExprParser.AssignmentExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.AssignmentOperatorExpressionContext){
            return visitAssignmentOperatorExpression((ExprParser.AssignmentOperatorExpressionContext) ctx);
        }else if(ctx instanceof ExprParser.IdentifierExpressionContext){
            return visitIdentifierExpression((ExprParser.IdentifierExpressionContext) ctx);
        }else{
            throw new SyntaxException("invalid expression:"+ctx.getText());
        }
        throw new SyntaxException("invalid expression:"+ctx.getText());
    }
    BinaryOpExpression binaryExpression(List<ExprParser.SingleExpressionContext> ctxs,String text){
        BinaryOpExpression expression = new BinaryOpExpression();
        newNode(expression,ctxs.get(0));
            expression.setLeft((SingleExpression) visitSingleExpression(ctxs.get(0)));
            expression.setRight((SingleExpression) visitSingleExpression(ctxs.get(1)));

        BinaryOp from = BinaryOp.from(text);
        if(from == null){
            throw new SyntaxException("invalid binary op :"+text);
        }
        return expression;
    }
    UnaryOpExpression unaryOpExpression(ExprParser.SingleExpressionContext expressionContext,String text){
        UnaryOp op = UnaryOp.from(text);
        if(op == null){
            throw new SyntaxException("invalid unary expression:"+text);
        }
        UnaryOpExpression expression = new UnaryOpExpression();
        expression.setExpression((SingleExpression) visitSingleExpression(expressionContext));
        newNode(expression,expressionContext);
        return expression;
    }
    @Override
    public ASTNode visitIfStatement(ExprParser.IfStatementContext ctx) {
        IfStatement ifStatement = new IfStatement();
        newNode(ifStatement,ctx);
        for (ExprParser.SingleExpressionContext singleExpressionContext : ctx.expressionSequence().singleExpression()) {
            ifStatement.getConditions().add((SingleExpression) visitSingleExpression(singleExpressionContext));
        }
        ifStatement.setBody((Statement) visitStatement(ctx.statement().get(0)));
        if(ctx.statement().size()>1){
            ifStatement.setElseStatement((Statement) visitStatement(ctx.statement().get(1)));
        }
        return ifStatement;
    }

    @Override
    public ASTNode visitDoStatement(ExprParser.DoStatementContext ctx) {
        DoStatement doStatement = new DoStatement();
        newNode(doStatement,ctx);
        for (ExprParser.SingleExpressionContext singleExpressionContext : ctx.expressionSequence().singleExpression()) {
            doStatement.getConditions().add((SingleExpression) visitSingleExpression(singleExpressionContext));
        }
        doStatement.setBody((Statement) visitStatement(ctx.statement()));
        return doStatement;
    }

    @Override
    public ASTNode visitWhileStatement(ExprParser.WhileStatementContext ctx) {
        WhileStatement whileStatement = new WhileStatement();
        newNode(whileStatement,ctx);
        for (ExprParser.SingleExpressionContext singleExpressionContext : ctx.expressionSequence().singleExpression()) {
            whileStatement.getConditions().add((SingleExpression) visitSingleExpression(singleExpressionContext));
        }
        whileStatement.setBody((Statement) visitStatement(ctx.statement()));
        return whileStatement;
    }

    @Override
    public ASTNode visitForStatement(ExprParser.ForStatementContext ctx) {
        ForStatement forStatement = new ForStatement();
        newNode(forStatement,ctx);
        if(ctx.variableDeclarationList()!=null){
            for (ExprParser.VariableDeclarationContext variableDeclarationContext : ctx.variableDeclarationList().variableDeclaration()) {
                forStatement.getDeclarations().add((VariableDeclaration) visitVariableDeclaration(variableDeclarationContext));
            }
        }
        if(ctx.forInitCondition()!=null){
            forStatement.setInitExprs(visitExprs(ctx.forInitCondition().expressionSequence().singleExpression()));
        }
        return forStatement;
    }

    List<SingleExpression> visitExprs(List<ExprParser.SingleExpressionContext> exprs){
        List<SingleExpression> ret = new ArrayList<>(exprs.size())    ;
        for (ExprParser.SingleExpressionContext expr : exprs) {
            ret.add((SingleExpression) visitSingleExpression(expr));
        }
        return ret;
    }
    @Override
    public ASTNode visitForOfStatement(ExprParser.ForOfStatementContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitForInitCondition(ExprParser.ForInitConditionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitForNextCondition(ExprParser.ForNextConditionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitVarModifier(ExprParser.VarModifierContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitContinueStatement(ExprParser.ContinueStatementContext ctx) {
        ContinueStatement continueStatement = new ContinueStatement();
        newNode(continueStatement,ctx);
        if(ctx.identifier()!=null){
            continueStatement.setIdentifier(ctx.identifier().getText());
        }
        return continueStatement;
    }

    @Override
    public ASTNode visitBreakStatement(ExprParser.BreakStatementContext ctx) {
        BreakStatement breakStatement = new BreakStatement();
        newNode(breakStatement,ctx);
        if(ctx.identifier()!=null){
            breakStatement.setIdentifier(ctx.identifier().getText());
        }
        return breakStatement;
    }

    @Override
    public ASTNode visitReturnStatement(ExprParser.ReturnStatementContext ctx) {
        ReturnStatement statement = new ReturnStatement();
        newNode(statement,ctx);
        if(ctx.expressionSequence()!=null){
            statement.setExpressions(visitExprs(ctx.expressionSequence().singleExpression()));
        }
        return statement;
    }

    @Override
    public ASTNode visitLabelledStatement(ExprParser.LabelledStatementContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitSwitchStatement(ExprParser.SwitchStatementContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitCaseBlock(ExprParser.CaseBlockContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitCaseClauses(ExprParser.CaseClausesContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitCaseClause(ExprParser.CaseClauseContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitDefaultClause(ExprParser.DefaultClauseContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitThrowStatement(ExprParser.ThrowStatementContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitTryStatement(ExprParser.TryStatementContext ctx) {
        TryStatement tryStatement = new TryStatement();
        newNode(tryStatement,ctx);
        tryStatement.setTryBlock((BlockStatement) visitBlock(ctx.block()));
        if(ctx.catchProduction()!=null){
            tryStatement.setCatchProduction((CatchProduction) visitCatchProduction(ctx.catchProduction()));
        }
        if(ctx.finallyProduction()!=null){
            tryStatement.setFinallyBlock((BlockStatement) visitFinallyProduction(ctx.finallyProduction()));
        }
        return tryStatement;
    }

    @Override
    public ASTNode visitCatchProduction(ExprParser.CatchProductionContext ctx) {
        CatchProduction production = new CatchProduction();
        newNode(production,ctx);
        if(ctx.assignable()!=null){
            production.setIdentifier(ctx.assignable().getText());
        }
        production.setCatchStatement((BlockStatement) visitBlock(ctx.block()));
        return production;
    }

    @Override
    public ASTNode visitFinallyProduction(ExprParser.FinallyProductionContext ctx) {
        throw new SyntaxException("invalid finally block");
    }

    @Override
    public ASTNode visitFunctionDeclaration(ExprParser.FunctionDeclarationContext ctx) {
        FunctionDeclaration declaration = new FunctionDeclaration();
        declaration.setIdentifier(ctx.identifier().getText());
        if(ctx.formalParameterList()!=null){
            for (ExprParser.FormalParameterArgContext formalParameterArgContext : ctx.formalParameterList().formalParameterArg()) {
                declaration.getParams().add(formalParameterArgContext.getText());
            }
        }
        if(ctx.functionBody()!=null && ctx.functionBody().sourceElements()!=null){
            for (ExprParser.SourceElementContext sourceElementContext : ctx.functionBody().sourceElements().sourceElement()) {
                declaration.getBody().add((Source) visitSourceElement(sourceElementContext));
            }
        }

        return declaration;
    }

    @Override
    public ASTNode visitFormalParameterList(ExprParser.FormalParameterListContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitFormalParameterArg(ExprParser.FormalParameterArgContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitFunctionBody(ExprParser.FunctionBodyContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitSourceElements(ExprParser.SourceElementsContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArrayLiteral(ExprParser.ArrayLiteralContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitElementList(ExprParser.ElementListContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArrayElement(ExprParser.ArrayElementContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitPropertyExpressionAssignment(ExprParser.PropertyExpressionAssignmentContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitComputedPropertyExpressionAssignment(ExprParser.ComputedPropertyExpressionAssignmentContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitPropertyName(ExprParser.PropertyNameContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArguments(ExprParser.ArgumentsContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArgument(ExprParser.ArgumentContext ctx) {
        Argument args = null;
        if(ctx.identifier()!=null){
            args = new IdentifierArgs();
            ((IdentifierArgs) args).setIdentifier(ctx.identifier().getText());
        }else if(ctx.singleExpression()!=null){
            args = new ExpressionArg();
            ((ExpressionArg) args).setExpr((SingleExpression) visitSingleExpression(ctx.singleExpression()));
        }
        newNode(args,ctx);
        return args;
    }

    @Override
    public ASTNode visitExpressionSequence(ExprParser.ExpressionSequenceContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitTernaryExpression(ExprParser.TernaryExpressionContext ctx) {
        TernaryExpression expression = new TernaryExpression();
        newNode(expression,ctx);
        expression.setCondition((SingleExpression) visitSingleExpression(ctx.singleExpression(0)));
        expression.setTrueExpr((SingleExpression) visitSingleExpression(ctx.singleExpression(1)));
        expression.setFalseExpr((SingleExpression) visitSingleExpression(ctx.singleExpression(2)));
        return expression;
    }

    @Override
    public ASTNode visitLogicalAndExpression(ExprParser.LogicalAndExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitPreIncrementExpression(ExprParser.PreIncrementExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitObjectLiteralExpression(ExprParser.ObjectLiteralExpressionContext ctx) {
        ObjectLiteral objectLiteral = new ObjectLiteral();
        newNode(objectLiteral,ctx);
        for (ExprParser.PropertyAssignmentContext assignmentContext
                : ctx.objectLiteral().propertyAssignment()) {
            if(assignmentContext instanceof ExprParser.PropertyExpressionAssignmentContext){
                PropertyExpressionAssignment assignment = new PropertyExpressionAssignment();
                assignment.setPropName(((ExprParser.PropertyExpressionAssignmentContext) assignmentContext).propertyName().getText());
                assignment.setValue((SingleExpression) visitSingleExpression(((ExprParser.PropertyExpressionAssignmentContext) assignmentContext).singleExpression()));
                objectLiteral.getElements().add(assignment);
            }else{
                ExprParser.ComputedPropertyExpressionAssignmentContext computedCtx = (ExprParser.ComputedPropertyExpressionAssignmentContext) assignmentContext;
                ComputedPropertyExpressionAssignment expressionAssignment =  new ComputedPropertyExpressionAssignment();
                expressionAssignment.setPropName((SingleExpression) visitSingleExpression(computedCtx.singleExpression(0)));
                expressionAssignment.setValue((SingleExpression) visitSingleExpression(computedCtx.singleExpression(1)));
            }
        }
        return objectLiteral;
    }


    @Override
    public ASTNode visitLogicalOrExpression(ExprParser.LogicalOrExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitNotExpression(ExprParser.NotExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitPreDecreaseExpression(ExprParser.PreDecreaseExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArgumentsExpression(ExprParser.ArgumentsExpressionContext ctx) {
        ArgumentsExpression argumentsExpression = new ArgumentsExpression();
        newNode(argumentsExpression,ctx);
        if(ctx.arguments()!=null){
            for (ExprParser.ArgumentContext argumentContext : ctx.arguments().argument()) {
                argumentsExpression.getArguments().add((Argument) visitArgument(argumentContext));
            }
        }
        if(ctx.singleExpression()!=null){
            argumentsExpression.setExpression((SingleExpression) visitSingleExpression(ctx.singleExpression()));
        }
        return argumentsExpression;
    }

    @Override
    public ASTNode visitFunctionExpression(ExprParser.FunctionExpressionContext ctx) {
        FunctionExpression expression = null;
        if(ctx.anoymousFunction() instanceof ExprParser.ArrowFunctionContext){
            expression=  new ArrowFunction();
            ExprParser.ArrowFunctionContext arrowCtx = (ExprParser.ArrowFunctionContext) ctx.anoymousFunction();
            if(arrowCtx.arrowFunctionBody().functionBody()!=null){
                for (ExprParser.SourceElementContext sourceElementContext : arrowCtx.arrowFunctionBody().functionBody().sourceElements().sourceElement()) {
                    expression.getBody().add((Source) visitSourceElement(sourceElementContext));
                }
            }else if(arrowCtx.arrowFunctionBody().singleExpression()!=null){
                ((ArrowFunction) expression).setReturnExpr((SingleExpression) visitSingleExpression(arrowCtx.arrowFunctionBody().singleExpression()));
            }
            if(arrowCtx.arrowFunctionParameters().identifier()!=null){
                expression.getParams().add(arrowCtx.arrowFunctionParameters().identifier().getText());
            }else if(arrowCtx.arrowFunctionParameters().formalParameterList()!=null){
                for (ExprParser.FormalParameterArgContext paramCtx : arrowCtx.arrowFunctionParameters().formalParameterList().formalParameterArg()) {
                    expression.getParams().add(paramCtx.getText());
                }
            }
        }else{
            ExprParser.FunctionDeclContext decContext = (ExprParser.FunctionDeclContext) ctx.anoymousFunction();
            expression = new FunctionDeclaration();
            ((FunctionDeclaration) expression).setIdentifier(decContext.functionDeclaration().identifier().getText());
            if(decContext.functionDeclaration().formalParameterList()!=null){
                for (ExprParser.FormalParameterArgContext argContext : decContext.functionDeclaration().formalParameterList().formalParameterArg()) {
                    expression.getParams().add(argContext.getText());
                }
            }
            if(decContext.functionDeclaration().functionBody().sourceElements()!=null){
                for (ExprParser.SourceElementContext sourceElementContext : decContext.functionDeclaration().functionBody().sourceElements().sourceElement()) {
                    expression.getBody().add((Source) visitSourceElement(sourceElementContext));
                }
            }
        }
        newNode(expression,ctx);
        return expression;
    }

    @Override
    public ASTNode visitUnaryMinusExpression(ExprParser.UnaryMinusExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitAssignmentExpression(ExprParser.AssignmentExpressionContext ctx) {
        AssignExpression assign = new AssignExpression();
        newNode(assign,ctx);
        assign.setLeft((SingleExpression) visitSingleExpression(ctx.singleExpression(0)));
        assign.setRight((SingleExpression) visitSingleExpression(ctx.singleExpression(1)));
        return assign;
    }

    @Override
    public ASTNode visitPostDecreaseExpression(ExprParser.PostDecreaseExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitUnaryPlusExpression(ExprParser.UnaryPlusExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitEqualityExpression(ExprParser.EqualityExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitBitXOrExpression(ExprParser.BitXOrExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitMultiplicativeExpression(ExprParser.MultiplicativeExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitBitShiftExpression(ExprParser.BitShiftExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitParenthesizedExpression(ExprParser.ParenthesizedExpressionContext ctx) {
        ParenthesizedExpression expression = new ParenthesizedExpression();
        newNode(expression,ctx);
        for (ExprParser.SingleExpressionContext singleExpressionContext : ctx.expressionSequence().singleExpression()) {
            expression.getExprs().add((SingleExpression) visitSingleExpression(singleExpressionContext));
        }
        return expression;
    }

    @Override
    public ASTNode visitAdditiveExpression(ExprParser.AdditiveExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitRelationalExpression(ExprParser.RelationalExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitPostIncrementExpression(ExprParser.PostIncrementExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitBitNotExpression(ExprParser.BitNotExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitNewExpression(ExprParser.NewExpressionContext ctx) {
        NewExpression expression = new NewExpression();
        newNode(expression,ctx);
        if(ctx.singleExpression()!=null){
            expression.setExpression((SingleExpression) visitSingleExpression(ctx.singleExpression()));
        }
        if(ctx.arguments()!=null){
            for (ExprParser.ArgumentContext argumentContext : ctx.arguments().argument()) {
                expression.getArguments().add((Argument) visitArgument(argumentContext));
            }
        }
        return expression;
    }

    @Override
    public ASTNode visitLiteralExpression(ExprParser.LiteralExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArrayLiteralExpression(ExprParser.ArrayLiteralExpressionContext ctx) {
        ArrayLiteral arrayLiteral = new ArrayLiteral();
        newNode(arrayLiteral,ctx);
        if(ctx.arrayLiteral().elementList()!=null){
            for (ExprParser.ArrayElementContext arrayElementContext : ctx.arrayLiteral().elementList().arrayElement()) {
                arrayLiteral.getArrayItems().add((SingleExpression) visitSingleExpression(arrayElementContext.singleExpression()));
            }
        }
        return arrayLiteral;
    }

    @Override
    public ASTNode visitMemberDotExpression(ExprParser.MemberDotExpressionContext ctx) {
        MemberDotExpression dotExpression = new MemberDotExpression();
        newNode(dotExpression,ctx);
        dotExpression.setParent((SingleExpression) visitSingleExpression(ctx.singleExpression()));
        if(ctx.Dot()==null){
            dotExpression.setAllowUndefine(false);
        }else{
            dotExpression.setAllowUndefine(true);
        }
        dotExpression.setPropName(ctx.identifierName().getText());
        return dotExpression;
    }

    @Override
    public ASTNode visitMemberIndexExpression(ExprParser.MemberIndexExpressionContext ctx) {
        MemberIndexExpression expression = new MemberIndexExpression();
        newNode(expression,ctx);
        expression.setParent((SingleExpression) visitSingleExpression(ctx.singleExpression()));
        if(ctx.expressionSequence().singleExpression().size()>1){
            throw new SyntaxException("member index expression only allow one prop");
        }
        expression.setProp((SingleExpression) visitSingleExpression(ctx.expressionSequence().singleExpression(0)));
        return expression;
    }

    @Override
    public ASTNode visitIdentifierExpression(ExprParser.IdentifierExpressionContext ctx) {
        IdentifierExpression expression = new IdentifierExpression();
        newNode(expression,ctx);
        expression.setIdentifier(ctx.identifier().getText());
        return expression;
    }

    @Override
    public ASTNode visitBitAndExpression(ExprParser.BitAndExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitBitOrExpression(ExprParser.BitOrExpressionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitAssignmentOperatorExpression(ExprParser.AssignmentOperatorExpressionContext ctx) {
        AssignOperatorExpression expression = new AssignOperatorExpression();
        newNode(expression,ctx);
        expression.setLeft((SingleExpression) visitSingleExpression(ctx.singleExpression(0)));
        expression.setRight((SingleExpression) visitSingleExpression(ctx.singleExpression(1)));
        expression.setAssignOp(AssignOperator.from(ctx.assignmentOperator().getText()));
        return expression;
    }

    @Override
    public ASTNode visitAssignable(ExprParser.AssignableContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitObjectLiteral(ExprParser.ObjectLiteralContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitFunctionDecl(ExprParser.FunctionDeclContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArrowFunction(ExprParser.ArrowFunctionContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArrowFunctionParameters(ExprParser.ArrowFunctionParametersContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitArrowFunctionBody(ExprParser.ArrowFunctionBodyContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitAssignmentOperator(ExprParser.AssignmentOperatorContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitLiteral(ExprParser.LiteralContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitNumericLiteral(ExprParser.NumericLiteralContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitIdentifierName(ExprParser.IdentifierNameContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitIdentifier(ExprParser.IdentifierContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitReservedWord(ExprParser.ReservedWordContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitKeyword(ExprParser.KeywordContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitEos(ExprParser.EosContext ctx) {
        return null;
    }

    @Override
    public ASTNode visit(ParseTree parseTree) {
        return null;
    }

    @Override
    public ASTNode visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public ASTNode visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public ASTNode visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
