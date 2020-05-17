function getRule(type){

}


var Factor = [
    "none","assignment",
    "or",//
    "and",//and
    "eq",//==,!=
    "term",//+-
    "factor",//*/
    "unary",//!-
    "call",//.运算
    "primary"
]
var Procedence = [];
Factor.forEach((item,index)=>{
    Procedence[item] = index
});
var prefix,infix,procedure;
var ParseRules = [
    [grouping,null,Procedence.none],//left
    [grouping,null,Procedence.none],// (
    [unary,binary,Procedence.term],//-
    [null,binary,Procedence.term],//+
    [number,null,procedence.none]
]
function number(){
    return number;
}
function grouping(){
    expression();
    consume(")")
}
function unary(){
    var token = parser.previous.type;
    expression();
    switch (operatorType) {
        case TOKEN_MINUS: emitByte(OP_NEGATE); break;
        default:
            return; // Unreachable.
    }
}
function expression(){
    parseProcedence(Procedence.assignment);
}
function binary(){
    var operatorType = parser.previous.type;
    var rule = getRule(operatorType);
    parseProcedence(rule.procedence+1);
    switch (operatorType){

    }
}


function parseProcedence(procedence){
    var prefixRule = getRule(parser.previous.type).prefix;
    if(prefixRule == null){
        throw new Error("expect expression.")
        return;
    }
    prefixRule();
    while(procedure < getRule(parser.current.type).procedure){
        advance();
        var infixRule = getRule(parser.previous.type).infix
        infixRule();
    }
}