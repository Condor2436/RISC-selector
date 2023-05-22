package dku.edu.zj82;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private Map<Integer, GameInfo> gameInfoMap;
    private ArrayList<Integer> joinGameList;
    private ArrayList<Integer> gamePortList;
    private int currentGame;
    private int userID;
    private ArrayList<Integer> availableList;
    private String userName;
    public Player(int id, String name){
        this.userName = name;
        this.userID = id;
        this.availableList = new ArrayList<>();
        this.currentGame = -1;
        this.joinGameList = new ArrayList<>();
        this.gamePortList = new ArrayList<>();
        this.gameInfoMap = new HashMap<>();
    }

    public Player() {
    }

    public ArrayList<Integer> getGamePortList() {
        return gamePortList;
    }

    public void setGamePortList(ArrayList<Integer> gamePortList) {
        this.gamePortList = gamePortList;
    }

    public ArrayList<Integer> getAvailableList() {
        return availableList;
    }

    public void setAvailableList(ArrayList<Integer> availableList) {
        this.availableList = availableList;
    }

    public Map<Integer, GameInfo> getGameInfoMap() {
        return gameInfoMap;
    }

    public ArrayList<Integer> getJoinGameList() {
        return joinGameList;
    }

    public int getCurrentGame() {
        return currentGame;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setGameInfoMap(Map<Integer, GameInfo> gameInfoMap) {
        this.gameInfoMap = gameInfoMap;
    }

    public void setJoinGameList(ArrayList<Integer> joinGameList) {
        this.joinGameList = joinGameList;
    }

    public void setCurrentGame(int currentGame) {
        this.currentGame = currentGame;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void createGameInfo(GameInfo gameInfo) throws IOException {
        gameInfoMap.put(gameInfo.getGameID()-10000,gameInfo);
        gameInfoMap.get(gameInfo.getGameID()-10000).connectToGame();
    }
    public void changeGameStatus(int status){
        gameInfoMap.get(currentGame).setStatus(status);
    }

    public void changeCurrentGame(int gameID) {
        this.currentGame = gameID;
    }
    public void disconnectFromGame(int gameID){
        gameInfoMap.remove(gameID);
        joinGameList.remove(gameID);
    }

    // TODO: 2023/5/17 add unit test to all setter and getter, this should be done when github copilot is available

}
