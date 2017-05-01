package com.wangxin.chinesepoker.Analyzer;


import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.Enum.CardsType;
import com.wangxin.chinesepoker.Manager.CardsManager;
import com.wangxin.chinesepoker.Player.Player;

import java.util.Vector;


/**
 * Created by wangxin on 2017/4/13.
 */

public class CardsAnalyzer {
    private int [] cards;
    private int count_2;         // 2的数量
    private int count_joker;     // 王的数量
    private int [] count_others = new int[12];    // 其他12中类型的牌的数量
    private Desk desk;

    private Vector<int []> cardOfBomb = new Vector<int []>(3);
    private Vector<int []> cardOfSeqTrip = new Vector<int []>(3);
    private Vector<int[]> cardOfSeqPairs = new Vector<int[]>(3);
    private Vector<int[]> cardOfTriplet = new Vector<int[]>(3);
    private Vector<int[]> cardOfSeq = new Vector<int[]>(3);
    private Vector<int[]> cardOfPairs = new Vector<int[]>(3);
    private Vector<int[]> cardOfSingle = new Vector<int[]>(5);


    public int getCount_2() {
        return count_2;
    }

    public int getCount_joker() {
        return count_joker;
    }

    public int[] getCount_others() {
        return count_others;
    }

    public Vector<int[]> getCardOfBomb() {
        return cardOfBomb;
    }

    public Vector<int[]> getCardOfSeqTrip() {
        return cardOfSeqTrip;
    }

    public Vector<int[]> getCardOfSeqPairs() {
        return cardOfSeqPairs;
    }

    public Vector<int[]> getCardOfTriplet() {
        return cardOfTriplet;
    }

    public Vector<int[]> getCardOfSeq() {
        return cardOfSeq;
    }

    public Vector<int[]> getCardOfPairs() {
        return cardOfPairs;
    }

    public Vector<int[]> getCardOfSingle() {
        return cardOfSingle;
    }


    // 单例模式创建分析器
    public static CardsAnalyzer getInstance() {
        return new CardsAnalyzer();
    }

    private void init() {
        for (int i = 0; i < count_others.length; i++) {
            count_others[i] = 0;
        }
        count_joker = 0;
        count_2 = 0;
        cardOfBomb.clear();
        cardOfSeq.clear();
        cardOfSeqPairs.clear();
        cardOfSeqTrip.clear();
        cardOfSingle.clear();
        cardOfPairs.clear();
        cardOfTriplet.clear();
    }

    public boolean lastCardTypeEq(CardsType pokeType) {
        if (remainCount() > 1) {
            return false;
        }
        switch (pokeType) {
            case TRIPLET :
                return cardOfTriplet.size() == 1;
            case PAIR :
                return cardOfPairs.size() == 1;
            case SINGLE:
                return cardOfSingle.size() == 1;
        }
        return false;
    }

    public int[] getPokes() {
        return cards;
    }

