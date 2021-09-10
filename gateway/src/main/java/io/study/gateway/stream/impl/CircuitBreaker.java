package io.study.gateway.stream.impl;

import com.google.common.util.concurrent.RateLimiter;
import io.study.gateway.stream.ICircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircuitBreaker<S,T> implements ICircuitBreaker<S,T> {//
    static final Logger logger = LoggerFactory.getLogger(CircuitBreaker.class);
    int breakerStatus = 0;
    long lastErrorTime = 0L;
    long lastRecoverTime = 0L;
    final int brokenTime;
    final RateLimiter rateLimiter;
    boolean forceBreak = false;
    final int circuitCount;
    int errorCount;
    static final int STATUS_OK = 0;
    static final  int STATUS_BREAK = 1;
    static final int STATUS_RECOVER = 2;
    public CircuitBreaker(RateLimiter limiter, int circuitCount, int brokenTime) {
        this.rateLimiter = limiter;
        this.circuitCount = circuitCount;
        this.brokenTime = brokenTime;
    }

    public String toString() {
        return "CircuitBreaker[status=" + this.breakerStatus + ",lastErrorTime=" + this.lastErrorTime + ",lastRecoverTime=" + this.lastRecoverTime + "]";
    }

    public synchronized int getBreakerStatus() {
        return this.breakerStatus;
    }

    
    public synchronized void onSuccess(Object result) {
        this.errorCount = 0;
        switch(this.breakerStatus) {
            case 2:
                this.breakerStatus = 0;
                logger.debug("breaker.recover_ok:breaker={}", this);
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
            long currentTime;
            switch(this.breakerStatus) {
                case STATUS_OK:
                    return false;
                case STATUS_BREAK:
                    currentTime = System.currentTimeMillis();
                    if (currentTime > this.lastErrorTime + (long)this.brokenTime) {
                        this.breakerStatus = STATUS_RECOVER;
                        logger.debug("breaker.try_recover_from_break:breaker={}", this);
                        this.lastRecoverTime = currentTime;
                        return false;
                    }

                    return true;
                case STATUS_RECOVER:
                    currentTime = System.currentTimeMillis();
                    if (currentTime > this.lastRecoverTime + (long)this.brokenTime) {
                        logger.debug("breaker.try_recover_from_break:breaker={}", this);
                        this.lastRecoverTime = currentTime;
                        return false;
                    }

                    return true;
                default:
                    return false;
            }
        }
    }


    public synchronized void onFailure(Throwable cause) {
        this.lastErrorTime = System.currentTimeMillis();
        ++this.errorCount;
        switch(this.breakerStatus) {
            case STATUS_OK:
                if (this.errorCount > this.circuitCount) {
                    this.breakerStatus = STATUS_BREAK;
                    logger.debug("breaker.repeat_error_count_exceed_limit:{},breaker={}", new Object[]{this.errorCount, this, cause});
                } else if (rateLimiter != null && !this.rateLimiter.tryAcquire()) {
                    this.breakerStatus = STATUS_BREAK;
                    logger.debug("breaker.error_rate_exceed_limit:{},breaker={}", new Object[]{this.rateLimiter.getRate(), this, cause});
                }
                break;
            case STATUS_BREAK:
            case STATUS_RECOVER:
                this.breakerStatus = STATUS_BREAK;
                logger.debug("breaker.extend_break_time:breaker={}", this);
        }

    }

    
    public void onCanceled() {

    }

    public synchronized void onCancelled() {
        switch(this.breakerStatus) {
            case STATUS_RECOVER:
                this.breakerStatus = 1;
            default:
        }
    }
}
