package io.study.gateway.registry;


import io.study.gateway.config.INode;

public interface IServerUpdateListener {
    public void onServerAdd(INode server);
    public void onServerRemove(INode server);
}
