package com.wangxin.chinesepoker.Memento;

import android.util.Log;

import com.wangxin.chinesepoker.Card.CardsHolder;
import com.wangxin.chinesepoker.Player.ComputerPlayer;
import com.wangxin.chinesepoker.Player.HumanPlayer;
import com.wangxin.chinesepoker.Player.Player;
import com.wangxin.chinesepoker.Tools.CopyProperty;

/**
 * Created by wangxin on 2017/4/14.
 */

public class LastState {
    private Player[] lastStateOfPlayers;
    private CardsHolder lastCardOnDesk;
    private int[][] playerCards;
    private int currentCircle = 0;// 本轮次数
    private int currentID = 0;
    public int multiple = 1;// 当前倍数
    private boolean[] canPass = new boolean[3];
    private boolean canDrawLatestCards;

    public LastState(Player[] lastStateOfPlayers, CardsHolder lastCardOnDesk,
                     int[][] playerCards, int currentCircle, int currentID,
                     int multiple, boolean[] canPass, boolean canDrawLatestCards) {
        this.lastStateOfPlayers = new Player[lastStateOfPlayers.length];
        for(int i = 0; i < lastStateOfPlayers.length; i++) {
            if(lastStateOfPlayers[i].getClass() == HumanPlayer.class) {
                this.lastStateOfPlayers[i] = new HumanPlayer(lastStateOfPlayers[i].getCards(), lastStateOfPlayers[i].getLeft(),
                        lastStateOfPlayers[i].getTop(), lastStateOfPlayers[i].getPaintDirection(), lastStateOfPlayers[i].getPlayID(),
                        lastStateOfPlayers[i].getDesk(), lastStateOfPlayers[i].getContext());
                Log.i("##info", "copy HumanCopy!");
            }
            else {
                this.lastStateOfPlayers[i] = new ComputerPlayer(lastStateOfPlayers[i].getCards(), lastStateOfPlayers[i].getLeft(),
                        lastStateOfPlayers[i].getTop(), lastStateOfPlayers[i].getPaintDirection(), lastStateOfPlayers[i].getPlayID(),
                        lastStateOfPlayers[i].getDesk(), lastStateOfPlayers[i].getContext());
                Log.i("##info", "copy ComputerCopy!");
            }
            try {
                CopyProperty.copyProperty(this.lastStateOfPlayers[i], lastStateOfPlayers[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < lastStateOfPlayers.length; i++) {
            // 设置玩家上下关系
            this.lastStateOfPlayers[i].setLastAndNext(this.lastStateOfPlayers[lastStateOfPlayers[i].getLast().getPlayID()],
                    this.lastStateOfPlayers[lastStateOfPlayers[i].getNext().getPlayID()]);
        }

        if(lastCardOnDesk != null)
            this.lastCardOnDesk = new CardsHolder(lastCardOnDesk.getCards(), lastCardOnDesk.getPlayID(), lastCardOnDesk.getContext());
        else
            this.lastCardOnDesk = null;

        this.playerCards = new int[playerCards.length][];
        for(int i = 0; i < playerCards.length; i++) {
            this.playerCards[i] = new int[playerCards[i].length];
            for(int j = 0; j < playerCards[i].length; j++) {
                this.playerCards[i][j] = playerCards[i][j];
            }
        }

        this.currentCircle = currentCircle;
        this.currentID = currentID;
        this.multiple = multiple;
        this.canPass = canPass;
        this.canDrawLatestCards = canDrawLatestCards;
    }

    public Player[] getLastStateOfPlayers() {
        return lastStateOfPlayers;
    }

    public void setLastStateOfPlayers(Player[] lastStateOfPlayers) {
        this.lastStateOfPlayers = lastStateOfPlayers;
    }

    public CardsHolder getLastCardOnDesk() {
        return lastCardOnDesk;
    }

    public void setLastCardOnDesk(CardsHolder lastCardOnDesk) {
        this.lastCardOnDesk = lastCardOnDesk;
    }

    public int[][] getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(int[][] playerCards) {
        this.playerCards = playerCards;
    }

    public int getCurrentCircle() {
        return currentCircle;
    }

    public void setCurrentCircle(int currentCircle) {
        this.currentCircle = currentCircle;
    }

    public int getCurrentID() {
        return currentID;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public boolean[] getCanPass() {
        return canPass;
    }

    public void setCanPass(boolean[] canPass) {
        this.canPass = canPass;
    }

    public boolean isCanDrawLatestCards() {
        return canDrawLatestCards;
    }

    public void setCanDrawLatestCards(boolean canDrawLatestCards) {
        this.canDrawLatestCards = canDrawLatestCards;
    }
}
