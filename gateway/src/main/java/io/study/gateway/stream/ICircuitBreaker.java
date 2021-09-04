package io.study.gateway.stream;

public interface ICircuitBreaker<S,T> extends IPromiseCallback<T>{
    int STATUS_OK = 0;
    int STATUS_BREAK = 1;
    int STATUS_RECOVER = 0;

    int getBreakerStatus();
    boolean shouldBreak(S val);
    void onSuccess(T val);
    void onFailure(Throwable cause);
    void onCancelled();
}
