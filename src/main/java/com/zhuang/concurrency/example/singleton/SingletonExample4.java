package com.zhuang.concurrency.example.singleton;


import com.zhuang.concurrency.annotations.NotRecommend;
import com.zhuang.concurrency.annotations.NotThreadSafe;

/**
 * 懒汉模式 -> 双重同步锁单例模式
 * 单例实例在第一次使用时进行创建
 */
@NotThreadSafe
public class SingletonExample4 {
    //私有化构造器
    private SingletonExample4(){

    }
    //1. memory = allocate()分配对象的内存空间
    //2. ctorInstance()初始化对象
    //3. instance = memory 设置instance指向刚分配的内存

    //JVM和CPU优化，发生了指令重排

    //1. memory = allocate()分配对象的内存空间
    //3. instance = memory 设置instance指向刚分配的内存
    //2. ctorInstance()初始化对象

    /**
     * 所以存在当一个线程A执行到'instance = new SingletonExample4();'时，
     * 指令重排使3被先执行，此时线程B执行到if(instance == null)时，
     * 直接return instance;但此时指令2并未执行，发生错误，线程不安全
     */
    //单例对象
    private static SingletonExample4 instance = null;

    //静态工厂方法
    public static SingletonExample4 getInstance(){
        if(instance == null){// 双重检测机制
            synchronized (SingletonExample4.class){ // 同步锁
                if(instance == null)
                    instance = new SingletonExample4();
            }
        }
        return instance;
    }
}
