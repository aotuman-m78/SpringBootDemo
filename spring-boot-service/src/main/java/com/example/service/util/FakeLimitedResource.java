package com.example.service.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fangrui on 2018/3/28.
 */
public class FakeLimitedResource {

    private AtomicBoolean resource = new AtomicBoolean();

    public void use() throws InterruptedException {

        if (! resource.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }

        try {
            Thread.sleep((long) (3 * Math.random()));
        } finally {
            resource.set(false);
        }
    }
}
