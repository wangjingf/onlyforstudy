package antlr.graphql.schema.entity.directive;

import antlr.graphql.schema.data.FetcherContext;
import antlr.graphql.schema.entity.FieldInfo;

import java.util.Map;

public class DirectiveContext {
    /**
     * 上下文变量
     */
    Map<String,Object> vars;
    Map<String,Object> values;
    FieldInfo fieldInfo = null;
    FetcherContext fetcherContext = null;

    public DirectiveContext(Map<String, Object> vars, Map<String, Object> values, FieldInfo fieldInfo) {
        this.vars = vars;
        this.values = values;
        this.fieldInfo = fieldInfo;
    }

    public Map<String, Object> getVars() {
        return vars;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public FetcherContext getFetcherContext() {
        return fetcherContext;
    }

    public void setFetcherContext(FetcherContext fetcherContext) {
        this.fetcherContext = fetcherContext;
    }
}
