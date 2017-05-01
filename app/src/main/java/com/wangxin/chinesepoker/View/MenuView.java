package com.wangxin.chinesepoker.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.R;

/**
 * Created by wangxin on 2017/4/7.
 */

public class MenuView extends View {


    Bitmap background;

    private int x = 300;
    private int y = 50;

    private Bitmap [] menuItems;

    public MenuView(Context context) {
        super(context);
        initResource();
    }

    /**
     * 初始化资源
     */
    private void initResource() {
        menuItems = new Bitmap[5];
        getSurfaceHolder();

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_bg);
        menuItems[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu1);
        menuItems[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu2);
        menuItems[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu3);
        menuItems[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu4);
        menuItems[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu5);

        this.getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }

    @Override
    public void createViewThread() {
        viewThread = new Thread() {
            @SuppressLint("WrongCall")
            @Override
            public void run() {
                super.run();

                while (threadFlag) {
                    try {
                        canvas = holder.lockCanvas(); // 获取绘制画布
                        synchronized (this) {
                            onDraw(canvas);           // 互斥绘制
                        }
                    } finally {
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

    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        Rect src = new Rect();
        Rect des = new Rect();
        src.set(0, 0, background.getWidth(), background.getHeight());
        des.set(0, 0, MainActivity.SCREEN_WIDTH, MainActivity.SCREEN_HEIGHT);

        Paint paint = new Paint();
        // 绘制北京
        canvas.drawBitmap(background, src, des, paint);
        //绘制菜单
        for(int i = 0; i < menuItems.length; i++) {
            canvas.drawBitmap(menuItems[i], (int)(x * MainActivity.SCALE_HORIAONTAL),
                    (int) ((y + i * 43) * MainActivity.SCALE_VERTICAL), paint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadFlag = true;
        viewThread.start();
        Log.i("##info:", "MenuView Surface Created");
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
                viewThread.join();  // 等待viewThread线程结束
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouch(android.view.View v, MotionEvent event) {
        int ex = (int) event.getX();
        int ey = (int) event.getY();

        Log.i("##info:", "触摸点位置：(" + ex + "," + ey + ")");

        int selectIndex = -1;

        for (int i = 0; i < menuItems.length; i++) {

            if( inRect(ex, ey, (int)(x * MainActivity.SCALE_HORIAONTAL),
                    (int) ((y + i * 43) * MainActivity.SCALE_VERTICAL),
                    (int) (125 * MainActivity.SCALE_HORIAONTAL),
                    (int) (33 * MainActivity.SCALE_VERTICAL)) ) {

                selectIndex = i;
                break;
            }
        }

        switch (selectIndex) {
            case 0 :
                MainActivity.handler.sendEmptyMessage(MainActivity.GAME);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                MainActivity.handler.sendEmptyMessage(MainActivity.EXIT);
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }
}
