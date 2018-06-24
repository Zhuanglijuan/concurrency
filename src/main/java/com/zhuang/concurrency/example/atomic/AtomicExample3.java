package com.zhuang.concurrency.example.atomic;

import com.zhuang.concurrency.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;


/**
 * 并发模拟
 */
@Slf4j
@ThreadSafe
public class AtomicExample3 {
    //请求总数
    public static int clientTotal = 5000;
    //同时并发执行的线程数
    public static int threadTotal = 200;

    /**
     * 对于普通类型的long和double变量，JVM允许将64位的读操作或写操作拆成两个32位的操作
     * LongAdder的核心实现是将热点数据分离：
     * 比如,可以将AtomicInteger的内部核心数据value分离成一个数组,每个线程访问时,通过哈希等算法映射到其中一个数字进行计数,
     * 而最终的计数结果,则为这个数组的求和累加,其中,热点数据value被分离成多个单元cell,每个cell独自维护内部的值,
     * 当前对象的实际值由所有的cell累计合成,这样,热点就进行了有效的分离,提高了并行度,LongAdder正是使用了这种思想.
     * 热点进行了有效的分离并提高了并行度
     * LongAdder相当于是在AtomicLong基础上将单点的更新压力分散到各个节点上，
     * 在低并发的时候通过对base的直接更新可以很好的保障和AtomicLong的性能基本一致
     * 而在高并发的时候通过分散提高性能
     * 缺陷：在同步的时候如果有并发更新，可能会导致统计的数据有误差
     * 实际处理高并发的时候，可以优先使用LongAdder而不是继续使用AtomicLong
     * 若是在需要准确的数值如生成全局唯一的序列号，则AtomicLong是正确的选择
     */
    public static LongAdder count = new LongAdder();
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
        count.increment();
    }
}
