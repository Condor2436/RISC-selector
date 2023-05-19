package dku.edu.zj82;

import java.util.ArrayList;

public class BehaviorList {
    private ArrayList<Behavior> moveList;
    private ArrayList<Behavior> attackList;
    private int playID;
    private int status; // -1 means disconnect; 0 means dead; 1 means live; 2 means win

    public BehaviorList(int playID, int status) {
        this.playID = playID;
        this.status = status;
        this.moveList = new ArrayList<>();
        this.attackList = new ArrayList<>();
    }

    public BehaviorList() {
        this.moveList = new ArrayList<>();
        this.attackList = new ArrayList<>();
    }

    public ArrayList<Behavior> getMoveList() {
        return moveList;
    }

    public ArrayList<Behavior> getAttackList() {
        return attackList;
    }

    public void addToMoveList(Behavior b){
        this.moveList.add(b);
    }
    public void addToAttackList(Behavior b){
        this.attackList.add(b);
    }
    public int getPlayID() {
        return playID;
    }

    public int getStatus() {
        return status;
    }
}
