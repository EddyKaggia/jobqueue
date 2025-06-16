package com.example.jobqueue;

public interface Job {
    boolean run() throws Exception;
    String getName();
}

