package antlr.graphql.schema.data;

import antlr.graphql.schema.TypeDefinitions;
import antlr.graphql.schema.entity.ObjectType;

import java.util.ArrayList;
import java.util.List;

public class FetcherContext {
    List<String> paths = new ArrayList<>();
    ObjectType queryType;
    TypeDefinitions typeDefinitions = null;
    public FetcherContext(TypeDefinitions typeDefinitions) {
        this.typeDefinitions = typeDefinitions;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
    public void addPath(String path){
        this.paths.add(path);
    }
    public void addPaths(List<String> paths){
        this.paths.addAll(paths);
    }
    public ObjectType getQueryType() {
        return queryType;
    }

    public void setQueryType(ObjectType queryType) {
        this.queryType = queryType;
    }

    public TypeDefinitions getTypeDefinitions() {
        return typeDefinitions;
    }

    public void setTypeDefinitions(TypeDefinitions typeDefinitions) {
        this.typeDefinitions = typeDefinitions;
    }
    public FetcherContext copy(){
        FetcherContext newContext = new FetcherContext(typeDefinitions);
        List<String> paths = new ArrayList<>();
        paths.addAll(getPaths());
        newContext.setPaths(paths);
        return newContext;
    }
}
