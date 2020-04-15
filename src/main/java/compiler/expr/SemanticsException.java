package compiler.expr;

public class SemanticsException extends RuntimeException {
    public SemanticsException() {

    }

    public SemanticsException(String message) {
        super(message);
    }

    public SemanticsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SemanticsException(Throwable cause) {
        super(cause);
    }

    public SemanticsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
