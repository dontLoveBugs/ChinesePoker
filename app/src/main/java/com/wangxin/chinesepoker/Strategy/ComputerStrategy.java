package com.wangxin.chinesepoker.Strategy;

import android.content.Context;

import com.wangxin.chinesepoker.Card.CardsHolder;
import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.Manager.CardsManager;
import com.wangxin.chinesepoker.Player.Player;

/**
 * Created by wangxin on 2017/4/10.
 */

public class ComputerStrategy implements Strategy {
    @Override
    public CardsHolder showCards(Player player, CardsHolder card, int cards[], boolean cardsFlags[], Context context) {
        int[] pokeWanted = null;

        if (card == null) {
            // 玩家随意出一手牌
            pokeWanted = CardsManager.outCardByItsself(cards, player.getLast(), player.getNext());
        }
        else {


            // 玩家需要出一手比card大的牌
            pokeWanted = CardsManager.findTheRightCard(card, cards, player.getLast(), player.getNext());
        }
        // 如果不能出牌，则返回
        if (pokeWanted == null) {
            return null;
        }
        // 以下为出牌的后续操作，将牌从玩家手中剔除
        for (int i = 0; i < pokeWanted.length; i++) {
            for (int j = 0; j < cards.length; j++) {
                if (cards[j] == pokeWanted[i]) {
                    cards[j] = -1;
                    break;
                }
            }
        }
        int[] newpokes = new int[0];
        if (cards.length - pokeWanted.length > 0) {
            newpokes = new int[cards.length - pokeWanted.length];
        }
        int j = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != -1) {
                newpokes[j] = cards[i];
                j++;
            }
        }

        player.setCards(newpokes);
        CardsHolder thiscard = new CardsHolder(pokeWanted, player.getPlayID(), context);
        // 更新桌子最近一手牌
        Desk.cardsOnDesktop = thiscard;
        player.setLatestCards(thiscard);
        return thiscard;
    }
}
