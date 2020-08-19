package io.entropy.lang.ast;

public class Source extends ASTNode {
    Statement statement = null;

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        return statement.toString();
    }
}
