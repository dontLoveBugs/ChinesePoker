package com.wangxin.chinesepoker.Oberver;


import com.wangxin.chinesepoker.Player.Player;

/**
 * Created by wangxin on 2017/4/14.
 */

public interface Observer {

    public void update(Player winner, int score);
}
