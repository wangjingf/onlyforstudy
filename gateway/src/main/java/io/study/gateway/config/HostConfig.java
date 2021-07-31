package io.study.gateway.config;

import java.net.Socket;
import java.net.SocketAddress;

public class HostConfig {
    SocketAddress address = null;
    Integer weight;
    public HostConfig(SocketAddress address){
        this.address = address;
    }
    public SocketAddress getAddress() {
        return address;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
