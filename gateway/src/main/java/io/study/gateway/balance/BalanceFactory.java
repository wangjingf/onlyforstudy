package io.study.gateway.balance;

import io.study.gateway.balance.impl.LeastActiveLoadBalance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceFactory {
    static Map<BalancePolicy,ILoadBalance> policies = new ConcurrentHashMap<>();
    static{
        policies.put(BalancePolicy.LeastActive,new LeastActiveLoadBalance());
    }
    public static ILoadBalance getLoadBalance(BalancePolicy policy){
        return policies.get(policy);
    }
}
