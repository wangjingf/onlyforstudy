package io.study.gateway.registry;

import io.study.gateway.channel.ChannelManager;
import io.study.gateway.config.INode;

import java.util.List;

public class ZookeeperServerList implements IServerList{
    public List<INode> servers;

    public ZookeeperServerList(List<INode> servers) {
        this.servers = servers;
    }

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
        return servers;
    }

    @Override
    public List<INode> getActiveServers() {
        return servers;
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
    static class PingTask{
        public boolean isHealth(){
                return true;
        }
    }
}
