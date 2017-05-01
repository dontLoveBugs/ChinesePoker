package com.wangxin.chinesepoker.Adapter;


import com.wangxin.chinesepoker.Analyzer.CardsAnalyzer;
import com.wangxin.chinesepoker.Player.Player;

import java.util.Vector;

/**
 * Created by wangxin on 2017/4/13.
 */

public class AnalyzerAdapter implements AptTarget{
    private CardsAnalyzer analyzer = CardsAnalyzer.getInstance();

    public int[] getPokes() {
        return analyzer.getPokes();
    }

    public void setPokes(int[] pokes) {
        analyzer.setPokes(pokes);
    }

    public boolean lastCardTypeEq(int pokeType) {
        com.wangxin.chinesepoker.Enum.CardsType type;
        switch (pokeType) {
            case 1:
                type = com.wangxin.chinesepoker.Enum.CardsType.SINGLE;
                break;
            case 2:
                type = com.wangxin.chinesepoker.Enum.CardsType.PAIR;
                break;
            case 3:
                type = com.wangxin.chinesepoker.Enum.CardsType.TRIPLET;
                break;
            case 4:
                type = com.wangxin.chinesepoker.Enum.CardsType.TRIPLET_WITH_ONES;
                break;
            case 5:
                type = com.wangxin.chinesepoker.Enum.CardsType.SEQUENCE;
                break;
            case 6:
                type = com.wangxin.chinesepoker.Enum.CardsType.SEQUENCE_OF_PAIRS;
                break;
            case 7:
                type = com.wangxin.chinesepoker.Enum.CardsType.SEQUENCE_OF_TRIPLETS;
                break;
            case 8:
                type = com.wangxin.chinesepoker.Enum.CardsType.SEQUENCE_TRIPLETS_PAIRS;
                break;
            case 9:
                type = com.wangxin.chinesepoker.Enum.CardsType.QUADPLEX;
                break;
            case 10:
                type = com.wangxin.chinesepoker.Enum.CardsType.BOMB;
                break;
            case 11:
                type = com.wangxin.chinesepoker.Enum.CardsType.ROCKET;
                break;
            case 12:
                type = com.wangxin.chinesepoker.Enum.CardsType.ERROR;
                break;
            default:
                type = com.wangxin.chinesepoker.Enum.CardsType.ERROR;
                break;
        }
        return analyzer.lastCardTypeEq(type);
    }

    public int getCount2() {
        return analyzer.getCount_2();
    }

    public int getCountWang() {
        return analyzer.getCount_joker();
    }

    public Vector<int[]> getCard_zhadan() {
        return analyzer.getCardOfBomb();
    }

    public Vector<int[]> getCard_sanshun() {
        return analyzer.getCardOfSeqTrip();
    }

    public Vector<int[]> getCard_shuangshun() {
        return analyzer.getCardOfSeqPairs();
    }

    public Vector<int[]> getCard_sanzhang() {
        return analyzer.getCardOfTriplet();
    }

    public Vector<int[]> getCard_danshun() {
        return analyzer.getCardOfSeq();
    }

    public Vector<int[]> getCard_duipai() {
        return analyzer.getCardOfPairs();
    }

    public Vector<int[]> getCard_danpai() {
        return analyzer.getCardOfSingle();
    }

    public int remainCount() {
        return analyzer.remainCount();
    }

    public int[] getMinType(Player last, Player next) {
        return analyzer.getMinType(last, next);
    }
}
