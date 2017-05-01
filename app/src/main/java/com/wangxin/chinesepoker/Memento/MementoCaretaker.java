package com.wangxin.chinesepoker.Memento;

import android.util.Log;

/**
 * Created by wangxin on 2017/4/14.
 */

public class MementoCaretaker {
    private Memento memento;

    // 备忘录取值方法
    public Memento retrieveMemento() {
        Log.i("##info", "MementoTaker retrieve Memento!");
        return this.memento;
    }

    // 备忘录保存方法
    public void saveMemento(Memento memento) {
        Log.i("##info:", "MementoTaker Save Memento!");
        this.memento = memento;
    }
}
