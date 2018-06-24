package com.zhuang.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
/**
 * 3. 修饰静态方法：整个静态方法，作用于所有对象
 * 4. 修饰类：括号括起来的部分，作用于所有对象。
 * synchronized不属于方法声明的一部分
 */
public class SynchronizedExample2 {
    //修饰类
    public void test1(int j){
        synchronized (SynchronizedExample2.class){
            for(int i = 0; i < 10 ; i ++){
              log.info("test1 {}-{}",j,i);
            }
        }
    }

    //修饰一个方法
    public static synchronized void test2(int j){
        for(int i = 0; i < 10 ; i ++){
            log.info("test2 {}-{}",j,i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample2 example1 = new SynchronizedExample2();
        SynchronizedExample2 example2 = new SynchronizedExample2();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() ->{
            example1.test2(1);
        });

        executorService.execute(() ->{
            example2.test2(2);
        });

    }
}
