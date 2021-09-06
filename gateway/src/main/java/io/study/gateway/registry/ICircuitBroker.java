package io.study.gateway.registry;

public interface ICircuitBroker<S,T> {
    int STATUS_OK = 0;
    int STATUS_BREAK = 1;
    int STATUS_RECOVER = 2;

    int getBreakerStatus();

    boolean shouldBreak(S context);

    void onSuccess(T result);

    void onFailure(Throwable cause);

}
