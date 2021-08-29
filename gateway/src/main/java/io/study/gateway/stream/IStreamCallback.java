package io.study.gateway.stream;

public interface IStreamCallback<T> extends IPromiseCallback<T> {
    /**
     * 刚收到消息时的回调
     * @param startMsg
     */
    public void onStart(Object startMsg);

    /**
     * 消息接收完毕后的回调
     * @param content
     */
    public void onNext(Object content);

    void onIdle();
}
