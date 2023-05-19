package dku.edu.zj82;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class GameInfo {
    private int playerID;
    private int gameID;
    private int status;
    private int totalNumOfPlayer;
    private boolean watchingPattern;
    private BehaviorList listForOneTurn;
    private ArrayList<Territory> globalMap;
    private OriginChecker originChecker;
    private SocketChannel socketChannel;
    private int gamePort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameInfo(int gamePort) {
        this.gamePort = gamePort;
        this.originChecker = new OriginChecker(null);
        this.watchingPattern = false;
        this.listForOneTurn = new BehaviorList();
        this.globalMap = new ArrayList<>();
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalNumOfPlayer() {
        return totalNumOfPlayer;
    }

    public void setTotalNumOfPlayer(int totalNumOfPlayer) {
        this.totalNumOfPlayer = totalNumOfPlayer;
    }

    public boolean isWatchingPattern() {
        return watchingPattern;
    }

    public void setWatchingPattern(boolean watchingPattern) {
        this.watchingPattern = watchingPattern;
    }

    public BehaviorList getListForOneTurn() {
        return listForOneTurn;
    }

    public void setListForOneTurn(BehaviorList listForOneTurn) {
        this.listForOneTurn = listForOneTurn;
    }

    public ArrayList<Territory> getGlobalMap() {
        return globalMap;
    }

    public void setGlobalMap(ArrayList<Territory> globalMap) {
        this.globalMap = globalMap;
    }

    public OriginChecker getOriginChecker() {
        return originChecker;
    }

    public void setOriginChecker(OriginChecker originChecker) {
        this.originChecker = originChecker;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public int getGamePort() {
        return gamePort;
    }

    public void setGamePort(int gamePort) {
        this.gamePort = gamePort;
    }

    public void connectToGame() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost",gamePort));
        //wait for connection finish
        if (!socketChannel.finishConnect()) {
            do {
            } while (!socketChannel.finishConnect());
        }
    }

    public void initialGlobalMapInGame(String input) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        TransInfo transInfo = new TransInfo();
        transInfo.setInfo(input);
        transInfo.setType("initialize map");
        String sendString = objectMapper.writeValueAsString(transInfo);
        System.out.println(sendString);
        buffer.clear();
        buffer.put(sendString.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }
    private Territory getTerritoryByName(String name){
        for(Territory t: globalMap){
            if(t.getName().equalsIgnoreCase(name)){
                return t;
            }
        }
        return null;
    }
    public void addMoveAction(String input){
        String[] tokens = input.split(" ");// unit origin destination
        int unitNum = -1;
        try{
            unitNum = Integer.parseInt(tokens[0]);
        } catch (NumberFormatException e){
            return;
        }
        if(unitNum<0||getTerritoryByName(tokens[1])==null||getTerritoryByName(tokens[2])==null){
            return;
        }
        Behavior addedMove = new Behavior(getTerritoryByName(tokens[1]),getTerritoryByName(tokens[2]),unitNum,this.playerID,"move");
        this.listForOneTurn.addToMoveList(addedMove);
    }
    public void addAttackAction(String input){
        String[] tokens = input.split(" ");// unit origin destination
        int unitNum = -1;
        try{
            unitNum = Integer.parseInt(tokens[0]);
        } catch (NumberFormatException e){
            return;
        }
        if(unitNum<0||getTerritoryByName(tokens[1])==null||getTerritoryByName(tokens[2])==null){
            return;
        }
        Behavior addedMove = new Behavior(getTerritoryByName(tokens[1]),getTerritoryByName(tokens[2]),unitNum,this.playerID,"attack");
        this.listForOneTurn.addToAttackList(addedMove);
    }
    public void sendListForOneTurn() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        String info = objectMapper.writeValueAsString(listForOneTurn);
        TransInfo transInfo = new TransInfo();
        transInfo.setInfo(info);
        transInfo.setType("list for one turn");
        String sendString = objectMapper.writeValueAsString(transInfo);
        System.out.println(sendString);
        buffer.clear();
        buffer.put(sendString.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        this.listForOneTurn = new BehaviorList();
    }
    // TODO: 2023/5/19 generate test for gameInfo
}
