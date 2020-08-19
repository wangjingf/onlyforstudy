package io.entropy.lang.ast;

import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Program extends ASTNode {
    List<Source> sources = new ArrayList<>();

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @Override
    public String toString() {
        List<String> list = sources.stream().map(vs -> vs.toString()).collect(Collectors.toList());
        return StringHelper.join(list,",");
    }
}
