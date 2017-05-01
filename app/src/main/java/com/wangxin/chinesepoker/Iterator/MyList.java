package com.wangxin.chinesepoker.Iterator;

/**
 * Created by wangxin on 2017/4/10.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 存储牌的容器使用迭代器模式
 */
public class MyList<E> implements Aggregate<E> {
    public List<E> cards = new ArrayList<E>();

    @Override
    public Iterator<E> createIterator() {
        return new MyIterator<E>(this);
    }

    @Override
    public int length() {
        return cards.size();
    }

    @Override
    public E get(int index) {
        return (E)cards.get(index);
    }

    @Override
    public void add(E e) {
        cards.add(e);
    }

    public void remove(E e) {
        cards.remove(e);
    }
}
