package io.entropy.lang.ast;

import io.entropy.lang.Comment;
import io.entropy.lang.SourceLocation;

import java.util.Collections;
import java.util.List;

public abstract class ASTNode implements ISourceLocationSupport {
    SourceLocation sourceLocation = null;
    private List<Comment> comments = Collections.emptyList();

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
