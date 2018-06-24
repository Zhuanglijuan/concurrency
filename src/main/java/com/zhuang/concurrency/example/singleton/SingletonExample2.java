package com.zhuang.concurrency.example.singleton;


/**
 * 饿汉模式
 * 单例实例在类装载时进行创建
 */

import com.zhuang.concurrency.annotations.ThreadSafe;

@ThreadSafe
public class SingletonExample2 {
    //私有化构造器
    private SingletonExample2(){

    }
    //单例对象
    private static SingletonExample2 instance = new SingletonExample2();

    //静态工厂方法
    public static SingletonExample2 getInstance(){
        return instance;
    }
}
