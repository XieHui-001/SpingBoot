package com.spingboot.demo.spingbootdemo.utils;


import java.util.concurrent.atomic.AtomicInteger;

public class RequestCounter {
    private AtomicInteger count = new AtomicInteger(1);
    private long lastAccessTime = System.currentTimeMillis();

    public void increment() {
        count.incrementAndGet();
        lastAccessTime = System.currentTimeMillis();
    }

    public int getRequestCount() {
        return count.get();
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public boolean isExpired(long expirationTime) {
        return System.currentTimeMillis() - lastAccessTime > expirationTime;
    }
}

