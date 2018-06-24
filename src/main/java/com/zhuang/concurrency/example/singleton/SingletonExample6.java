package com.zhuang.concurrency.example.singleton;


/**
 * 饿汉模式
 * 单例实例在类装载时进行创建
 * 注意：静态代码块是按照顺序执行的
 */

import com.zhuang.concurrency.annotations.ThreadSafe;

@ThreadSafe
public class SingletonExample6 {
    //私有化构造器
    private SingletonExample6(){

    }

    //单例对象
    private static SingletonExample6 instance = null;

    static{
        instance = new SingletonExample6();
    }

    //静态工厂方法
    public static SingletonExample6 getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance());
        System.out.println(getInstance());
    }
}
