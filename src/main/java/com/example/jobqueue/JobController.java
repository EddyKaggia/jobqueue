package com.example.jobqueue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobQueue queue = new JobQueue();

    public JobController() {
        queue.startWorker();
    }

    @PostMapping("/email")
    public ResponseEntity<String> submitEmailJob(@RequestParam String recipient) {
        queue.submit(new EmailJob(recipient));
        return ResponseEntity.ok("EmailJob submitted for " + recipient);
    }
}

