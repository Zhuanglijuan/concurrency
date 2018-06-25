package com.zhuang.concurrency.example.syncContainer;

import java.util.Iterator;
import java.util.Vector;

public class VectorExample3 {

    // java.util.ConcurrentModificationException
    //遍历过程中不要做更新操作,对其进行标记
    //遍历完之后再进行更新

    /**
     * final void checkForComodification() {
     * if (modCount != expectedModCount)
     * throw new ConcurrentModificationException();
     }
     */
    private static void test1(Vector<Integer> v1){// foreach
        for(Integer i : v1){
            if(i.equals(3))
                v1.remove(i);
        }
    }

    // java.util.ConcurrentModificationException
    private static void test2(Vector<Integer> v1){ // iterator
        Iterator<Integer> iterator = v1.iterator();
        while(iterator.hasNext()){
            Integer i = iterator.next();
            if(i.equals(3))
                v1.remove(i);
        }
    }

    private static void test3(Vector<Integer> v1){
        for(int i = 0 ; i < v1.size();i++){
            if(v1.get(i).equals(3)){
                v1.remove(v1.get(i));
            }
        }
    }

    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        test1(vector);
    }
}
