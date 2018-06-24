package com.zhuang.concurrency.example.singleton;

import com.zhuang.concurrency.annotations.Recommend;
import com.zhuang.concurrency.annotations.ThreadSafe;

/**
 * 枚举模式：最安全
 */
@ThreadSafe
@Recommend
public class SingletonExample7 {
    // 私有构造函数
    private SingletonExample7(){}

    public static SingletonExample7 getInstance(){
        return Singleton.INTANCE.getInstance();
    }

    private enum Singleton{
        INTANCE;
        private SingletonExample7 singleton;

        //JVM保证这个方法绝对值调用一次
        Singleton(){
            singleton = new SingletonExample7();
        }

        public SingletonExample7 getInstance() {
            return singleton;
        }
    }

}
