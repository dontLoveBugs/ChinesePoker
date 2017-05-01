package com.wangxin.chinesepoker.Iterator;

import android.util.Log;

/**
 * Created by wangxin on 2017/4/10.
 */

public class MyIterator<E> implements Iterator<E> {
    private Aggregate<E> aggregate;

    private int currentIndex = 0;

    public MyIterator(Aggregate<E> aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public E first() {
        currentIndex = 0;
        Log.i("##info", "call MyIterator Fisrt Function！");
        if(hasNext()) {
            return aggregate.get(currentIndex);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean hasNext() {
        Log.i("##info", "call MyIterator hasNext Function！");
        return (currentIndex < aggregate.length());
    }

    @Override
    public E next() {
        Log.i("##info", "call MyIterator next Function！");
        currentIndex++;
        if(hasNext()) {
            return aggregate.get(currentIndex);
        }
        else
            return null;
    }

    @Override
    public E current() {
        return aggregate.get(currentIndex);
    }
}
