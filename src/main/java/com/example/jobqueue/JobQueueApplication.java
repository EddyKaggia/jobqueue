package com.example.jobqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobQueueApplication {
	public static void main(String[] args) {
		SpringApplication.run(JobQueueApplication.class, args);

		JobQueue queue = new JobQueue();
		queue.startWorker();

		for (int i = 0; i < 5; i++) {
			queue.submit(new EmailJob("user" + i + "@example.com"));
		}
	}
}

