package io.study.lang;

public interface IRandom {
    int nextInt();

    int nextInt(int maxValue);

    boolean nextBoolean();

    long nextLong();

    long nextLong(long maxValue);

    double nextDouble();

    double nextDouble(double maxValue);

    float nextFloat();

    float nextFloat(float maxValue);

    default double nextDouble(double start, double end) {
        return start + this.nextDouble(end - start);
    }

    default float nextFloat(float start, float end) {
        return start + this.nextFloat(end - start);
    }

    int nextInt(int start, int end);

    long nextLong(long start, long end);

    void nextBytes(byte[] bytes);
}