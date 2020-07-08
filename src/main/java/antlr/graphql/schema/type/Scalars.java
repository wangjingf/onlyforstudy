package antlr.graphql.schema.type;

public class Scalars {

     public static final GraphQLScalarType INT = new GraphQLScalarType("Int","int type",new IntCoercing());
    public static final GraphQLScalarType FLOAT = new GraphQLScalarType("Float","float",new FloatCoercing());
    public static final GraphQLScalarType ID = new GraphQLScalarType("ID","ID type",new StringCoercing());
    public static final GraphQLScalarType BOOLEAN = new GraphQLScalarType("Boolean","boolean type",new BooleanCoercing());
    public static final GraphQLScalarType STRING = new GraphQLScalarType("String","string type",new StringCoercing());
    public static final GraphQLScalarType BIG_INTEGER = new GraphQLScalarType("BigInteger","BigInteger type",new BigIntegerCoercing());
    public static final GraphQLScalarType BIG_DECIMAL = new GraphQLScalarType("BigDecimal","BigDecimal type",new BigDecimalCoercing());

    public static final GraphQLScalarType DATETIME = new GraphQLScalarType("Datetime","Datetime type",new DatetimeCoercing());
}
