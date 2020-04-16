package compiler.expr.symbol;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {
    Map<String, Symbol> symbols = new LinkedCaseInsensitiveMap<>();
    SymbolTable parent;
    public SymbolTable(){
        symbols.put(BuiltinTypeSymbol.INTEGER.getName(),BuiltinTypeSymbol.INTEGER);
        symbols.put(BuiltinTypeSymbol.REAL.getName(),BuiltinTypeSymbol.REAL);
    }
    public void define(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol lookup(String name) {
        return lookup(name, false);
    }

    public Symbol lookup(String name, boolean currentOnly) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            return symbol;
        }
        if (currentOnly || parent == null) {
            return null;
        }
        return parent.lookup(name);
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }
}
