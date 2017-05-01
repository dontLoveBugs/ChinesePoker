package com.wangxin.chinesepoker.Strategy;


import android.content.Context;

import com.wangxin.chinesepoker.Card.CardsHolder;
import com.wangxin.chinesepoker.Player.Player;

/**
 * Created by wangxin on 2017/4/10.
 */

public interface Strategy {

    public CardsHolder showCards(Player player, CardsHolder card, int cards[], boolean cardsFlags[], Context context);
}
