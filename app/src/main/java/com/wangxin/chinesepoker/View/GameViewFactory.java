package com.wangxin.chinesepoker.View;

import android.content.Context;

/**
 * Created by wangxin on 2017/4/7.
 */

public class GameViewFactory implements ViewFactory {
    @Override
    public View createView(Context context) {
        GameView view = new GameView(context);
        return view;
    }
}

