package com.zhuang.concurrency.example.lock;

import com.zhuang.concurrency.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 并发模拟
 */
@Slf4j
@ThreadSafe
public class LockExample2 {
    //请求总数
    public static int clientTotal = 5000;
    //同时并发执行的线程数
    public static int threadTotal = 200;

    public static int count = 0;

    private final static Lock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        for(int i = 0 ; i < clientTotal ; i ++){
            executorService.execute(() ->{
                try {
                    semaphore.acquire();
                    add();
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
        log.info("count :{}" ,count);
    }

    private static void add(){
        lock.lock();
        try{
            count++;
        }finally {
            lock.unlock();
        }
    }
}
