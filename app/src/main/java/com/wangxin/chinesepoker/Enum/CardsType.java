package com.wangxin.chinesepoker.Enum;

/**
 * Created by wangxin on 2017/4/9.
 */

public enum CardsType {

    SINGLE(1),  // 单牌
    PAIR(2),    // 对牌
    TRIPLET(3), // 三张
    TRIPLET_WITH_ONES(4), // 三带一
    //TRIPLET_WITH_PAIR(13),  // 三带对
    SEQUENCE(5),             // 顺子
    SEQUENCE_OF_PAIRS(6),    // 双顺
    SEQUENCE_OF_TRIPLETS(7), // 三顺
    SEQUENCE_TRIPLETS_PAIRS(8), // 飞机
    QUADPLEX(9), // 四带二
    BOMB(10), // 炸弹
    ROCKET(11), // 轰炸
    ERROR(12); // 出牌错误

    private int index;

    private CardsType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public static CardsType getTypeByID(int id) {
        CardsType type;
        switch (id) {
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
        return type;
    }
}
