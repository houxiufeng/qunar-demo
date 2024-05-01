package com.example.demo.backoff;

public interface BackOffHandler {

    long STOP = -1;

    /**
     * Back off for a while, blocking.
     */
    long nextBackOff();

}
