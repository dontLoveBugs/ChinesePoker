package com.wangxin.chinesepoker.View;

import android.content.Context;

/**
 * Created by wangxin on 2017/4/7.
 */

public class MenuViewFactory implements ViewFactory {
    @Override
    public View createView(Context context) {
        MenuView view = new MenuView(context);
        return view;
    }
}

