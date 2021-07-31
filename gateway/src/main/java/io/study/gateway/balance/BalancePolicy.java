package io.study.gateway.balance;

public enum BalancePolicy {
    Random("Random"),LeastActive("LeastActive"),RoundRobin("RoundRobin"),ConsistHash("ConsistHash");
    String name;
    BalancePolicy(String name) {
        this.name = name;
    }


}
