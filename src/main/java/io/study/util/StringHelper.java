package io.study.util;

import io.study.exception.StdException;

import java.util.Map;

public class StringHelper {
    public static String capitalize(String s){
        if(s == null){
            return null;
        }
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }
     public static String firstPart(String var0, char var1) {
        if (var0 == null) {
            return null;
        } else {
            int var2 = var0.indexOf(var1);
            return var2 < 0 ? var0 : var0.substring(0, var2);
        }
    }

     public static String lastPart(String var0, char var1, boolean var2) {
        if (var0 == null) {
            return null;
        } else {
            int var3 = var0.lastIndexOf(var1);
            if (var3 < 0) {
                return var2 ? "" : var0;
            } else {
                return var0.substring(var3 + 1);
            }
        }
    }
    /***
     * 类似slf4j的log,将{}占位符变为参数
     * @param messagePattern a:{},b:{}
     * @param argArray
     * @return
     */
    public static String formatMsg(String messagePattern, Object... argArray) {
        if (messagePattern == null) {
            return null;
        } else if (argArray == null) {
            return messagePattern;
        } else {
            int i = 0;
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

            for (int L = 0; L < argArray.length; ++L) {
                int j = messagePattern.indexOf("{}", i);
                if (j == -1) {
                    if (i == 0) {
                        return messagePattern;
                    }
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return sbuf.toString();
                }

                sbuf.append(messagePattern, i, j);
                sbuf.append(argArray[L] == null ? null : argArray[L].toString());
                i = j + 2;
            }
            sbuf.append(messagePattern, i, messagePattern.length());
            return sbuf.toString();
        }
    }
    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }
    /**
     * 将{{name}} 类型的变量替换掉
     * @param template
     * @param args
     */
    public static String format(String template,Map<String,Object> args){
        if(template == null){
            return template;
        }
        StringBuilder sb = new StringBuilder();
        boolean isMatchMode = false;
        StringBuilder variable = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            if(isMatchMode){
                if(template.charAt(i)!='}'){
                    variable.append(template.charAt(i));
                    continue;
                }else{ // i = '}'
                    if(i+1 < template.length() && template.charAt(i+1) == '}'){ //i+1 == '}'

                        String var = variable.toString().trim();
                        if(!isEmpty(var)){
                            Object value = args.get(var);
                            if(value == null){
                                throw new StdException("template.err_miss_var").param("var",var);
                            }else{
                                sb.append(value.toString());
                            }
                        }
                        variable = new StringBuilder();
                        i++;
                        isMatchMode = false;
                    }else{
                        sb.append("{{"+variable.toString());
                        variable = new StringBuilder();
                        isMatchMode = false;
                    }

                }

            }else{
                if(i+1<template.length() && template.charAt(i) == '{' && template.charAt(i+1) =='{'){
                    isMatchMode = true;
                    i++;
                }
                if(!isMatchMode){
                    sb.append(template.charAt(i));
                }
            }


        }
        String var = variable.toString();
        if(!StringHelper.isEmpty(var)){
            sb.append("{{"+var);
        }
        return sb.toString();
    }
}
