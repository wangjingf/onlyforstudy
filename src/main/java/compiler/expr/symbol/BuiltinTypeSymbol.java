package compiler.expr.symbol;

/**
 * 内置数据类型
 */
public class BuiltinTypeSymbol extends Symbol {
    public BuiltinTypeSymbol(String name) {
        super(name);
    }
    public static BuiltinTypeSymbol INTEGER = new BuiltinTypeSymbol("INTEGER");
    public static BuiltinTypeSymbol REAL = new BuiltinTypeSymbol("REAL");
}
