package io.study.gateway.client;

public class ConnectionPoolConfig {
    Integer connectTimeout;
    boolean tcpKeepAlive;
    boolean tcpNoDelay;
    Integer tcpSendBufferSize;
    Integer tcpReceiveBufferSize;
    Integer nettyWriteBufferHighWaterMark;
    Integer nettyWriteBufferLowWaterMark;
    boolean nettyAutoRead;
    int maxConnectionCount = 20;

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isTcpKeepAlive() {
        return tcpKeepAlive;
    }

    public void setTcpKeepAlive(boolean tcpKeepAlive) {
        this.tcpKeepAlive = tcpKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public Integer getTcpSendBufferSize() {
        return tcpSendBufferSize;
    }

    public void setTcpSendBufferSize(Integer tcpSendBufferSize) {
        this.tcpSendBufferSize = tcpSendBufferSize;
    }

    public Integer getTcpReceiveBufferSize() {
        return tcpReceiveBufferSize;
    }

    public void setTcpReceiveBufferSize(Integer tcpReceiveBufferSize) {
        this.tcpReceiveBufferSize = tcpReceiveBufferSize;
    }

    public Integer getNettyWriteBufferHighWaterMark() {
        return nettyWriteBufferHighWaterMark;
    }

    public void setNettyWriteBufferHighWaterMark(Integer nettyWriteBufferHighWaterMark) {
        this.nettyWriteBufferHighWaterMark = nettyWriteBufferHighWaterMark;
    }

    public Integer getNettyWriteBufferLowWaterMark() {
        return nettyWriteBufferLowWaterMark;
    }

    public void setNettyWriteBufferLowWaterMark(Integer nettyWriteBufferLowWaterMark) {
        this.nettyWriteBufferLowWaterMark = nettyWriteBufferLowWaterMark;
    }

    public boolean isNettyAutoRead() {
        return nettyAutoRead;
    }

    public void setNettyAutoRead(boolean nettyAutoRead) {
        this.nettyAutoRead = nettyAutoRead;
    }

    public int getMaxConnectionCount() {
        return maxConnectionCount;
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }
}
