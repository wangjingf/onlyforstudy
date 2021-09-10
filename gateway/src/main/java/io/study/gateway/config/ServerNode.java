package io.study.gateway.config;

import io.study.gateway.stream.ICircuitBreaker;
import io.study.gateway.stream.impl.CircuitBreaker;

import java.net.SocketAddress;

public class ServerNode implements INode {
    CircuitBreaker circuitBreaker = null;
    SocketAddress address = null;
    public ServerNode(SocketAddress address){
        this.address = address;
        this.circuitBreaker = new CircuitBreaker(null,10,30*10000);
    }
    @Override
    public SocketAddress getAddress() {
        return address;
    }

    @Override
    public ICircuitBreaker getBreaker() {
        return this.circuitBreaker;
    }

}
