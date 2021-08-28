package io.study.gateway.registry;

import io.study.gateway.channel.ChannelManager;
import io.study.gateway.config.INode;

import java.util.List;

/**
 * 服务器列表
 */
public interface  IServerList {
    public void start();
    public void stop();
    public void setChannelManager(ChannelManager channelManager);
    public List<INode> getServers();
    public List<INode> getActiveServers();
    public void removeServer(INode server);
    public void addServer(INode server);
    public void addEventListener(IServerUpdateListener listener);
}
