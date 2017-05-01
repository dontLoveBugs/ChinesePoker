package com.wangxin.chinesepoker.Iterator;

/**
 * Created by wangxin on 2017/4/10.
 */

public interface Aggregate<E> {

    public Iterator<E> createIterator();

    public int length();

    public E get(int index);

    public void add(E e);
}
