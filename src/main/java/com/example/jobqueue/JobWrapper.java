package com.example.jobqueue;

public class JobWrapper {
    public final Job job;
    public int attempts;
    public final int maxAttempts;
    public long nextRunTimeMillis;

    public JobWrapper(Job job, int maxAttempts) {
        this.job = job;
        this.maxAttempts = maxAttempts;
        this.attempts = 0;
        this.nextRunTimeMillis = System.currentTimeMillis();
    }

    public void scheduleNextRetry() {
        attempts++;
        long backoff = (long) Math.pow(2, attempts)  * 1000;
        nextRunTimeMillis = System.currentTimeMillis() + backoff;
    }

    public boolean shouldRunNow() {
        return System.currentTimeMillis() >= nextRunTimeMillis;
    }
}
