package io.study.gateway.config;

import io.study.gateway.stream.ICircuitBreaker;

import java.net.SocketAddress;

public class ServerNode implements INode {
    SocketAddress address = null;
    public ServerNode(SocketAddress address){
        this.address = address;
    }
    @Override
    public SocketAddress getAddress() {
        return address;
    }

    @Override
    public ICircuitBreaker getBreaker() {
        return null;
    }

}
