package io.study.gateway.config;

import io.study.gateway.client.ConnectionPoolConfig;

import java.net.InetSocketAddress;

public class GatewaySetting {
    String ipAddress;
    int port = 9988;
    int sslPort = 9999;

    int clientIdleTimeout = 60000;
    /**
     * 服务的连接超时事件：单位是毫秒
     */
    int serverIdleTimeout = 60000;

    int connectTimeout = 30000;

    int ioRatio = 100;

    long readThrottleBytesPerSecond;

    long writeThrottleBytesPerSecond;

    boolean authenticateSslClients;

    private int clientToProxyAcceptorThreads = 1;
    private int clientToProxyWorkerThreads = 16;
    private int proxyToServerWorkerThreads = 16;

    String localAddrForClient;

    int localPortForClient;
    int actionExecutorPoolSize = 2;

    int httpMaxInitialLineLength = 4096;
    int httpMaxHeaderSize = 8192;
    int httpMaxChunkSize = 8192;

    ConnectionPoolConfig poolConfig = null;


    public int getIoRatio() {
        return this.ioRatio;
    }

    public void setIoRatio(int ioRatio) {
        this.ioRatio = ioRatio;
    }


    public int getHttpMaxInitialLineLength() {
        return this.httpMaxInitialLineLength;
    }

    public void setHttpMaxInitialLineLength(int httpMaxInitialLineLength) {
        this.httpMaxInitialLineLength = httpMaxInitialLineLength;
    }


    public int getHttpMaxHeaderSize() {
        return this.httpMaxHeaderSize;
    }

    public void setHttpMaxHeaderSize(int httpMaxHeaderSize) {
        this.httpMaxHeaderSize = httpMaxHeaderSize;
    }


    public int getHttpMaxChunkSize() {
        return this.httpMaxChunkSize;
    }

    public void setHttpMaxChunkSize(int httpMaxChunkSize) {
        this.httpMaxChunkSize = httpMaxChunkSize;
    }

    public int getActionExecutorPoolSize() {
        return this.actionExecutorPoolSize;
    }

    public void setActionExecutorPoolSize(int actionExecutorPoolSize) {
        this.actionExecutorPoolSize = actionExecutorPoolSize;
    }

    public String getIpAddressForConnect() {
        return this.localAddrForClient;
    }

    public int getPortForConnect() {
        return this.localPortForClient;
    }

    public void setIpAddressForConnect(String localAddr) {
        this.localAddrForClient = localAddr;
    }

    public void setPortForConnect(int localPort) {
        this.localPortForClient = localPort;
    }


    public InetSocketAddress buildSocketAddressForConnect() {
        if (this.localAddrForClient == null || this.localAddrForClient.length() <= 0)
        {
            return null;
        }
        return new InetSocketAddress(this.localAddrForClient, this.localPortForClient);
    }


    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getSslPort() {
        return this.sslPort;
    }

    public void setSslPort(int sslPort) {
        this.sslPort = sslPort;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getClientIdleTimeout() {
        return this.clientIdleTimeout;
    }

    public void setClientIdleTimeout(int idleConnectionTimeout) {
        this.clientIdleTimeout = idleConnectionTimeout;
    }

    public int getServerIdleTimeout() {
        return this.serverIdleTimeout;
    }

    public void setServerIdleTimeout(int serverIdleTimeout) {
        this.serverIdleTimeout = serverIdleTimeout;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getReadThrottleBytesPerSecond() {
        return this.readThrottleBytesPerSecond;
    }

    public void setReadThrottleBytesPerSecond(long readThrottleBytesPerSecond) {
        this.readThrottleBytesPerSecond = readThrottleBytesPerSecond;
    }

    public long getWriteThrottleBytesPerSecond() {
        return this.writeThrottleBytesPerSecond;
    }

    public void setWriteThrottleBytesPerSecond(long writeThrottleBytesPerSecond) {
        this.writeThrottleBytesPerSecond = writeThrottleBytesPerSecond;
    }

    public boolean isAuthenticateSslClients() {
        return this.authenticateSslClients;
    }

    public void setAuthenticateSslClients(boolean authenticateSslClients) {
        this.authenticateSslClients = authenticateSslClients;
    }

    public int getClientToProxyAcceptorThreads() {
        return this.clientToProxyAcceptorThreads;
    }

    public void setClientToProxyAcceptorThreads(int clientToProxyAcceptorThreads) {
        this.clientToProxyAcceptorThreads = clientToProxyAcceptorThreads;
    }

    public int getClientToProxyWorkerThreads() {
        return this.clientToProxyWorkerThreads;
    }

    public void setClientToProxyWorkerThreads(int clientToProxyWorkerThreads) {
        this.clientToProxyWorkerThreads = clientToProxyWorkerThreads;
    }

    public int getProxyToServerWorkerThreads() {
        return this.proxyToServerWorkerThreads;
    }

    public void setProxyToServerWorkerThreads(int proxyToServerWorkerThreads) {
        this.proxyToServerWorkerThreads = proxyToServerWorkerThreads;
    }

    public ConnectionPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(ConnectionPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }
}
