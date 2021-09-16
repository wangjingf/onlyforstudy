package io.study.exception;

import java.util.*;

public class StdException extends RuntimeException {
    private static final long serialVersionUID = 4130839118991243059L;
    long seq;
    int errorNo;
    Map<String, Object> params;
    Map<String, Object> attachments;
    boolean isWrapException;
    boolean isRemote;

    boolean traced;


    public StdException(String message) {
        super(message == null ? "app.err_fail" : message);
        this.errorNo = -1;
        this.params = new HashMap<>();
    }

    public StdException(String message, Throwable e) {
        super(message, e);

        this.params = new HashMap<>();
    }

    public StdException(String var1, boolean var2, boolean var3) {
        super(var1, (Throwable) null, var2, var3);

        this.params = new HashMap<>();
    }

    public boolean isTraced() {
        return this.traced;
    }

    public void setTraced(boolean traced) {
        this.traced = traced;
    }


    public boolean isWrapException() {
        return this.isWrapException;
    }


    public StdException remote(boolean isRemote) {
        this.isRemote = isRemote;
        return this;
    }

    public boolean isRemote() {
        return this.isRemote;
    }

    public String toString() {
        return this.getMessage();
    }


    public String getParamsString() {
        try {
            return this.params.toString();
        } catch (Throwable var2) {
            return "<" + var2.getClass().getName() + ">";
        }
    }

    public long getErrorSeq() {
        return this.seq;
    }

    public StdException seq(long seq) {
        this.seq = seq;
        return this;
    }

    public StdException errorNo(int errorNo) {
        this.errorNo = errorNo;
        return this;
    }

    public int getErrorNo() {
        return this.errorNo;
    }

    public StdException param(String var1, Object[] var2) {
        return this.param(var1, (Object) (var2 != null ? Arrays.asList(var2) : null));
    }

    public StdException args(Object[] var1) {
        return var1 != null && var1.length > 0 ? this.param("args", var1) : this;
    }

    public StdException param(String var1, char var2) {
        String var3;
        if (var2 == 0) {
            var3 = "\u0000";
        } else if (var2 == '\t') {
            var3 = "\t";
        } else if (var2 == '\n') {
            var3 = "\n";
        } else if (var2 == '\r') {
            var3 = "\r";
        } else {
            var3 = String.valueOf(var2);
        }

        return this.param(var1, (Object) var3);
    }

    public StdException param(String var1, Object var2) {
        if (this.params == null) {
            this.params = new HashMap();
        }

        this.params.put(var1, var2);
        return this;
    }

    protected void addParam(String name, Object value) {
        if (this.params == null) {
            this.params = new HashMap();
        }

        this.params.put(name, value);
    }


    public String getErrorCode() {
        return super.getMessage();
    }

    public Object getParam(String name) {
        return this.params.get(name);
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public StdException params(Map<String, Object> params) {
        if (params == null) {
            return this;
        } else {
            if (this.params == null) {
                this.params = new HashMap(params.size());
            }

            Iterator var2 = params.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry var3 = (Map.Entry) var2.next();
                this.param((String) var3.getKey(), var3.getValue());
            }

            return this;
        }
    }


    public Object attachment(String name) {
        return this.attachments == null ? null : this.attachments.get(name);
    }

    public StdException attachment(String name, Object value) {
        if (this.attachments == null) {
            this.attachments = new HashMap();
        }

        this.attachments.put(name, value);
        return this;
    }

    public static RuntimeException adapt(Throwable var0) {
        return (RuntimeException) (var0 instanceof RuntimeException ? (RuntimeException) var0 : new StdAdaptException(var0));
    }
    public static RuntimeException adapt(Throwable var0,String message) {
        return  adapt(var0);
    }

}
