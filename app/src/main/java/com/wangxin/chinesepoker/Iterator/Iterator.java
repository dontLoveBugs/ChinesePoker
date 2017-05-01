package com.wangxin.chinesepoker.Iterator;

/**
 * Created by wangxin on 2017/4/10.
 */

public interface Iterator<E> {

    public E first();

    public boolean hasNext();

    public E next();

    public E current();
}
