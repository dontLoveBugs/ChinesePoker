package com.wangxin.chinesepoker.Card;

/**
 * Created by wangxin on 2017/4/10.
 */

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

import com.wangxin.chinesepoker.Enum.CardSuit;
import com.wangxin.chinesepoker.Enum.CardsType;
import com.wangxin.chinesepoker.Interface.CardImage;
import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.Manager.CardsManager;


/**
 * 手牌
 */

public class CardsHolder {
    private int [] cards;        // 手牌的编号
    private int playID;          // 持有该手牌的玩家的ID
    Context context;             //
    private CardsType cardsType; // 该手牌的牌型
    private int cardsValue;

    public CardsHolder(int [] cards, int id, Context context) {
        this.cards = cards;
        this.playID = id;
        this.context = context;

        this.cardsType = CardsType.getTypeByID(CardsManager.getType(cards));
        this.cardsValue = CardsManager.getValue(cards);
        //TODO
        // 分析炸弹加倍状态在别处做比较好
        // 这个再考虑
    }

    public Context getContext() {
        return context;
    }

    public CardsType getCardsType() {
        return cardsType;
    }

    public int getCardsValue() {
        return cardsValue;
    }

    public int getCardsLength() {
        return cards.length;
    }

    public int getPlayID() {
        return playID;
    }

    public int[] getCards() {
        return cards;
    }

    /**
     *
     * @param canvas  画布
     * @param left    坐标
     * @param top
     * @param direction  方向
     */
    public void paint(Canvas canvas, int left, int top, int direction) {
        Rect src = new Rect();
        Rect dst = new Rect();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        int cardImgID = 0;
        Bitmap cardImg;

        for(int i = 0;  i < cards.length; i++) {
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
                cardImgID = CardImage.cardImages[card.getValue()-3][card.getCardSuit().getIndex()];
            }
            cardImg = BitmapFactory.decodeResource(context.getResources(), cardImgID);

            if(direction == Card.DIRECTION_VERTICAL) {

                src.set(0, 0, cardImg.getWidth(), cardImg.getHeight());
                dst.set((int) (left * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top + i * 15) * MainActivity.SCALE_VERTICAL),
                        (int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top + 60 + i * 15) * MainActivity.SCALE_VERTICAL));
            }
            else {
                src.set(0, 0, cardImg.getWidth(), cardImg.getHeight());
                dst.set((int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) (top * MainActivity.SCALE_VERTICAL),
                        (int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top + 60) * MainActivity.SCALE_VERTICAL));
            }
            RectF rectF = new RectF(dst);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(cardImg, src, dst, paint);
        }

    }


}
