package io.study.exception;

public class StdAdaptException  extends StdException {
    private static final long serialVersionUID = -8877286400206485097L;

    StdAdaptException(Throwable e) {
        super("app.err_fail", e);
    }

    public boolean isWrapException() {
        return true;
    }
}
