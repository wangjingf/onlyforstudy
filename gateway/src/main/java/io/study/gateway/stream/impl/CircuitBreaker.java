package io.study.gateway.stream.impl;

import com.google.common.util.concurrent.RateLimiter;
import io.study.gateway.stream.ICircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircuitBreaker<S,T> {//implements ICircuitBreaker<T> 
    static final Logger h = LoggerFactory.getLogger(CircuitBreaker.class);
    int breakerStatus = 0;
    long a = 0L;
    long f = 0L;
    final int g;
    final RateLimiter rateLimiter;
    boolean forceBreak = false;
    final int d;
    int e;

    public CircuitBreaker(RateLimiter limiter, int var2, int var3) {
        this.rateLimiter = limiter;
        this.d = var2;
        this.g = var3;
    }

    public String toString() {
        return "CircuitBreaker[status=" + this.breakerStatus + ",lastErrorTime=" + this.a + ",lastRecoverTime=" + this.f + "]";
    }

    public synchronized int getBreakerStatus() {
        return this.breakerStatus;
    }

    
    public synchronized void onSuccess(Object val) {
        this.e = 0;
        switch(this.breakerStatus) {
            case 2:
                this.breakerStatus = 0;
                h.debug("breaker.recover_ok:breaker={}", this);
            default:
        }
    }



    public synchronized boolean isForceBreak() {
        return this.forceBreak;
    }

    public synchronized void setForceBreak(boolean forceBreak) {
        this.forceBreak = forceBreak;
    }
     public synchronized boolean shouldBreak(S var1) {
        if (this.forceBreak) {
            return true;
        } else {
            long var2;
            switch(this.breakerStatus) {
                case 0:
                    return false;
                case 1:
                    var2 = System.currentTimeMillis();
                    if (var2 > this.a + (long)this.g) {
                        this.breakerStatus = 2;
                        h.debug("breaker.try_recover_from_break:breaker={}", this);
                        this.f = var2;
                        return false;
                    }

                    return true;
                case 2:
                    var2 = System.currentTimeMillis();
                    if (var2 > this.f + (long)this.g) {
                        h.debug("breaker.try_recover_from_break:breaker={}", this);
                        this.f = var2;
                        return false;
                    }

                    return true;
                default:
                    return false;
            }
        }
    }


    public synchronized void onFailure(Throwable var1) {
        this.a = System.currentTimeMillis();
        ++this.e;
        switch(this.breakerStatus) {
            case 0:
                if (this.e > this.d) {
                    this.breakerStatus = 1;
                    h.debug("breaker.repeat_error_count_exceed_limit:{},breaker={}", new Object[]{this.e, this, var1});
                } else if (!this.rateLimiter.tryAcquire()) {
                    this.breakerStatus = 1;
                    h.debug("breaker.error_rate_exceed_limit:{},breaker={}", new Object[]{this.rateLimiter.getRate(), this, var1});
                }
                break;
            case 1:
            case 2:
                this.breakerStatus = 1;
                h.debug("breaker.extend_break_time:breaker={}", this);
        }

    }

    
    public void onCanceled() {

    }

    public synchronized void onCancelled() {
        switch(this.breakerStatus) {
            case 2:
                this.breakerStatus = 1;
            default:
        }
    }
}
