package com.wangxin.chinesepoker.Memento;

/**
 * Created by wangxin on 2017/4/14.
 */

public class Memento {
    private LastState lastState;

    public Memento(LastState lastState) {
        this.lastState = lastState;
    }

    public LastState getLastState() {
        return lastState;
    }

    public void setLastState(LastState lastState) {
        this.lastState = lastState;
    }
}
