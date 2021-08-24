package io.study.gateway.balance.impl;

import io.study.gateway.balance.AbstractLoadBalance;
import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.LoadBalanceContext;
import io.study.gateway.invoker.INode;
import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * // TODO: 2021/7/31  wjf 在leastActiveLoadBalance里持有相关状态是否存在问题呢？如果有，问题是什么呢？
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance {
    Map<INode/* host url */,Integer/*cnt*/> activeCnt = new ConcurrentHashMap<>();
    @Override
    public INode doSelect(List<INode> invokers) {
        int leastCnt = -1;

        INode selectedInvoker = null;

        for (int i = 0; i < invokers.size(); i++) {
            INode invoker = invokers.get(i);

            Integer active = activeCnt.computeIfAbsent(invoker,vs->{
                return 0;
            });
            if(active < leastCnt  || leastCnt == -1){
                leastCnt =active;
                selectedInvoker = invoker;
            }
        }
        // 这样设计可能也不合理
        synchronized (selectedInvoker.getUrl()){
            activeCnt.put(selectedInvoker, leastCnt+1);
        }
        return selectedInvoker;
    }


}
