package com.example.service.distribute;

import com.example.service.base.AbstractServiceTest;
import com.example.service.distribute.lock.impl.DistributeLock;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fangrui on 2018/2/10.
 */
public class DistributeLockTest extends AbstractServiceTest {

    private Logger logger = LoggerFactory.getLogger(DistributeLockTest.class);

    private static int num = 10;

    @Test
    public void testDistributeLock() throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(num);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    DistributeLock lock = new DistributeLock("testlock");

                    lock.lock();
                    try {
                        logger.info("thread : " + Thread.currentThread().getName() + " do something");
                        Thread.sleep(1000);
                        latch.countDown();
                    } finally {
                        lock.unlock();
                    }

                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }

            }
        };

        for (int i = 0; i < num; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }
        latch.await();
    }
}
