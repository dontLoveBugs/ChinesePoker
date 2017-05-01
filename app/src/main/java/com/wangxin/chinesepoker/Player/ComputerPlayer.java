package com.wangxin.chinesepoker.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.Interface.CardImage;
import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.Manager.CardsManager;
import com.wangxin.chinesepoker.R;
import com.wangxin.chinesepoker.Strategy.ComputerStrategy;

/**
 * Created by wangxin on 2017/4/10.
 */

public class ComputerPlayer extends Player {

    public ComputerPlayer(int[] cards, int left, int top, int cardDirection, int id, Desk desk, Context context) {
        super(cards, left, top, cardDirection, id, desk, context);
        this.strategy = new ComputerStrategy();
        Log.i("##info:", "create ComputerPlayer!");
    }

    @Override
    public void paint(Canvas canvas) {
        System.out.println("id:" + super.getPlayID());
        Rect src = new Rect();
        Rect des = new Rect();

        int row;
        int col;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Bitmap backImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.card_bg);

        src.set(0, 0, backImage.getWidth(), backImage.getHeight());
        des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
                (int) (top * MainActivity.SCALE_VERTICAL),
                (int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
                (int) ((top + 60) * MainActivity.SCALE_VERTICAL));
        RectF rectF = new RectF(des);
        canvas.drawRoundRect(rectF, 5, 5, paint);
        canvas.drawBitmap(backImage, src, des, paint);

        // 显示剩余牌数
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize((int) (20 * MainActivity.SCALE_HORIAONTAL));
        canvas.drawText("" + cards.length, (int) (left * MainActivity.SCALE_HORIAONTAL),
                (int) ((top + 80) * MainActivity.SCALE_VERTICAL), paint);
    }

    @Override
    public void paintResultCards(Canvas canvas) {
        // TODO Auto-generated method stub
        Rect src = new Rect();
        Rect des = new Rect();
        int row;
        int col;

        for (int i = 0; i < cards.length; i++) {
            row = CardsManager.getImageRow(cards[i]);
            col = CardsManager.getImageCol(cards[i]);
            Bitmap cardImage = BitmapFactory.decodeResource(context.getResources(),
                    CardImage.cardImages[row][col]);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


            src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
            des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
                    (int) ((top - 40 + i * 15) * MainActivity.SCALE_VERTICAL),
                    (int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((top + 20 + i * 15) * MainActivity.SCALE_VERTICAL));
            RectF rectF = new RectF(des);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(cardImage, src, des, paint);
        }

    }
}
