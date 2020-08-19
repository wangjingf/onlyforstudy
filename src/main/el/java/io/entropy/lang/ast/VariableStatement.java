package io.entropy.lang.ast;

import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VariableStatement extends Statement {
    List<VariableDeclaration> declarations = new ArrayList<>();

    public List<VariableDeclaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<VariableDeclaration> declarations) {
        this.declarations = declarations;
    }
    @Override
    public String toString() {
        String template = "var {{template}};";
        Map<String,Object> vars  = new HashMap<>();
        List<String> list = declarations.stream().map(vs -> vs.toString()).collect(Collectors.toList());
        vars.put("template",StringHelper.join(list,","));
        return StringHelper.format(template,vars);
    }
}
