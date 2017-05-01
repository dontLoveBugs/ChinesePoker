package com.wangxin.chinesepoker.State;

import com.wangxin.chinesepoker.Desk;

/**
 * Created by wangxin on 2017/4/9.
 */

public class ScoreDoubleState implements ScoreState {

    @Override
    public void multiple(Desk desk) {
        desk.multiple = desk.multiple * 2;
    }
}
