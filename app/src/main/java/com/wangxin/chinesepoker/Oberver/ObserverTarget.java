package com.wangxin.chinesepoker.Oberver;

import android.util.Log;

import com.wangxin.chinesepoker.Iterator.MyList;
import com.wangxin.chinesepoker.Player.Player;

/**
 * Created by wangxin on 2017/4/14.
 */

public abstract class ObserverTarget {
    protected MyList<Observer> observers = new MyList<Observer>();

    public ObserverTarget() {

    }

    // 注册观察者对象
    public void attach(Observer observer) {
        observers.add(observer);
    }

    // 删除观察者对象
    public void detach(Observer observer) {
        observers.remove(observer);
        Log.i("##info", "OT length:" + observers.length());
    }

    // 通知所有注册观察者对象
    protected abstract void notifyObservers(Player winner, int score);
 }


