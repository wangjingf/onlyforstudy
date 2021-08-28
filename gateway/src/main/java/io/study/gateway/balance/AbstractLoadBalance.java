package io.study.gateway.balance;

import com.jd.vd.common.lang.Guard;
import io.study.gateway.config.INode;
import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;

public  abstract  class AbstractLoadBalance implements ILoadBalance {
    @Override
    public INode select(List<INode> invokers) {

        Guard.assertTrue(invokers.size() > 0,"balance.err_invokers_size_is_not_allow_empty");
        if(invokers.size() > 1){
            return doSelect(invokers);
        }
        return invokers.get(0);
    }
    public abstract INode doSelect(List<INode> invokers);
}
