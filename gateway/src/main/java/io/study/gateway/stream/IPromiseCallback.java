package io.study.gateway.stream;

public interface IPromiseCallback<T> {
    void onSuccess(T result);
    void onFailure(Throwable cause);
    void onCanceled();
}
