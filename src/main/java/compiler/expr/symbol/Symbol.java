package compiler.expr.symbol;

public class Symbol {
    String name;
    Object type;

    public Symbol(String name) {
        this.name = name;
     }

    public Symbol(String name, Object type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }
}