    public void setPokes(int[] pokes) {
        CardsManager.sort(pokes);
        this.cards = pokes;
        try {
            this.analyze();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int remainCount() {
        return cardOfSingle.size() + cardOfPairs.size() + cardOfTriplet.size() + cardOfSeq.size()
                + cardOfSeqPairs.size() + cardOfSeqTrip.size() + cardOfBomb.size();
    }

    public int[] getMinType(Player last, Player next) {

        CardsAnalyzer lastAna = CardsAnalyzer.getInstance();
        lastAna.setPokes(last.getCards());

        CardsAnalyzer nextAna = CardsAnalyzer.getInstance();
        nextAna.setPokes(next.getCards());

        // int lastCount = lastAna.remainCount();
        // int nextCount = nextAna.remainCount();

        int needSmart = -1;
        if (Desk.boss == next.getPlayID()
                || (Desk.boss != next.getPlayID() && Desk.boss != last.getPlayID())) {
            // 是对手
            if (next.getCards().length <= 2) {
                needSmart = next.getCards().length;
            }
        }

        // TODO
        int pokeType = -1;
        int minValue = 55;
        int pokeIdx = 0;
        int size;
        Vector<int[]> temp;

        temp = cardOfSeqTrip;
        size = temp.size();

        for (int i = 0; i < size; i++) {
            int[] p = temp.elementAt(i);
            if (minValue > p[0]) {
                pokeType = CardsType.SEQUENCE_OF_TRIPLETS.getIndex();
                minValue = p[0];
                pokeIdx = i;
            }
        }

        temp = cardOfSeqPairs;
        size = temp.size();

        for (int i = 0; i < size; i++) {
            int[] p = temp.elementAt(i);
            if (minValue > p[0]) {
                pokeType = CardsType.SEQUENCE_OF_PAIRS.getIndex();
                minValue = p[0];
                pokeIdx = i;
            }
        }

        temp = cardOfSeq;
        size = temp.size();

        for (int i = 0; i < size; i++) {
            int[] p = temp.elementAt(i);
            if (minValue > p[0]) {
                pokeType = CardsType.SEQUENCE.getIndex();
                minValue = p[0];
                pokeIdx = i;
            }
        }
        temp = cardOfTriplet;
        size = temp.size();

        for (int i = 0; i < size; i++) {
            int[] p = temp.elementAt(i);
            if (minValue > p[0]) {
                pokeType = CardsType.TRIPLET.getIndex();
                minValue = p[0];
                pokeIdx = i;
            }
        }

        if (needSmart == 2) {
            if (pokeType != -1) {
                return new int[]{pokeType, pokeIdx};
            }
            else {
                temp = cardOfPairs;
                size = temp.size();
                int min2 = -1;
                for (int i = 0; i < size; i++) {
                    int[] p = temp.elementAt(i);
                    if (min2 <= p[0]) {
                        pokeType = CardsType.PAIR.getIndex();
                        minValue = p[0];
                        min2 = p[0];
                        pokeIdx = i;
                    }
                }
            }

        }
        else {
            temp = cardOfPairs;
            size = temp.size();

            for (int i = 0; i < size; i++) {
                int[] p = temp.elementAt(i);
                if (minValue > p[0]) {
                    pokeType = CardsType.PAIR.getIndex();
                    minValue = p[0];
                    pokeIdx = i;
                }
            }
        }
        if (needSmart == 1) {
            if (pokeType != -1) {
                return new int[]{pokeType, pokeIdx};
            }
            else {
                int min1 = -1;
                for (int i = 0; i < size; i++) {
                    int[] p = temp.elementAt(i);
                    if (min1 <= p[0]) {
                        pokeType = CardsType.SINGLE.getIndex();
                        minValue = p[0];
                        min1 = p[0];
                        pokeIdx = i;
                    }
                }
            }

        }
        else {
            temp = cardOfSingle;
            size = temp.size();

            for (int i = 0; i < size; i++) {
                int[] p = temp.elementAt(i);
                if (minValue > p[0]) {
                    pokeType = CardsType.SINGLE.getIndex();
                    minValue = p[0];
                    pokeIdx = i;
                }
            }
        }
        return new int[]{pokeType, pokeIdx};
    }
    // 分析几大主要牌型
    private void analyze() {

        // 初始化牌型容器
        init();

        // 分析王，2，普通牌的数量
        for (int i = 0; i < cards.length; i++) {
            int v = CardsManager.getCardNumber(cards[i]);
            if (v == 16 || v == 17) {
                count_joker++;
            }
            else
            if (v == 15) {
                count_2++;
            }
            else {
                count_others[v - 3]++;
            }
        }

        // 分析三顺牌型
        int start = -1;
        int end = -1;
        for (int i = 0; i < count_others.length; i++) {
            if (count_others[i] == 3) {
                if (start == -1) {
                    start = i;
                }
                else {
                    end = i;
                }
            }
            else {
                if (end != -1 && start != -1) {
                    int dur = end - start + 1;
                    int[] ss = new int[dur * 3];
                    int m = 0;
                    for (int j = 0; j < cards.length; j++) {
                        int v = CardsManager.getCardNumber(cards[j]) - 3;
                        if (v >= start && v <= end) {
                            ss[m++] = cards[j];
                        }
                    }
                    if (m == dur * 3 - 1) {
                        System.out.println("sanshun is over!!!");
                    }
                    else {
                        System.out.println("sanshun error!!!");
                    }
                    cardOfSeqTrip.addElement(ss);
                    for (int s = start; s <= end; s++) {
                        count_others[s] = -1;
                    }
                    start = end = -1;
                    continue;
                }
                else {
                    start = end = -1;
                }
            }
        }

        // 分析双顺牌型
        int sstart = -1;
        int send = -1;
        for (int i = 0; i < count_others.length; i++) {
            if (count_others[i] == 2) {
                if (sstart == -1) {
                    sstart = i;
                }
                else {
                    send = i;
                }
            }
            else {
                if (sstart != -1 && send != -1) {
                    int dur = send - sstart + 1;
                    if (dur < 3) {
                        sstart = send = -1;
                        continue;
                    }
                    else {
                        int shuangshun[] = new int[dur * 2];
                        int m = 0;
                        for (int j = 0; j < cards.length; j++) {
                            int v = CardsManager.getCardNumber(cards[j]) - 3;
                            if (v >= sstart && v <= send) {
                                shuangshun[m++] = cards[j];
                            }
                        }
                        cardOfSeqPairs.addElement(shuangshun);
                        for (int s = sstart; s <= send; s++) {
                            count_others[s] = -1;
                        }
                        sstart = send = -1;
                        continue;
                    }
                }
                else {
                    sstart = send = -1;
                }
            }
        }

        // 分析单顺牌型
        int dstart = -1;
        int dend = -1;
        for (int i = 0; i < count_others.length; i++) {
            if (count_others[i] >= 1) {
                if (dstart == -1) {
                    dstart = i;
                }
                else {
                    dend = i;
                }
            }
            else {
                if (dstart != -1 && dend != -1) {
                    int dur = dend - dstart + 1;
                    if (dur >= 5) {
                        int m = 0;
                        int[] danshun = new int[dur];
                        for (int j = 0; j < cards.length; j++) {
                            int v = CardsManager.getCardNumber(cards[j]) - 3;
                            if (v == dend) {
                                danshun[m++] = cards[j];
                                count_others[dend]--;
                                dend--;
                            }
                            if (dend == dstart - 1) {
                                break;
                            }
                        }
                        cardOfSeq.addElement(danshun);
                    }
                    dstart = dend = -1;
                }
                else {
                    dstart = dend = -1;
                }
            }
        }

        // 分析三张牌型
        for (int i = 0; i < count_others.length; i++) {
            if (count_others[i] == 3) {
                count_others[i] = -1;
                int[] sanzhang = new int[3];
                int m = 0;
                for (int j = 0; j < cards.length; j++) {
                    int v = CardsManager.getCardNumber(cards[j]) - 3;
                    if (v == i) {
                        sanzhang[m++] = cards[j];
                    }
                }
                cardOfTriplet.addElement(sanzhang);
            }
        }

        // 分析对牌
        for (int i = 0; i < count_others.length; i++) {
            if (count_others[i] == 2) {
                int[] duipai = new int[2];
                for (int j = 0; j < cards.length; j++) {
                    int v = CardsManager.getCardNumber(cards[j]) - 3;
                    if (v == i) {
                        duipai[0] = cards[j];
                        duipai[1] = cards[j + 1];
                        cardOfPairs.addElement(duipai);
                        break;
                    }
                }
                count_others[i] = -1;
            }
        }

        // 分析单牌
        for (int i = 0; i < count_others.length; i++) {
            if (count_others[i] == 1) {
                for (int j = 0; j < cards.length; j++) {
                    int v = CardsManager.getCardNumber(cards[j]) - 3;
                    if (v == i) {
                        cardOfSingle.addElement(new int[]{cards[j]});
                        count_others[i] = -1;
                        break;
                    }

                }
            }
        }

        // 根据2的数量进行分析
        switch (count_2) {
            case 4 :
                cardOfBomb.addElement(new int[]{cards[count_joker], cards[count_joker + 1],
                        cards[count_joker + 2], cards[count_joker + 3]});
                break;
            case 3 :
                cardOfTriplet.addElement(new int[]{cards[count_joker], cards[count_joker + 1],
                        cards[count_joker + 2]});
                break;
            case 2 :
                cardOfPairs.addElement(new int[]{cards[count_joker], cards[count_joker + 1]});
                break;
            case 1 :
                cardOfSingle.addElement(new int[]{cards[count_joker]});
                break;
        }

        // 分析炸弹
        for (int i = 0; i < count_others.length - 1; i++) {
            if (count_others[i] == 4) {
                cardOfBomb.addElement(new int[]{i * 4 + 3, i * 4 + 2, i * 4 + 1, i * 4});
                count_others[i] = -1;
            }
        }

        // 分析火箭
        if (count_joker == 1) {
            cardOfSingle.addElement(new int[]{cards[0]});
        }
        else
        if (count_joker== 2) {
            cardOfBomb.addElement(new int[]{cards[0], cards[1]});
        }
    }
}
