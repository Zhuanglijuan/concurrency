package com.zhuang.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.zhuang.concurrency.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@NotThreadSafe
public class ImmutableExample1 {
    private final static Integer a = 1;
    private final static String b = "2";
    private final static Map<Integer,Integer> map = Maps.newHashMap();

    static{
        map.put(1,2);
        map.put(3,4);
        map.put(5,6);
    }

    public static void main(String[] args) {
//       a = 2;
//       b = "3";
//       map = Maps.newHashMap() 不允许指向另外一个对象
        map.put(1,3);           //可以把值进行修改
        log.info("{}",map.get(1));
    }

    /**
     * 基本类型变量被final修饰，也是不能修改的
     * @param a
     */
    private void test(final int a){
//        a = 1;
    }
}
