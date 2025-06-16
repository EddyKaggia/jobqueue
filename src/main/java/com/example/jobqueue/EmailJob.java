package com.example.jobqueue;

import static java.lang.Math.*;

public class EmailJob implements Job {
    private final String recipient;

    public EmailJob(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean run() throws Exception {
        if (Math.random() < 0.5) {
            throw new Exception("Random failure");
        }
        System.out.println("Email sent to " + recipient);
        return true;
    }

    @Override
    public String getName() {
        return "EmailJob-" + recipient;
    }

}
