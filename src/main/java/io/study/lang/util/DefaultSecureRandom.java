package io.study.lang.util;

import io.study.lang.IRandom;

import java.security.SecureRandom;

public class DefaultSecureRandom implements IRandom {
    final SecureRandom random;

    public DefaultSecureRandom() {
        this(new SecureRandom());
    }

    public DefaultSecureRandom(SecureRandom random) {
        this.random = random;
    }

    public void setSeed(long speed) {
        this.random.setSeed(speed);
    }

    public int nextInt() {
        return this.random.nextInt();
    }

    public int nextInt(int var1) {
        return this.random.nextInt(var1);
    }

    public boolean nextBoolean() {
        return this.random.nextBoolean();
    }

    public long nextLong() {
        return this.random.nextLong();
    }

    public long nextLong(long var1) {
        return this.nextLong(0L, var1);
    }

    public double nextDouble() {
        return this.random.nextDouble();
    }

    public double nextDouble(double var1) {
        return this.random.nextDouble() * var1;
    }

    public float nextFloat() {
        return this.random.nextFloat();
    }

    public float nextFloat(float max) {
        return this.random.nextFloat() * max;
    }

    public int nextInt(int start, int end) {
        return start == end ? end : start + this.random.nextInt(end - start);
    }

    public long nextLong(long start, long end) {
        long value = this.nextLong();
        if (start < end) {
            long var7 = end - start;
            long var9 = var7 - 1L;
            if ((var7 & var9) == 0L) {
                value = (value & var9) + start;
            } else if (var7 > 0L) {
                for(long var11 = value >>> 1; var11 + var9 - (value = var11 % var7) < 0L; var11 = this.nextLong() >>> 1) {
                    ;
                }

                value += start;
            } else {
                while(value < start || value >= end) {
                    value = this.nextLong();
                }
            }
        }

        return value;
    }

    public void nextBytes(byte[] bytes) {
        this.random.nextBytes(bytes);
    }
}