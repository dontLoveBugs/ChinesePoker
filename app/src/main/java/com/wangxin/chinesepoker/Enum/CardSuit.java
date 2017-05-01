package com.wangxin.chinesepoker.Enum;

/**
 * Created by wangxin on 2017/4/9.
 */

/**
 * 牌的花色
 * S H D C 2, 3, 4, 5, 6 , ~, K
 * Joker 1, 2
 */
public enum CardSuit {
    Spade(0), Heart(1), Diamond(2), Club(3), Joker(4);

    private int index;

    private CardSuit(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
