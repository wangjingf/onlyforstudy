package io.entropy.lang.ast;

import java.util.List;

public abstract class FunctionExpression extends SingleExpression {
    List<String> params;
    List<Source> body;


    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public List<Source> getBody() {
        return body;
    }

    public void setBody(List<Source> body) {
        this.body = body;
    }
}
