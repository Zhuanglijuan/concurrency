package com.zhuang.concurrency.example.commonUnsafe;

import com.zhuang.concurrency.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@ThreadSafe
public class DateFormatExample3 {
    //请求总数
    public static int clientTotal = 5000;
    //同时并发执行的线程数
    public static int threadTotal = 200;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");

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
    }

    private static void update(int i){
        log.info("{}, {}",i,DateTime.parse("20180625",dateTimeFormatter).toDate());
    }
}
