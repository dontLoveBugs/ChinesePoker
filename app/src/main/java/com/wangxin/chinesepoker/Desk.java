package com.wangxin.chinesepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.wangxin.chinesepoker.Card.CardsHolder;
import com.wangxin.chinesepoker.Interface.CardImage;
import com.wangxin.chinesepoker.Interface.CardsType;
import com.wangxin.chinesepoker.Iterator.Iterator;
import com.wangxin.chinesepoker.Manager.CardsManager;
import com.wangxin.chinesepoker.Memento.LastState;
import com.wangxin.chinesepoker.Memento.Memento;
import com.wangxin.chinesepoker.Memento.MementoCaretaker;
import com.wangxin.chinesepoker.Oberver.Observer;
import com.wangxin.chinesepoker.Oberver.ObserverTarget;
import com.wangxin.chinesepoker.Player.ComputerPlayer;
import com.wangxin.chinesepoker.Player.HumanPlayer;
import com.wangxin.chinesepoker.Player.Player;
import com.wangxin.chinesepoker.State.ScoreDoubleState;
import com.wangxin.chinesepoker.State.ScoreNonDoubleState;
import com.wangxin.chinesepoker.State.ScoreState;
import com.wangxin.chinesepoker.Strategy.ComputerStrategy;
import com.wangxin.chinesepoker.Strategy.HumanStrategy;
import com.wangxin.chinesepoker.Visitor.ResultVisitor;
import com.wangxin.chinesepoker.Visitor.Visitor;

/**
 * Created by wangxin on 2017/4/9.
 */

// Desk单例 观察者模式 Desk是观察者目标 如果有玩家操作 调用观察者模式更新界面

public class Desk extends ObserverTarget{
    public static int winId = -1;
    private static int runs = 0;

    Bitmap cardImages;

    // Bitmap regretImg;       //悔牌
//    Bitmap passImg;         //跳过
//    Bitmap showCardImg;     //出牌
//    Bitmap hintImg;         //提示  ？？使用哪种模式实现？？
//    Bitmap redoImg;         //重选  通过 状态模式实现

    Bitmap redoImage;
    Bitmap passImage;
    Bitmap chuPaiImage;
    Bitmap tiShiImage;


    // 玩家头像
    Bitmap farmerImage;
    Bitmap landlordImage;

    private static Context context;

    // 备忘录模式
    private static MementoCaretaker humanMC = new MementoCaretaker();

    // 状态模式 记录分数
    ScoreState state = new ScoreNonDoubleState(); //初始状态为未加倍状态

    public static Player[] players = new Player[3];// 三个玩家
    public static int multiple = 1;// 当前倍数
    public static int boss = 0;// 地主
    public boolean ifClickChupai = false;


    private static int[] scores = new int[3];
    private int[] threeCards = new int[3];// 三张底牌
    private int[][] threeCardsPosition = {{170, 10}, {220, 10}, {270, 10}};
    private int[][] timeLimitePosition = {{130, 190}, {80, 80}, {360, 80}};
    private int[][] passPosition = {{130, 190}, {80, 80}, {360, 80}};
    private int[][] playerLatestCardsPosition = {{130, 140}, {80, 60}, {360, 60}};
    private int[][] playerCardsPosition = {{30, 210}, {30, 60}, {410, 60}};
    private int[][] scorePosition = {{70, 290}, {70, 30}, {340, 30}};
    private int[][] iconPosition = {{30, 270}, {30, 10}, {410, 10}};
    private int buttonPosition_X = 240;
    private int buttonPosition_Y = 160;
    private boolean[] canPass = new boolean[3];

    private int[][] playerCards = new int[3][17];

    private boolean canDrawLatestCards = false;
    private int[] allCards = new int[54];// 一副扑克牌

    private int currentScore = 10;// 当前分数
    private int currentId = 0;// 当前操作的人
    private int currentCircle = 0;// 本轮次数

    public static CardsHolder cardsOnDesktop = null;// 最新的一手牌
    private int timeLimite = 300;
    // 存储胜负得分信息
    private int result[] = new int[3];

