package com.wangxin.chinesepoker.Visitor;

import android.graphics.Canvas;

import com.wangxin.chinesepoker.Player.Player;

/**
 * Created by wangxin on 2017/4/14.
 */

public class ResultVisitor implements Visitor {
    @Override
    public void visitPlayer(Player player, Canvas canvas) {
        player.paintResult(canvas);
    }
}
