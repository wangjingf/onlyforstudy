package antlr.g4;

import antlr.graphql.ast.Document;
import io.study.lang.IVisitable;
import io.study.lang.IVisitor;

public class GraphqlAstVisitor implements IVisitor {
    @Override
    public boolean preVisit(IVisitable iVisitable) {
        return true;
    }

    @Override
    public void postVisit(IVisitable iVisitable) {

    }
    public boolean visitDocument(Document document){
        return true;
    }

}

