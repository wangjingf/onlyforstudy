package io.study.gateway.config;

import io.study.gateway.stream.ICircuitBreaker;

import java.net.Socket;
import java.net.SocketAddress;

public interface INode {

    public SocketAddress getAddress();

     default int getWeight(){
         return 100;
     }

     ICircuitBreaker getBreaker();
}
