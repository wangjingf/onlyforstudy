package data_structure.acm.poj.leetcode;

import java.util.*;

/**
 * expr ->  expr expr | expr,expr | factor
 * factor -> {expr} | word
 * 消除二义性：
 * expr -> expr factor | expr ,factor | factor
 *  factor -> {expr} | word | null
 * 消除左递归: expr -> factor expr1 | factor expr2
 *             expr1 -> factor expr1
 *             expr2 -> ,factor expr2
 *             factor -> {expr} | word
 * 变成E-BNF  expr -> factor+ | factor(,factor)*
 *            factor -> {expr} | word | null
 * 加入运算优先级： or运算优先 < & 运算
 *                  如 {a,b},a{a,b} 是解析成 {a,b},a {a,b} 还是： {a,b} a{a,b}
 *                   expr -> term (,term)*
 *                  term -> factor+
 *                  factor -> {expr} |word
 * 等价于：  OrExpr -> AndExpr (,AndExpr)*
 *           AndExpr -> factor+
 *          factor-> {OrExpr} | word | null
 *  https://leetcode-cn.com/problems/brace-expansion-ii/
 */
public class ShellStrCode {

    int i = 0;



    Factor factor(String s){
        if(i>= s.length()) return null;
        char c = s.charAt(i);
        Factor factor = new Factor();
        if(Character.isLetter(c)){

            factor.e = strExpr(s);
            return factor;
        }else if(c == '{'){
            i++;
            factor.e = orExpr(s);
            i++;
        }else if(Character.isWhitespace(c)){
            i++;
        }
        if(factor.e==null){
            return null;
        }
        return factor;
    }

    private AndExpr andExpr(String s) {
        AndExpr expr = new AndExpr();
        expr.addExpr(factor(s));
        if(i >= s.length()){
            return expr;
        }
        Character c = s.charAt(i);
        while (Character.isLetter(c) || c == '{'){
            expr.addExpr(factor(s));
            if(i >= s.length()){
                return expr;
            }
            c = s.charAt(i);
        }
        return expr;
    }

    StrExpr strExpr(String s){
        StrExpr strExpr = new StrExpr();
        String ret = "";
        if(i >= s.length()){
            return null;
        }
        while (Character.isLetter(s.charAt(i))){
            ret+=s.charAt(i);
            i++;
            if(i >= s.length()){
                strExpr.e = ret;
                return strExpr;
            }
        }
        strExpr.e = ret;
        return strExpr;
    }
    public OrExpr orExpr(String s){

        OrExpr expr = new OrExpr();
        expr.addExpr(andExpr(s));
        if(i >= s.length()){
            return expr;
        }
        char c = s.charAt(i);
        while(c ==','){
            i++;//consume ,
            expr.addExpr(andExpr(s));
            if(i >= s.length()){
                return expr;
            }
            c = s.charAt(i);
        }
        return expr;
    }
    static class AndExpr implements Expr{
        List<Factor> factors = new ArrayList<>();
        public void addExpr(Factor expr){
            if(expr != null){
                factors.add(expr);
            }
        }
        @Override
        public String toString() {
            String str = "";
            for (Expr expr : factors) {
                str+= expr.toString();
            }
            return str;
        }

        @Override
        public Set<String> generate() {
            Set<String> ret = new LinkedHashSet<>();
            for (Factor expr : factors) {
                Set<String> generate = expr.generate();
                ret = mix(ret, generate);
            }
            return ret;
        }
    }

    public static Set mix(Set<String> list1,Set<String> list2){
        if(list1.isEmpty()){
            return list2;
        }else if(list2.isEmpty()){
            return list1;
        }
        Set<String> ret = new LinkedHashSet<>();
        for (String s : list1) {
            for (String s1 : list2) {
                ret.add(s+s1);
            }
        }
        return ret;
    }
    static class StrExpr implements Expr{
        String e;
        @Override
        public String toString() {
            if(e == null){
                return "";
            }
            return e;
        }

        @Override
        public Set<String> generate() {
            if(e == null){
                return Collections.emptySet();
            }
            return Collections.singleton(e);
        }
    }
    static class OrExpr implements Expr{
        List<AndExpr> andExprs = new ArrayList<>();
        public void addExpr(AndExpr factor){
            if(factor != null){
                andExprs.add(factor);
            }
        }
        public String toString(){
            String s = "";
            int j = 0;
            for (AndExpr expr : andExprs) {
                if(j < andExprs.size() -1){
                    s+=expr.toString()+",";

                }else{
                    s+=expr.toString();
                }
                j++;
            }
            return s;
        }
        @Override
        public Set<String> generate() {
            Set<String> set = new LinkedHashSet<>();
            for (AndExpr expr : andExprs) {
                Set<String> generate = expr.generate();
                set.addAll(generate);
            }
            return set;
        }
    }
    static class Factor implements Expr{
        Expr e;
        @Override
        public Set<String> generate() {
            if(e == null){
                return Collections.emptySet();
            }
            return e.generate();
        }
        public String toString(){
            if(e == null){
                return "";
            }else if( e instanceof StrExpr){
                return ((StrExpr) e).e;
            }else if(e instanceof OrExpr){
                return '{'+e.toString()+"}";
            }
            return "";
        }
    }
    static interface Expr{
         Set<String> generate();
    }
    public static void main(String[] args){
        ShellStrCode code = new ShellStrCode();
        OrExpr expr = code.orExpr("{a,b}c{d,e}f");

        System.out.println(expr.toString());
        Set<String> generate = expr.generate();
        List<String> list = new ArrayList<>();
        list.addAll(generate);
        Collections.sort(list);
        System.out.println(list);
    }
}