    /**
     * 游戏控制逻辑
     * -1 重新开始 0 游戏中 1 本局游戏结束
     */
    private static int op = -1;

    // 单例模式创建GameDesk
    // 带参数的懒汉式写法
    private static Desk desk;

    private Desk(Context context) {
        super();
        this.context = context;

        redoImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_redo);
        passImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pass);
        chuPaiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_chupai);
        tiShiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_tishi);

        farmerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_farmer);
        landlordImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_landlord);
    };

    public static Desk getInstance(Context context) {
        if(desk == null) {
            synchronized (Desk.class) {
                if(desk == null) {
                    desk = new Desk(context);
                }
            }
        }
        desk.context = context;
        Log.i("##info:", "create game desk!");
        return desk;
    }

    public static Context getContext() {
        return context;
    }


    public void gameLogic() {
        switch (op) {
            case -1 :
                init();
                Log.i("##info", "desk init!");
                op = 0;
                break;
            case 0 :
                //Log.i("##info", "poler gaming！");
                checkGameOver();
                break;
            case 1 :
                break;
        }
    }

    public void controlPaint(Canvas canvas) {
        switch (op) {
            case -1 :
                break;
            case 0 :
                paintGaming(canvas);
                break;
            case 1 :
                paintResult(canvas);
                break;

        }
    }


    private void checkGameOver() {
        for (int k = 0; k < 3; k++) {
            // 当三个人中其中一个人牌的数量为0，则游戏结束
            if (players[k].getCards().length == 0) {
                // 切换到游戏结束状态
                op = 1;
                // 得到最先出去的人的id
                winId = k;
                // 判断哪方获胜
//                if (boss == winId) {
//                    // 地主方获胜后的积分操作
//                    for (int i = 0; i < 3; i++) {
//                        if (i == boss) {
//                            // 地主需要加两倍积分
//                            result[i] = currentScore * multiple * 2;
//                            scores[i] += currentScore * multiple * 2;
//                        }
//                        else {
//                            // 农民方需要减分
//                            result[i] = -currentScore * multiple;
//                            scores[i] -= currentScore * multiple;
//                        }
//                    }
//                }
//                else {
//                    // 如果农民方胜利
//                    for (int i = 0; i < 3; i++) {
//                        if (i != boss) {
//                            // 农民方加分
//                            result[i] = currentScore * multiple;
//                            scores[i] += currentScore * multiple;
//                        }
//                        else {
//                            // 地主方减分
//                            result[i] = -currentScore * multiple * 2;
//                            scores[i] -= currentScore * multiple * 2;
//                        }
//                    }
//                }
//                for(int i = 0; i < 3; i++) {
//                    players[i].setScores(scores[i]);
//                    if(result[i] > 0) {
//                        players[i].setResult("WINNER");
//                    }
//                    else {
//                        players[i].setResult("LOSER");
//                    }
//                }
                // 观察者模式改写
                this.notifyObservers(players[winId], currentScore * multiple);
                runs++;
                for(int i = 0; i < 3; i++) {
                    scores[i] = players[i].getScores();
                    detach(players[i]);
                }  // 将得分写回Desk 以便下一局显示正确
                // Toast.makeText(context, "点击屏幕重新开始！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        CardsHolder goCard = null;
        // 游戏没有结束，继续。
        // 如果轮到电脑出牌
        if (currentId == 1 || currentId == 2) {
            if (timeLimite <= 300 && timeLimite >= 0) {
                if(currentId == boss) {
                    // 备忘录模式记录 悔牌前的状况
                    LastState lastState = new LastState(players, cardsOnDesktop, playerCards, currentCircle, currentId, multiple, canPass, canDrawLatestCards);
                    humanMC.saveMemento(this.createMemento(lastState));
                }
                // 获取手中的牌中能够打过当前手牌
                CardsHolder tempcard = players[currentId].showCards(cardsOnDesktop);
                if (tempcard != null) {
                    // 手中有大过的牌，则出
                    cardsOnDesktop = tempcard;
                    goCard = tempcard;
                    nextPerson();
                }
                else {
                    // 没有打过的牌，则不要
                    buyao();
                }
            }
        }

        // 如果轮到本人出牌
        if (currentId == 0) {

            if (timeLimite <= 300 && timeLimite >= 0) {
                if (ifClickChupai == true) {
                    if(currentId == boss) {
                        // 备忘录模式记录 悔牌前的状况
                        LastState lastState = new LastState(players, cardsOnDesktop, playerCards, currentCircle, currentId, multiple, canPass, canDrawLatestCards);
                        humanMC.saveMemento(this.createMemento(lastState));
                    }
                    CardsHolder card = players[0].showCards(cardsOnDesktop);
                    if (card != null) {
                        cardsOnDesktop = card;
                        goCard = card;
                        nextPerson();
                    }
                    ifClickChupai = false;
                }

            }
            else {
                if (currentCircle != 0) {
                    if(currentId == boss) {
                        // 备忘录模式记录 悔牌前的状况
                        LastState lastState = new LastState(players, cardsOnDesktop, playerCards, currentCircle, currentId, multiple, canPass, canDrawLatestCards);
                        humanMC.saveMemento(this.createMemento(lastState));
                    }
                    buyao();
                }
                else {
                    if(currentId == boss) {
                        // 备忘录模式记录 悔牌前的状况
                        LastState lastState = new LastState(players, cardsOnDesktop, playerCards, currentCircle, currentId, multiple, canPass, canDrawLatestCards);
                        humanMC.saveMemento(this.createMemento(lastState));
                    }
                    players[currentId].setStrategy(new ComputerStrategy());
                    CardsHolder autoCard = players[currentId].showCards(cardsOnDesktop);
                    cardsOnDesktop = autoCard;
                    goCard = autoCard;
                    players[currentId].setStrategy(new HumanStrategy());
                    nextPerson();
                }
            }
        }

        if(goCard != null) {
            if(goCard.getCardsType().equals(com.wangxin.chinesepoker.Enum.CardsType.BOMB)
                    || goCard.getCardsType().equals(com.wangxin.chinesepoker.Enum.CardsType.ROCKET) ) {
                state = new ScoreDoubleState();
            }
            else {
                state = new ScoreNonDoubleState();
            }
        }
        else {
            state = new ScoreNonDoubleState();
        }
        state.multiple(desk);       // 每次打完牌后 根据状态决定是否加倍
        // 时间倒计时
        timeLimite -= 2;
        canDrawLatestCards = true;
    }

    // 初始化游戏
    public void init() {
        allCards = new int[54];
        playerCards = new int[3][17];
        threeCards = new int[3];
        winId = -1;
        currentScore = 3;
        multiple = 1;
        cardsOnDesktop = null;
        currentCircle = 0;
        currentId = 0;

        for (int i = 0; i < 3; i++) {
            canPass[i] = false;
        }
        for (int i = 0; i < allCards.length; i++) {
            allCards[i] = i;
        }
        CardsManager.shuffle(allCards);
        fapai(allCards);
        chooseBoss();
        CardsManager.sort(playerCards[0]);
        CardsManager.sort(playerCards[1]);
        CardsManager.sort(playerCards[2]);
        players[0] = new HumanPlayer(playerCards[0], playerCardsPosition[0][0],
                playerCardsPosition[0][1], CardsType.direction_Horizontal, 0, this, context);
        players[1] = new ComputerPlayer(playerCards[1], playerCardsPosition[1][0],
                playerCardsPosition[1][1], CardsType.direction_Vertical, 1, this, context);
        players[2] = new ComputerPlayer(playerCards[2], playerCardsPosition[2][0],
                playerCardsPosition[2][1], CardsType.direction_Vertical, 2, this, context);
        players[0].setLastAndNext(players[1], players[2]);
        players[1].setLastAndNext(players[2], players[0]);
        players[2].setLastAndNext(players[0], players[1]);

        for(int i = 0; i < 3; i++) {
            if(i == boss)
                players[i].setLandlord(true);
            else
                players[i].setLandlord(false);
        }

        if(runs == 0) {
            for(int i = 0; i < 3; i++) {
                scores[i] = 50;
                this.attach(players[i]);
                players[i].setScores(scores[i]);
            }
        }
        else {
            for(int i = 0; i < 3; i++) {
                this.attach(players[i]);
                players[i].setScores(scores[i]);
            }
        }

        Log.i("##info", "第" + runs + "局游戏开始");
    }

    // 发牌
    public void fapai(int[] cards) {
        for (int i = 0; i < 51; i++) {
            playerCards[i / 17][i % 17] = cards[i];
        }
        threeCards[0] = cards[51];
        threeCards[1] = cards[52];
        threeCards[2] = cards[53];
    }

    private void chooseBoss() {
        boss = CardsManager.getBoss();
        currentId = boss;
        int[] diZhuCards = new int[20];
        for (int i = 0; i < 17; i++) {
            diZhuCards[i] = playerCards[boss][i];
        }
        diZhuCards[17] = threeCards[0];
        diZhuCards[18] = threeCards[1];
        diZhuCards[19] = threeCards[2];
        playerCards[boss] = diZhuCards;
    }

    // 不要牌的操作
    private void buyao() {
        // 清空当前不要牌的人的最后一手牌
        players[currentId].setLatestCards(null);

        canPass[currentId] = true;
        // 定位下一个人的id
        nextPerson();
        // 如果已经转回来，则该人继续出牌，本轮清空，新一轮开始
        if (cardsOnDesktop != null && currentId == cardsOnDesktop.getPlayID()) {
            currentCircle = 0;
            cardsOnDesktop = null;// 转回到最大牌的那个人再出牌
            players[currentId].setLatestCards(null);
        }
    }

    // 定位下一个人的id并重新倒计时
    private void nextPerson() {
        switch (currentId) {
            case 0 :
                currentId = 2;
                break;
            case 1 :
                currentId = 0;
                break;
            case 2 :
                currentId = 1;
                break;
        }
        currentCircle++;
        timeLimite = 300;
    }

    // 绘制游戏画面
    private void paintGaming(Canvas canvas) {

        players[0].paint(canvas);
        players[1].paint(canvas);
        players[2].paint(canvas);
        paintThreeCards(canvas);
        paintIconAndScore(canvas);
        paintTimeLimite(canvas);

        // 如果轮到本人出牌，画“不要”“出牌”“重新开始”按钮
        if (currentId == 0) {
            Rect src = new Rect();
            Rect dst = new Rect();

            src.set(0, 0, chuPaiImage.getWidth(), chuPaiImage.getHeight());
            dst.set((int) (buttonPosition_X * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPosition_Y * MainActivity.SCALE_VERTICAL),
                    (int) ((buttonPosition_X + 80) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPosition_Y + 40) * MainActivity.SCALE_VERTICAL));
            canvas.drawBitmap(chuPaiImage, src, dst, null);

            if (currentCircle != 0) {
                src.set(0, 0, passImage.getWidth(), passImage.getHeight());
                dst.set((int) ((buttonPosition_X - 80) * MainActivity.SCALE_HORIAONTAL),
                        (int) (buttonPosition_Y * MainActivity.SCALE_VERTICAL),
                        (int) ((buttonPosition_X) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((buttonPosition_Y + 40) * MainActivity.SCALE_VERTICAL));
                canvas.drawBitmap(passImage, src, dst, null);
            }

            src.set(0, 0, redoImage.getWidth(), redoImage.getHeight());
            dst.set((int) ((buttonPosition_X + 80) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPosition_Y) * MainActivity.SCALE_VERTICAL),
                    (int) ((buttonPosition_X + 160) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPosition_Y + 40) * MainActivity.SCALE_VERTICAL));
            canvas.drawBitmap(redoImage, src, dst, null);

            src.set(0, 0, tiShiImage.getWidth(), tiShiImage.getHeight());
            dst.set((int) ((buttonPosition_X + 160) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPosition_Y) * MainActivity.SCALE_VERTICAL),
                    (int) ((buttonPosition_X + 240) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPosition_Y + 40) * MainActivity.SCALE_VERTICAL));
            canvas.drawBitmap(tiShiImage, src, dst, null);

        }

        // 画各自刚出的牌或“不要”
        for (int i = 0; i < 3; i++) {
            if (currentId != i && players[i].getLatestCards() != null && canDrawLatestCards == true) {
                players[i].getLatestCards().paint(canvas, playerLatestCardsPosition[i][0],
                        playerLatestCardsPosition[i][1], players[i].getPaintDirection());
            }
            if (currentId != i && players[i].getLatestCards() == null && canPass[i] == true) {
                paintPass(canvas, i);
            }
        }

    }

    // 画倒计时
    private void paintTimeLimite(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize((int) (16 * MainActivity.SCALE_HORIAONTAL));
        for (int i = 0; i < 3; i++) {
            if (i == currentId) {
                canvas.drawText("" + (timeLimite / 10),
                        (int) (timeLimitePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) (timeLimitePosition[i][1] * MainActivity.SCALE_VERTICAL), paint);
            }
        }
    }

    // 画“不要”二字
    private void paintPass(Canvas canvas, int id) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize((int) (16 * MainActivity.SCALE_HORIAONTAL));
        canvas.drawText("不要", (int) (passPosition[id][0] * MainActivity.SCALE_HORIAONTAL),
                (int) (passPosition[id][1] * MainActivity.SCALE_VERTICAL), paint);

    }

    // 画游戏中的分数
    private void paintIconAndScore(Canvas canvas) {

        Paint paint = new Paint();
        paint.setTextSize((int) (16 * MainActivity.SCALE_VERTICAL));
        Rect src = new Rect();
        Rect dst = new Rect();
        for (int i = 0; i < 3; i++) {
            if (boss == i) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(1);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                src.set(0, 0, landlordImage.getWidth(), landlordImage.getHeight());
                dst.set((int) (iconPosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) (iconPosition[i][1] * MainActivity.SCALE_VERTICAL),
                        (int) ((iconPosition[i][0] + 40) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((iconPosition[i][1] + 40) * MainActivity.SCALE_VERTICAL));
                RectF rectF = new RectF(dst);
                canvas.drawRoundRect(rectF, 5, 5, paint);
                canvas.drawBitmap(landlordImage, src, dst, paint);

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawText("玩家" + i,
                        (int) (scorePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) (scorePosition[i][1] * MainActivity.SCALE_VERTICAL), paint);
                canvas.drawText("得分：" + scores[i],
                        (int) (scorePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) ((scorePosition[i][1] + 20) * MainActivity.SCALE_VERTICAL), paint);
            }
            else {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(1);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                src.set(0, 0, farmerImage.getWidth(), farmerImage.getHeight());
                dst.set((int) (iconPosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) (iconPosition[i][1] * MainActivity.SCALE_VERTICAL),
                        (int) ((iconPosition[i][0] + 40) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((iconPosition[i][1] + 40) * MainActivity.SCALE_VERTICAL));
                RectF rectF = new RectF(dst);
                canvas.drawRoundRect(rectF, 5, 5, paint);
                canvas.drawBitmap(farmerImage, src, rectF, paint);

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawText("玩家" + i,
                        (int) (scorePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) (scorePosition[i][1] * MainActivity.SCALE_VERTICAL), paint);
                canvas.drawText("得分：" + scores[i],
                        (int) (scorePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) ((scorePosition[i][1] + 20) * MainActivity.SCALE_VERTICAL), paint);
            }
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawText("当前底分：" + currentScore + "  当前倍数：" + multiple,
                (int) (150 * MainActivity.SCALE_HORIAONTAL),
                (int) (150 * MainActivity.SCALE_VERTICAL), paint);
    }

    // 画游戏结束时的分数和各自剩余牌
    // @Visitor
    private void paintResult(Canvas canvas) {

        Visitor rv = new ResultVisitor();
        for (int i = 0; i < 3; i++) {
            players[i].accpet(rv, canvas);
        }
    }

    // 画地主的三张牌
    private void paintThreeCards(Canvas canvas) {
        Rect src = new Rect();
        Rect dst = new Rect();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        for (int i = 0; i < 3; i++) {
            int row = CardsManager.getImageRow(threeCards[i]);
            int col = CardsManager.getImageCol(threeCards[i]);
            Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                    CardImage.cardImages[row][col]);
            src.set(0, 0, image.getWidth(), image.getHeight());
            dst.set((int) (threeCardsPosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                    (int) (threeCardsPosition[i][1] * MainActivity.SCALE_VERTICAL),
                    (int) ((threeCardsPosition[i][0] + 40) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((threeCardsPosition[i][1] + 60) * MainActivity.SCALE_VERTICAL));
            RectF rectF = new RectF(dst);
            canvas.drawBitmap(image, src, dst, paint);
            canvas.drawRoundRect(rectF, 5, 5, paint);
        }
    }

    public void restart() {
        op = 1;
    }
    // 触屏的处理
    public void onTouch(int x, int y) {
        // 若游戏结束，则点击任意屏幕重新开始
        if (op == 1) {
            op = -1;
        }

        if (currentId == 0) {

            if (CardsManager.inRect(x, y, (int) (buttonPosition_X * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPosition_Y * MainActivity.SCALE_VERTICAL),
                    (int) (80 * MainActivity.SCALE_HORIAONTAL),
                    (int) (40 * MainActivity.SCALE_VERTICAL))) {
                System.out.println("出牌");
                ifClickChupai = true;

            }
            if (currentCircle != 0) {
                if (CardsManager.inRect(x, y,
                        (int) ((buttonPosition_X - 80) * MainActivity.SCALE_HORIAONTAL),
                        (int) (buttonPosition_Y * MainActivity.SCALE_VERTICAL),
                        (int) (80 * MainActivity.SCALE_HORIAONTAL),
                        (int) (40 * MainActivity.SCALE_VERTICAL))) {
                    System.out.println("不要");
                    buyao();
                }
            }
            if (CardsManager.inRect(x, y,
                    (int) ((buttonPosition_X + 80) * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPosition_Y * MainActivity.SCALE_VERTICAL),
                    (int) (80 * MainActivity.SCALE_HORIAONTAL),
                    (int) (40 * MainActivity.SCALE_VERTICAL))) {
                System.out.println("重选");
                this.restoreMemento(humanMC.retrieveMemento());
                //players[0].redo();
            }
            if (CardsManager.inRect(x, y,
                    (int) ((buttonPosition_X + 160) * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPosition_Y * MainActivity.SCALE_VERTICAL),
                    (int) (80 * MainActivity.SCALE_HORIAONTAL),
                    (int) (40 * MainActivity.SCALE_VERTICAL))) {
                System.out.println("提示");
                //restart();
                players[0].hintPlayer(cardsOnDesktop);
            }
        }

        Log.i("##info", "user tap screen！");
        players[0].OnTouch(x, y);
    }

    @Override
    protected void notifyObservers(Player winner, int score) {
        // 迭代器模式遍历观察者模式列表
        Iterator<Observer> iter = observers.createIterator();
        for(Observer ob = iter.first(); iter.hasNext(); ob = iter.next()) {
            ob.update(winner, score);
        }
    }


    // 备忘录模式
    // 创建新的备忘录对象
    public Memento createMemento(LastState lastState) {
        return new Memento(lastState);
    }

    //将发起人恢复到备忘录对象所记载的状态
    public void restoreMemento(Memento memento) {
        Log.i("##info", "before regret circle is " + currentCircle);
        Log.i("##info", "before regret num of pai is " + players[0].getCards().length);
        players = memento.getLastState().getLastStateOfPlayers();
        for(int i = 0; i < players.length; i++) {
            if( players[i] == null )
                Log.i("##info", "play " + i + "is null");
        }
        cardsOnDesktop = memento.getLastState().getLastCardOnDesk();
        playerCards = memento.getLastState().getPlayerCards();
        currentCircle = memento.getLastState().getCurrentCircle();
        this.currentId = memento.getLastState().getCurrentID();
        multiple = memento.getLastState().getMultiple();
        canPass = memento.getLastState().getCanPass();
        canDrawLatestCards = memento.getLastState().isCanDrawLatestCards();
        Log.i("##info", "after regret circle is " + currentCircle);
        Log.i("##info", "after regret num of pai is " + players[0].getCards().length);
    }
}
