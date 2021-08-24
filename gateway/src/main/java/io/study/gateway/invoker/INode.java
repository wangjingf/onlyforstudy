package io.study.gateway.invoker;

import java.net.SocketAddress;

public interface INode {
    /**
     * 获取目的地址
     * @return
     */
    public String getUrl();

    public SocketAddress getAddress();
}
