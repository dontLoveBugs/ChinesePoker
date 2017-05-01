package com.wangxin.chinesepoker.Card;

import com.wangxin.chinesepoker.Enum.CardSuit;
import com.wangxin.chinesepoker.Interface.CardImage;

/**
 * Created by wangxin on 2017/4/9.
 */

public class Card {
    public final static int DIRECTION_HORIZONTAL = 0;
    public final static int DIRECTION_VERTICAL = 1;

    private CardSuit cardSuit;
    private int value;

    public Card(CardSuit cardSuit, int value) {
        this.cardSuit = cardSuit;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    public void setCardSuit(CardSuit cardSuit) {
        this.cardSuit = cardSuit;
    }

    public int getCardImgID() {
        if(CardSuit.Joker.equals(cardSuit)) {
            return CardImage.cardImages[13][value];
        }
        return CardImage.cardImages[value][cardSuit.getIndex()];
    }
}
