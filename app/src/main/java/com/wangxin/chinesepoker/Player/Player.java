package com.wangxin.chinesepoker.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

import com.wangxin.chinesepoker.Card.CardsHolder;
import com.wangxin.chinesepoker.Desk;
import com.wangxin.chinesepoker.Interface.CardsType;
import com.wangxin.chinesepoker.MainActivity;
import com.wangxin.chinesepoker.Manager.CardsManager;
import com.wangxin.chinesepoker.Oberver.Observer;
import com.wangxin.chinesepoker.Strategy.Strategy;
import com.wangxin.chinesepoker.View.GameView;
import com.wangxin.chinesepoker.Visitor.Visitor;

/**
 * Created by wangxin on 2017/4/9.
 */

public abstract class Player implements Observer{

    int [] cards;  // 玩家手中的牌
    boolean [] cardsFlag;  // 玩家出牌的标志位
    // 备忘录记录选中的牌 以方便重选时恢复

    int playID;    // 玩家ID
    int currentID; // 当前玩家的ID

    int currentCircle; // 当前的回合

    int top, left;  // 玩家在桌面上的坐标

    Desk desk;  // 玩家所在的桌子

    private boolean isLandlord;

    // 玩家最新一手牌
    CardsHolder latestCards;

    // 桌面最新的一手牌
    CardsHolder cardsOnDesktop;



    Context context;

    Strategy strategy;   // 出牌策略

    private String result;
    private int scores;
    private int score;

    // 前一个出牌玩家 后一个出牌玩家
    private Player last, next;

    private int paintDirection = CardsType.direction_Vertical;


    // Getter  Setter 函数
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public void setLandlord(boolean landlord) {
        this.isLandlord = landlord;
    }

    public boolean isLandlord() {
        return isLandlord;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public Desk getDesk() {
        return desk;
    }

    public Context getContext() {
        return context;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public CardsHolder getCardsOnDesktop() {
        return cardsOnDesktop;
    }

    // 访问者模式接口
    public void accpet(Visitor visitor, Canvas canvas) {
        visitor.visitPlayer(this, canvas);
    }



    @Override
    public void update(Player winner, int score) {
        if(playID == winner.getPlayID()) {
            if(isLandlord) {
                this.score = score * 2;
                this.scores = this.scores + score * 2;
            }
            else {
                this.score = score;
                this.scores += score;
            }
            result = "WINNER";
        }
        else {
            if(winner.isLandlord()) {
                this.scores -= score;
                this.score = -score;
                result = "LOSER";
            }
            else {
                this.scores += score;
                this.score = score;
                result = "WINNER";
            }
        }
        Log.i("##info", "Player " + this.getPlayID() + " score " + this.score + " as " + result);
    }

    public void paintResult(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize((int) (20 * MainActivity.SCALE_HORIAONTAL));

        canvas.drawText("玩家" + playID + ":本局得分:" + score + "   总分：" + scores,
                (int) (110 * MainActivity.SCALE_HORIAONTAL),
                (int) ((96 + playID * 30) * MainActivity.SCALE_VERTICAL), paint);

        this.paintResultCards(canvas);
    }

    /**
     *
     * @param cards 玩家手中的牌
     * @param left  玩家的坐标位置
     * @param top
     * @param cardDirection  牌绘制的方形
     * @param id        玩家的ID
     * @param desk      玩家所在的游戏桌面
     * @param context   Android图形界面的上下文
     */
    public Player(int [] cards, int left, int top, int cardDirection, int id, Desk desk, Context context) {
        this.cards = cards;
        this.left = left;
        this.top = top;
        this.playID = id;
        this.desk = desk;
        this.context = context;
        cardsFlag = new boolean[cards.length];
        this.paintDirection = cardDirection;
        this.setLeftAndTop(left, top);

    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    // 设置坐标
    public  void setLeftAndTop(int left, int top) {
        this.left = left;
        this.top = top;
    }

    // 设置玩家出牌的上下级关系
    public void setLastAndNext(Player last, Player next) {
        this.last = last;
        this.next = next;
    }

    public int getPaintDirection() {
        return paintDirection;
    }

    public Player getLast() {
        return last;
    }

    public Player getNext() {
        return next;
    }

    public int[] getCards() {
        return cards;
    }

    public void setCards(int[] cards) {
        this.cards = cards;
    }

    public boolean[] getCardsFlag() {
        return cardsFlag;
    }

    public void setCardsFlag(boolean[] cardsFlag) {
        this.cardsFlag = cardsFlag;
    }

    public int getPlayID() {
        return playID;
    }

    public void setPlayID(int playID) {
        this.playID = playID;
    }

    public CardsHolder getLatestCards() {
        return latestCards;
    }

    public void setLatestCards(CardsHolder latestCards) {
        this.latestCards = latestCards;
    }

    // 绘制玩家手中的牌
    public abstract void paint(Canvas canvas);

    //
    public abstract void paintResultCards(Canvas canvas);

    public CardsHolder showCards(CardsHolder card) {
        return strategy.showCards(this, card, cards, cardsFlag, this.context);
    }

    public void OnTouch(int x, int y) {
        for (int i = 0; i < cards.length; i++) {
            // 判断是那张牌被选中，设置标志
            if (i != cards.length - 1) {
                if (GameView.inRect(x, y,
                        (int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top - (cardsFlag[i] ? 10 : 0)) * MainActivity.SCALE_VERTICAL),
                        (int) (20 * MainActivity.SCALE_HORIAONTAL),
                        (int) (60 * MainActivity.SCALE_VERTICAL))) {
                    cardsFlag[i] = !cardsFlag[i];
                    break;
                }
            }
            else {
                if (GameView.inRect(x, y,
                        (int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top - (cardsFlag[i] ? 10 : 0)) * MainActivity.SCALE_VERTICAL),
                        (int) (40 * MainActivity.SCALE_HORIAONTAL),
                        (int) (60 * MainActivity.SCALE_VERTICAL))) {
                    cardsFlag[i] = !cardsFlag[i];
                    break;
                }
            }

        }
    }

    public void hintPlayer(CardsHolder cardsOnDesktop) {
        int[] pokeWanted = null;

        if (cardsOnDesktop == null) {
            // 玩家随意出一手牌
            pokeWanted = CardsManager.outCardByItsself(cards, this.getLast(), this.getNext());
        }
        else {
            // 玩家需要出一手比card大的牌
            pokeWanted = CardsManager.findTheRightCard(cardsOnDesktop, cards, this.getLast(), this.getNext());
        }
        // 如果不能出牌，则返回
        if (pokeWanted != null) {
            for(int i = 0; i < cards.length; i++) {
                for (int j = 0; j < pokeWanted.length; j++) {
                    if(cards[i] == pokeWanted[j]) {
                        cardsFlag[i] = true;
                    }
                }
            }
        }
        else {
            Toast.makeText(context, "您没有能出的牌！", Toast.LENGTH_SHORT).show();
        }
    }


    public void redo() {
        // TODO Auto-generated method stub
        for (int i = 0; i < cardsFlag.length; i++) {
            cardsFlag[i] = false;
        }
    }

}
