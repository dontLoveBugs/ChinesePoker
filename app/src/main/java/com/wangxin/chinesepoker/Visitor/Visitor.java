package com.wangxin.chinesepoker.Visitor;

import android.graphics.Canvas;

import com.wangxin.chinesepoker.Player.Player;

/**
 * Created by wangxin on 2017/4/14.
 */

public interface Visitor {

    //访问玩家
    public void visitPlayer(Player player, Canvas canvas);
}
