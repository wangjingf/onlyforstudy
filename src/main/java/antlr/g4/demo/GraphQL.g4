//自己尝试写的，不完整
grammar GraphQL;

document: definition+ ;
definition :  operationDefinition | fragmentDefinition;
operationDefinition: selectionSet | operationType Name? variableDefinitions? directives? selectionSet;
operationType: 'query' | 'mutation'	|'subscription';
selectionSet :  '{'selection+'}';
selection : field | fragmentSpread| inlineFragment;
field: alias ? Name arguments ? directives?selectionSet?;
alias: Name':';
arguments : '('argument+')';
argument: Name':'value;
fragmentSpread: '...' fragmentName directives?;
inlineFragment: '...' typeCondition? directives? selectionSet;
fragmentDefinition: 'fragment' fragmentName typeCondition directives?selectionSet;
fragmentName: Name ;
typeCondition: 'on' namedType;
value: variable
| IntValue
| FloatValue
| StringValue
| BooleanValue
| NullValue
| enumValue
| listValue
| objectValue
| BooleanValue;
BooleanValue:'true'|'false';
NullValue: 'null';
enumValue: Name ;//true falsenull true 或者false校验可以放到语义阶段
listValue:'[' value*']';
objectValue:  '{'objectField* '}';
objectField: Name':'value;
variableDefinitions:'('variableDefinition+')';
variableDefinition:variable':'type defaultValue?;
variable: '$'Name;
defaultValue: '='value;
type: namedType|listType|nonNullType;
namedType:Name;
listType:'['type']';
nonNullType:namedType'!'|listType'!';
directives:directive+;
directive:'@'Name arguments?;
Name:[_A-Za-z][_0-9A-Za-z]*;
IntValue:IntegerPart;
IntegerPart:NegativeSign? '0'| NegativeSign?NonZeroDigit Digit*;
NegativeSign:'-';
FloatValue: IntegerPart FractionalPart |
            IntegerPart ExponentPart |
            IntegerPart FractionalPart ExponentPart;
FractionalPart: '.'Digit+;
ExponentPart : ExponentIndicator Sign? Digit+;
ExponentIndicator:'e'|'E';
Sign:'+'|'-';
StringValue: '"' (~(["\\\n\r\u2028\u2029])|EscapedChar)* '"';
fragment EscapedChar :   '\\' (["\\/bfnrt] | Unicode) ;
fragment Unicode : 'u' Hex Hex Hex Hex ;
fragment Hex : [0-9a-fA-F] ;


fragment Digit :[0-9];
fragment NonZeroDigit:[1-9];
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines