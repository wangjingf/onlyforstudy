package study.conductor;

public class AttemptResult<V> {
    int retryCount;
    V result;
    Exception exception;
    long millTimes;
    public AttemptResult(int retryCount, V result, Exception exception, long millTimes) {
        this.retryCount = retryCount;
        this.result = result;
        this.exception = exception;
        this.millTimes = millTimes;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public V getResult() {
        return result;
    }

    public void setResult(V result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
