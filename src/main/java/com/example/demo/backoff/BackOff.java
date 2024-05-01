package com.example.demo.backoff;

public interface BackOff {

    BackOffHandler start();

    long getMaxElapsedTime();
}
