package io.study.gateway.balance.impl;

import io.study.gateway.balance.AbstractLoadBalance;
import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.LoadBalanceContext;
import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * // TODO: 2021/7/31  wjf 在leastActiveLoadBalance里持有相关状态是否存在问题呢？如果有，问题是什么呢？
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance {
    Map<String/* domain+path */, Map<String/* host url */,Integer/*cnt*/>> activeCnt = new ConcurrentHashMap<>();
    @Override
    public ProxyInvoker doSelect(List<ProxyInvoker> invokers, LoadBalanceContext context) {
        assert context != null;
        int leastCnt = -1;

        ProxyInvoker selectedInvoker = null;
        Map<String, Integer> map = activeCnt.computeIfAbsent(context.getDomain() + context.getPath(), vs -> {
            return new ConcurrentHashMap<>();
        });
        for (int i = 0; i < invokers.size(); i++) {
            ProxyInvoker invoker = invokers.get(i);

            Integer active = map.computeIfAbsent(invoker.getUrl(),vs->{
                return 0;
            });
            if(active < leastCnt  || leastCnt == -1){
                leastCnt =active;
                selectedInvoker = invoker;
            }
        }
        // 这样设计可能也不合理
        synchronized (selectedInvoker.getUrl()){
            map.put(selectedInvoker.getUrl(), leastCnt+1);
        }
        return selectedInvoker;
    }


}
