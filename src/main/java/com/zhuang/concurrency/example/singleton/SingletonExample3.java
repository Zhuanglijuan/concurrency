package com.zhuang.concurrency.example.singleton;


import com.zhuang.concurrency.annotations.NotRecommend;
import com.zhuang.concurrency.annotations.NotThreadSafe;

/**
 * 懒汉模式
 * 单例实例在第一次使用时进行创建
 */
@NotThreadSafe
@NotRecommend
public class SingletonExample3 {
    //私有化构造器
    private SingletonExample3(){

    }
    //单例对象
    private static SingletonExample3 instance = null;

    //静态工厂方法
    public static synchronized SingletonExample3 getInstance(){
        if(instance == null)
            instance = new SingletonExample3();
        return instance;
    }
}
