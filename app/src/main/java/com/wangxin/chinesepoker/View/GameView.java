package com.wangxin.chinesepoker.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.R;


/**
 * Created by wangxin on 2017/4/7.
 */

public class GameView extends View{

    Desk desk;
    Bitmap background;

    public GameView(Context context) {
        super(context);
        desk = Desk.getInstance(context);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_bg);
        this.getHolder().addCallback(this);
        this.setOnTouchListener(this);
        Log.i("##info:", "create Game View!");
    }

    @SuppressLint("DrawAlloction")
    @Override
    protected void onDraw(Canvas canvas) {

        Rect src = new Rect();
        Rect des = new Rect();

        src.set(0, 0, background.getWidth(), background.getHeight());
        des.set(0, 0, MainActivity.SCREEN_WIDTH, MainActivity.SCREEN_HEIGHT);

        canvas.drawBitmap(background, src, des, null);
        desk.controlPaint(canvas);
    }

    @Override
    public void createViewThread() {
        Log.i("##info:", "create game view thread!");
        viewThread = new Thread() {
            @SuppressLint("WrongCall")
            @Override
            public void run() {
                getSurfaceHolder();
                while (threadFlag) {

                    //TODO
                    desk.gameLogic();
                    //Log.i("##info:", "game logic is running??!");
                    try {
                        canvas = holder.lockCanvas();

                        onDraw(canvas);
                    }  finally {
                        holder.unlockCanvasAndPost(canvas);
                    }

                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                }
            }
        };
        Log.i("##info:", "game view thread created finished!");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadFlag = true;
        viewThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadFlag = false;
        boolean retry = true;

        while (retry) {
            try {
                viewThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouch(android.view.View v, MotionEvent event) {

        if(event.getAction() != MotionEvent.ACTION_UP) {
            return  true;
        }

        int ex = (int) event.getX();
        int ey = (int) event.getY();

        desk.onTouch(ex, ey);
        Log.i("##info", "GameView 触摸点位置:(" + ex + "," + ey + ")");
        //TODO

        return true;
    }
}
