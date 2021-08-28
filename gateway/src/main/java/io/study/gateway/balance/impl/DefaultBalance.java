package io.study.gateway.balance.impl;

import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.config.INode;

import java.util.List;

public class DefaultBalance implements ILoadBalance {

    @Override
    public INode select(List<INode> invokers) {
        return null;
    }
}
