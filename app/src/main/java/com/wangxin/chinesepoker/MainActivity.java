package com.wangxin.chinesepoker;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.wangxin.chinesepoker.View.GameView;
import com.wangxin.chinesepoker.View.GameViewFactory;
import com.wangxin.chinesepoker.View.MenuView;
import com.wangxin.chinesepoker.View.MenuViewFactory;
import com.wangxin.chinesepoker.View.ViewFactory;


public class MainActivity extends Activity {

    public final static int MENU = 0;
    public final static int GAME = 1;
    public final static int EXIT = 2;
    public final static int SMALL_CARD = 3;
    public final static int WRONG_CARD = 4;
    public final static int EMPTY_CARD = 5;

    //屏幕显示信息
    public static int SCREEN_WIDTH;  // 像素点数
    public static int SCREEN_HEIGHT;
    public static double SCALE_VERTICAL; // 像素密度
    public static double SCALE_HORIAONTAL;

    public static Handler handler;

    private MenuView mv;
    private GameView gv;

    //屏幕分辨率


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //设置显示效果 全屏 无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Log.i("##info", "test!!!!!!!!!!!!!!!!!!!!!");

        //获取屏幕显示信息
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_HEIGHT > SCREEN_WIDTH) {
            int temp = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = temp;
        }

        Log.i("##像素分辨率为", SCREEN_WIDTH + "X" + SCREEN_HEIGHT);
        // 1920 1080是屏幕分辨率
        SCALE_VERTICAL = SCREEN_HEIGHT / 320.0;
        SCALE_HORIAONTAL = SCREEN_WIDTH / 480.0;

        Log.i("##像素密度", SCALE_VERTICAL + " and " + SCALE_HORIAONTAL);

        ViewFactory mvf = new MenuViewFactory();
        mv = (MenuView) mvf.createView(this);
        ViewFactory gvf = new GameViewFactory();
        gv = (GameView) gvf.createView(this.getApplicationContext());

        setContentView(mv);

        // 消息处理
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MENU:
                        setContentView(mv);
                        break;
                    case GAME:
                        Log.i("##info:", "tap GAME!");
                        setContentView(gv);
                        break;
                    case EXIT:
                        finish();
                        break;
                    case SMALL_CARD:
                        Toast.makeText(getApplicationContext(), "你的牌太小!", Toast.LENGTH_SHORT).show();
                        break;
                    case WRONG_CARD:
                        Toast.makeText(getApplicationContext(), "出牌不符合规则!", Toast.LENGTH_SHORT).show();
                        break;
                    case EMPTY_CARD:
                        Toast.makeText(getApplicationContext(), "请出牌!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };


    }
}
