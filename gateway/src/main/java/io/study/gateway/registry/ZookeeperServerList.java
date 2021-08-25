package io.study.gateway.registry;

import io.study.gateway.channel.ChannelManager;
import io.study.gateway.invoker.INode;

import java.util.List;

public class ZookeeperServerList implements IServerList{
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setChannelManager(ChannelManager channelManager) {

    }

    @Override
    public List<INode> getServers() {
        return null;
    }

    @Override
    public List<INode> getActiveServers() {
        return null;
    }

    @Override
    public void removeServer(INode server) {

    }

    @Override
    public void addServer(INode server) {

    }

    @Override
    public void addEventListener(IServerUpdateListener listener) {

    }
}
