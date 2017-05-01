package com.wangxin.chinesepoker.Strategy;

import android.content.Context;
import android.util.Log;

import com.wangxin.chinesepoker.Card.CardFactory;
import com.wangxin.chinesepoker.Card.CardsHolder;
import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.Interface.CardsType;
import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.Manager.CardsManager;
import com.wangxin.chinesepoker.Player.Player;


/**
 * Created by wangxin on 2017/4/10.
 */

public class HumanStrategy implements Strategy {
    @Override
    public CardsHolder showCards(Player player, CardsHolder card, int cards[], boolean cardsFlags[], Context context) {
        int count = 0;
        for(int i = 0; i < cards.length; i++) {
            if(cardsFlags[i]) {
                count++;
                Log.i("####", "已出牌 牌型" + String.valueOf(CardFactory.getInstance().get(cards[i]).getCardSuit())
                        + "牌值：" + String.valueOf(CardFactory.getInstance().get(cards[i]).getValue()));
            }
        }

        int [] chupaiPokes = new int[count];
        boolean [] cardsFlag = player.getCardsFlag();
        int j = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cardsFlag[i]) {
                chupaiPokes[j] = cards[i];
                j++;
            }
        }
        int cardType = CardsManager.getType(chupaiPokes);
        System.out.println("cardType:" + cardType);
        if (cardType == CardsType.error) {
            // 出牌错误
            if (chupaiPokes.length != 0) {
                MainActivity.handler.sendEmptyMessage(MainActivity.WRONG_CARD);
            }
            else {
                MainActivity.handler.sendEmptyMessage(MainActivity.EMPTY_CARD);
            }
            return null;
        }
        CardsHolder newLatestCardsHolder = new CardsHolder(chupaiPokes, player.getPlayID(), context);
        if (card == null) {
            Desk.cardsOnDesktop = newLatestCardsHolder;
            player.setLatestCards(newLatestCardsHolder);

            int[] newPokes = new int[cards.length - count];
            int k = 0;
            for (int i = 0; i < cards.length; i++) {
                if (!cardsFlag[i]) {
                    newPokes[k] = cards[i];
                    k++;
                }

            }

            player.setCards(newPokes);
            player.setCardsFlag(new boolean[cards.length]);
        }
        else {

            if (CardsManager.compare(newLatestCardsHolder, card) == 1) {
                Desk.cardsOnDesktop = newLatestCardsHolder;
                player.setLatestCards(newLatestCardsHolder);

                int[] newPokes = new int[cards.length - count];
                int ni = 0;
                for (int i = 0; i < cards.length; i++) {
                    if (!cardsFlag[i]) {
                        newPokes[ni] = cards[i];
                        ni++;
                    }
                }

                player.setCards(newPokes);
                player.setCardsFlag(new boolean[cards.length]);
            }
            if (CardsManager.compare(newLatestCardsHolder, card) == 0) {
                MainActivity.handler.sendEmptyMessage(MainActivity.SMALL_CARD);
                return null;
            }
            if (CardsManager.compare(newLatestCardsHolder, card) == -1) {
                MainActivity.handler.sendEmptyMessage(MainActivity.WRONG_CARD);
                return null;
            }
        }
        return newLatestCardsHolder;
    }
}
