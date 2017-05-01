package com.wangxin.chinesepoker.View;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by wangxin on 2017/4/7.
 */

public abstract class View extends SurfaceView implements SurfaceHolder.Callback, android.view.View.OnTouchListener {

    protected Context context;
    protected SurfaceHolder holder;
    protected Canvas canvas;
    protected boolean threadFlag = true;
    protected Thread viewThread;


    public View(Context context) {
        super(context);
        this.context = context;
        createViewThread();
    }

    //判断触摸点(x, y)是否在（rectX, rectY)为左上点 长为rectW 宽为rectH
    public static boolean inRect(int x, int y, int rectX, int rectY, int rectW, int rectH) {
        if (x <= rectX || x >= rectX + rectW || y <= rectY || y >= rectY + rectH) {
            return false;
        }
        return true;
    }

    public void getSurfaceHolder() {
        holder = getHolder();
    }

    public abstract void createViewThread();
}


