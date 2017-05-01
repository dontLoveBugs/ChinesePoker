package com.wangxin.chinesepoker.Adapter;

import com.wangxin.chinesepoker.Player.Player;

import java.util.Vector;

/**
 * Created by wangxin on 2017/4/13.
 */

public interface AptTarget {

    public int[] getPokes();

    public void setPokes(int[] pokes);

    public boolean lastCardTypeEq(int pokeType);

    public int getCount2();

    public int getCountWang();

    public Vector<int[]> getCard_zhadan();

    public Vector<int[]> getCard_sanshun();

    public Vector<int[]> getCard_shuangshun();

    public Vector<int[]> getCard_sanzhang();

    public Vector<int[]> getCard_danshun();

    public Vector<int[]> getCard_duipai();

    public Vector<int[]> getCard_danpai();

    public int remainCount();

    public int[] getMinType(Player last, Player next);
}
