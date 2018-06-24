package com.zhuang.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
/**
 * 1. 修饰代码块：大括号括起来的代码，作用于调用的对象
 * 2. 修饰方法：整个方法，作用于调用的对象
 * synchronized不属于方法声明的一部分
 */
public class SynchronizedExample1 {
    //修饰一个代码块
    public void test1(int j){
        synchronized (this){
            for(int i = 0; i < 10 ; i ++){
              log.info("test1 {}-{}",j,i);
            }
        }
    }

    //修饰一个方法
    public synchronized void test2(int j){
        for(int i = 0; i < 10 ; i ++){
            log.info("test2 {}-{}",j,i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample1 example1 = new SynchronizedExample1();
        SynchronizedExample1 example2 = new SynchronizedExample1();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() ->{
            example1.test2(1);
        });

        executorService.execute(() ->{
            example2.test2(2);
        });

    }
}
