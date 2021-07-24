package io.study.gateway.proxy;

public enum ProxyProtocol {
    Http1_1("http1.1"),Rpc("rpc");
    String name;
    ProxyProtocol(String name) {
        this.name = name;
    }
}
