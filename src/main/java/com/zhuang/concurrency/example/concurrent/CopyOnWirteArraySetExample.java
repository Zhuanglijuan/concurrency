package com.zhuang.concurrency.example.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class CopyOnWirteArraySetExample {
    //请求总数
    public static int clientTotal = 400;
    //同时并发执行的线程数
    public static int threadTotal = 200;

    private static Set<Integer> set = new CopyOnWriteArraySet<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for(int i = 0 ; i < clientTotal ; i ++){
            final int count = i;
            executorService.execute(() ->{
                try {
                    semaphore.acquire();
                    update(count);
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception",e);
                }
            });
            countDownLatch.countDown();
        }

        //保证countDown减为0，所有进程都执行完
        countDownLatch.await();
        //关闭线程池
        executorService.shutdown();
        log.info("size:{}", set.size());
    }


    private static void update(int i){
        set.add(i);
    }
}
