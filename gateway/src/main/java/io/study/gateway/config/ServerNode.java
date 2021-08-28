package io.study.gateway.config;

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

}
