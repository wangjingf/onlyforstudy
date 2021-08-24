package io.study.gateway.interceptor;

public enum FilterType {
    INBOUND("in"),ENDPOINT("end"),OUTBOUND("out");
    String name;
     FilterType(String name){
        this.name = name;
    }
}
