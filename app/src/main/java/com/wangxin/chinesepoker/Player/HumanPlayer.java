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

import com.wangxin.chinesepoker.Card.Card;
import com.wangxin.chinesepoker.Card.CardFactory;
import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.Enum.CardSuit;
import com.wangxin.chinesepoker.Interface.CardImage;
import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.Strategy.HumanStrategy;


/**
 * Created by wangxin on 2017/4/10.
 */

public class HumanPlayer extends Player{
    // 玩家名字
    private String name;

    /**
     * @param cards         玩家手中的牌
     * @param left          玩家的坐标位置
     * @param top
     * @param cardDirection 牌绘制的方形
     * @param id            玩家的ID
     * @param desk          玩家所在的游戏桌面
     * @param context       Android图形界面的上下文
     */
    public HumanPlayer(int[] cards, int left, int top, int cardDirection,
                       int id, Desk desk, Context context) {
        super(cards, left, top, cardDirection, id, desk, context);
        this.strategy = new HumanStrategy();
        Log.i("##info:", "create HumanPlayer!");
    }


    @Override
    public void paint(Canvas canvas) {
        Rect src = new Rect();
        Rect dst = new Rect();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        int cardImgID = 0;
        Bitmap cardImg;

        for(int i = 0; i < cards.length; i++) {
            int cardIndex = cards[i];
            Card card = CardFactory.getInstance().get(cardIndex);
            if(card.getCardSuit().equals(CardSuit.Joker)) {
                if(card.getValue() == 16) {
                    cardImgID = CardImage.cardImages[13][0];
                }
                else {
                    cardImgID = CardImage.cardImages[13][1];
                }
            }
            else {
                cardImgID = CardImage.cardImages[card.getValue() - 3][card.getCardSuit().getIndex()];
            }
            cardImg = BitmapFactory.decodeResource(context.getResources(), cardImgID);

            int select = 0;
            if (cardsFlag[i]) {
                select = 10;
            }
            src.set(0, 0, cardImg.getWidth(), cardImg.getHeight());
            dst.set((int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((top - select) * MainActivity.SCALE_VERTICAL),
                    (int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((top - select + 60) * MainActivity.SCALE_VERTICAL));
            RectF rectF = new RectF(dst);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(cardImg, src, dst, paint);
        }
    }

    @Override
    public void paintResultCards(Canvas canvas) {
        Rect src = new Rect();
        Rect dst = new Rect();

        int cardImgID;
        int cardIndex;
        Bitmap cardImg;

        for(int i =0; i < cards.length; i++) {
            cardIndex = cards[i];
            Card card = CardFactory.getInstance().get(cardIndex);
            if(card.getCardSuit().equals(CardSuit.Joker)) {
                if(card.getValue() == 16) {
                    cardImgID = CardImage.cardImages[13][0];
                }
                else {
                    cardImgID = CardImage.cardImages[13][1];
                }
            }
            else {
                cardImgID = CardImage.cardImages[card.getValue() - 3][card.getCardSuit().getIndex()];
            }
            cardImg = BitmapFactory.decodeResource(context.getResources(), cardImgID);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            src.set(0, 0, cardImg.getWidth(), cardImg.getHeight());
            dst.set((int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                    (int) (top * MainActivity.SCALE_VERTICAL),
                    (int) ((left + 80 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((top + 60) * MainActivity.SCALE_VERTICAL));
            RectF rectF = new RectF(dst);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(cardImg, src, dst, paint);
        }
    }

    public void regret() {

    }

}
