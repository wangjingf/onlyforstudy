package study.conductor;

public class RetryException extends RuntimeException{
    int  number;
    AttemptResult result;
    public RetryException(int  number, AttemptResult result) {
        super("retry Exception");
        this.number = number;
        this.result = result;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public AttemptResult getResult() {
        return result;
    }

    public void setResult(AttemptResult result) {
        this.result = result;
    }
}
