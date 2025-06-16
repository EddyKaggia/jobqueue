package com.example.jobqueue;

import java.util.concurrent.*;
import java.util.*;

public class JobQueue {
    private final BlockingQueue<JobWrapper> queue = new LinkedBlockingQueue<>();
    private final List<JobWrapper> retryQueue = new CopyOnWriteArrayList<>();

    public void submit(Job job) {
        queue.add(new JobWrapper(job, 5));
    }

    public void startWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    JobWrapper wrapper = queue.take();
                    if (!wrapper.shouldRunNow()) {
                        retryQueue.add(wrapper); // Delay retry
                        continue;
                    }

                    try {
                        boolean success = wrapper.job.run();
                        if (!success && wrapper.attempts < wrapper.maxAttempts) {
                            wrapper.scheduleNextRetry();
                            retryQueue.add(wrapper);
                        }
                    } catch (Exception e) {
                        System.out.println("Job failed: " + wrapper.job.getName() + ", attempt " + wrapper.attempts);
                        if (wrapper.attempts < wrapper.maxAttempts) {
                            wrapper.scheduleNextRetry();
                            retryQueue.add(wrapper);
                        } else {
                            System.out.println("Giving up on " + wrapper.job.getName());
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }

                // Reschedule retries
                Iterator<JobWrapper> it = retryQueue.iterator();
                while (it.hasNext()) {
                    JobWrapper retry = it.next();
                    if (retry.shouldRunNow()) {
                        queue.add(retry);
                        it.remove();
                    }
                }
            }
        });

        worker.setDaemon(true);
        worker.start();
    }
}

