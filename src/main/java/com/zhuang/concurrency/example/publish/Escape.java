package com.zhuang.concurrency.example.publish;

import com.zhuang.concurrency.annotations.NotRecommend;
import com.zhuang.concurrency.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NotThreadSafe
@NotRecommend
/**
 * 在对象未完成之前不可以将其发布
 */
public class Escape {
    private int thisCanBeEscape = 0;
    public Escape(){
        new innnerClass();
    }

    private class innnerClass{
         public innnerClass(){
             log.info("{}",Escape.this.thisCanBeEscape);
         }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
