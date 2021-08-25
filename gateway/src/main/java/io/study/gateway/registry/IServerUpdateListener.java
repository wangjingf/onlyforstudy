package io.study.gateway.registry;

import io.study.gateway.invoker.INode;

public interface IServerUpdateListener {
    public void onServerAdd(INode server);
    public void onServerRemove(INode server);
}
