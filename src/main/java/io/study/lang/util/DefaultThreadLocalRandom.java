package io.study.lang.util;

import io.study.lang.IRandom;

import java.util.concurrent.ThreadLocalRandom;

public class DefaultThreadLocalRandom implements IRandom {
    public static DefaultThreadLocalRandom INSTANCE = new DefaultThreadLocalRandom();

    public DefaultThreadLocalRandom() {
    }

    public int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public int nextInt(int var1) {
        return ThreadLocalRandom.current().nextInt(var1);
    }

    public long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public long nextLong(long var1) {
        return ThreadLocalRandom.current().nextLong(var1);
    }

    public boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public double nextDouble(double var1) {
        return ThreadLocalRandom.current().nextDouble(var1);
    }

    public float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public float nextFloat(float var1) {
        return (float)ThreadLocalRandom.current().nextDouble((double)var1);
    }

    public int nextInt(int var1, int var2) {
        return ThreadLocalRandom.current().nextInt(var1, var2);
    }

    public long nextLong(long var1, long var3) {
        return ThreadLocalRandom.current().nextLong(var1, var3);
    }

    public void nextBytes(byte[] var1) {
        ThreadLocalRandom.current().nextBytes(var1);
    }
}