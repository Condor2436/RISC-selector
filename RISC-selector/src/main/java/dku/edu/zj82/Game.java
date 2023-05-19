package dku.edu.zj82;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Game implements Runnable {
    private HashMap<Integer, SocketChannel> playerChannelList;// id channel
    private HashMap<Integer, Player> playerList;// uid, player
    private HashMap<Integer, Integer> IDMap;// id to uid
    private ArrayList<Territory> map;
    private int port;
    private ServerSocketChannel thisChannel;
    private int maxPlayerNum;
    private ObjectMapper objectMapper;
    private BasicChecker ruleChecker;
    private int gameID;
    private Selector selector;
    private int initPlayerID = 100000001;
    private SocketChannel toServer;
    private int serverPort;
    private final int START_UNIT = 50;
    private ArrayList<Behavior> attackList = new ArrayList<>();
    private ArrayList<Behavior> moveList = new ArrayList<>();

    public Game(int port, int maxPlayerNum, int gameID, int serverPort) {
        this.playerChannelList = new HashMap<>();
        this.IDMap = new HashMap<>();
        this.map = new ArrayList<>();
        this.port = port;
        this.maxPlayerNum = maxPlayerNum;
        ruleChecker = new OriginChecker(new DestinationChecker(null));
        this.gameID = gameID;
        this.objectMapper = new ObjectMapper();
        this.serverPort = serverPort;
        this.playerList = new HashMap<>();
    }

    private void acceptPlayer(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = null;
        client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        playerChannelList.put(initPlayerID, client);
        String message = "Welcome to RISC, your player id is " + initPlayerID++ + " total number of players is " + maxPlayerNum;
        TransInfo reply = new TransInfo();
        reply.setType("Welcome");
        reply.setInfo(message);
        message = objectMapper.writeValueAsString(reply);
        ByteBuffer messageBuffer = ByteBuffer.wrap(message.getBytes());
        client.write(messageBuffer);
        messageBuffer.clear();
    }

    private void connectToServer() throws IOException {
        toServer = SocketChannel.open();
        toServer.configureBlocking(false);
        toServer.connect(new InetSocketAddress("localhost", serverPort));
        while (!toServer.finishConnect()) {
            System.out.println("Wait to connect to server");
        }
        System.out.println("Connected to server: " + toServer.getRemoteAddress());
    }

    private void getPlayerFromServer(String playerInfo) throws JsonProcessingException {
        Player p = objectMapper.readValue(playerInfo, Player.class);
        playerList.put(p.getUserID(), p);
    }

    private void openChannelToPlayer(Selector selector) throws IOException {
        thisChannel = ServerSocketChannel.open();
        thisChannel.socket().bind(new InetSocketAddress("localhost", port));
        thisChannel.configureBlocking(false);
        thisChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void updateIDMap(String IDInfo) {
        String token[] = IDInfo.split(" ");// uid id
        IDMap.put(Integer.parseInt(token[0]), Integer.parseInt(token[1]));
    }

    private void updatePlayerToServer() throws IOException {
        TransInfo info = new TransInfo();
        info.setType("update player");
        String message = objectMapper.writeValueAsString(playerList);
        info.setInfo(message);
        message = objectMapper.writeValueAsString(info);
        ByteBuffer messageBuffer = ByteBuffer.wrap(message.getBytes());
        toServer.write(messageBuffer);
        messageBuffer.clear();
    }

    private void unitNatureIncrease() {
        for (Territory territory : map) {
            territory.getUnits().addUnit();
        }
    }

    private void initMap() {
        map.add(new Territory("Narnia", -1));
        map.add(new Territory("Midkemia", -1));
        map.add(new Territory("Oz", -1));
        map.add(new Territory("Gondor", -1));
        map.add(new Territory("Elantris", -1));
        map.add(new Territory("Scadrial", -1));
        map.add(new Territory("Roshar", -1));
        map.add(new Territory("Hogwarts", -1));
        if (maxPlayerNum != 3 && maxPlayerNum != 4) {
            map.add(new Territory("Mordor", -1));
        }
        if (maxPlayerNum != 4) {
            map.add(new Territory("Duke", -1));
        }

        int[] territoryOwner;
        if (maxPlayerNum == 2) {
            territoryOwner = new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
        } else if (maxPlayerNum == 3) {
            territoryOwner = new int[]{2, 1, 1, 2, 2, 1, 3, 3, 3};
        } else if (maxPlayerNum == 4) {
            territoryOwner = new int[]{1, 1, 2, 2, 3, 3, 4, 4};
        } else {
            territoryOwner = new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        }

        for (int i = 0; i < map.size(); i++) {
            map.get(i).setOwnID(territoryOwner[i]);
        }
        initMapHelper();
    }

    private void initMapHelper() {
        map.get(0).updateNeighbor(map.get(1));
        map.get(0).updateNeighbor(map.get(3));

        map.get(1).updateNeighbor(map.get(0));
        map.get(1).updateNeighbor(map.get(2));
        map.get(1).updateNeighbor(map.get(4));

        map.get(2).updateNeighbor(map.get(1));
        map.get(2).updateNeighbor(map.get(5));

        map.get(3).updateNeighbor(map.get(0));
        map.get(3).updateNeighbor(map.get(4));
        map.get(3).updateNeighbor(map.get(6));
        map.get(3).updateNeighbor(map.get(7));

        map.get(4).updateNeighbor(map.get(1));
        map.get(4).updateNeighbor(map.get(3));
        map.get(4).updateNeighbor(map.get(5));
        map.get(4).updateNeighbor(map.get(7));
        if (maxPlayerNum != 4) {
            map.get(4).updateNeighbor(map.get(8));
        }


        map.get(5).updateNeighbor(map.get(2));
        map.get(5).updateNeighbor(map.get(4));
        if (maxPlayerNum != 4) {
            map.get(5).updateNeighbor(map.get(8));
        }

        if (maxPlayerNum != 3 && maxPlayerNum != 4) {
            map.get(5).updateNeighbor(map.get(9));

        }

        map.get(6).updateNeighbor(map.get(3));
        map.get(6).updateNeighbor(map.get(7));


        map.get(7).updateNeighbor(map.get(3));
        map.get(7).updateNeighbor(map.get(4));
        map.get(7).updateNeighbor(map.get(6));
        if (maxPlayerNum != 4) {
            map.get(7).updateNeighbor(map.get(8));
        }

        if (maxPlayerNum != 4) {
            map.get(8).updateNeighbor(map.get(4));
            map.get(8).updateNeighbor(map.get(5));
            map.get(8).updateNeighbor(map.get(7));
            if (maxPlayerNum != 3) {
                map.get(8).updateNeighbor(map.get(9));
            }
        }


        if (maxPlayerNum != 3 && maxPlayerNum != 4) {
            map.get(9).updateNeighbor(map.get(5));
            map.get(9).updateNeighbor(map.get(8));
        }

    }

    private void refreshTurnMapAndAL() {
        this.attackList = new ArrayList<>();
        this.moveList = new ArrayList<>();
    }

    private void turnStartMessageDelivery() throws IOException {
        for (Map.Entry<Integer, SocketChannel> e : playerChannelList.entrySet()) {
            TransInfo transInfo = new TransInfo();
            transInfo.setType("turn start update");
            String mapInfo = objectMapper.writeValueAsString(map);
            String playerInfo = objectMapper.writeValueAsString(playerList.get(IDMap.get(e.getKey())));
            StringBuilder sb = new StringBuilder();
            sb.append(mapInfo).append(" ").append(playerInfo);
            String info[] = sb.toString().split(" ");
            transInfo.setMuliInfo(info);
            String message = objectMapper.writeValueAsString(transInfo);
            ByteBuffer messageBuffer = ByteBuffer.wrap(message.getBytes());
            e.getValue().write(messageBuffer);
            messageBuffer.clear();
        }
    }

    private void handleReceivedBehaviorList(String behaviorList) throws JsonProcessingException {
        BehaviorList current = objectMapper.readValue(behaviorList, BehaviorList.class);
        this.moveList.addAll(current.getMoveList());
        this.attackList.addAll(current.getAttackList());
    }

    private void checkAndExecuteBehavior() {
        checkAndExecuteMoveBehavior(moveList);
        checkAndExecuteAttackBehavior(attackList);
    }

    private void checkAndExecuteMoveBehavior(ArrayList<Behavior> behaviorArrayList) {  // checkandupdate
        for (Behavior b : behaviorArrayList) {
            if (ruleChecker.checkMyRule(b, map) == null) {
                executeMoveBehavior(b);
            } else {
                System.out.println("Move fail because " + ruleChecker.checkMyRule(b, map));
            }
        }
    }

    private void executeMoveBehavior(Behavior b) {
        String sourceName = b.getOrigin().getName();
        String destName = b.getDestination().getName();
        Units units = b.getUnits();
        for (Territory value : map) {
            if (sourceName.equals(value.getName())) {
                value.getUnits().reduceUnit(units);
                break;
            }
        }
        for (Territory territory : map) {
            if (destName.equals(territory.getName())) {
                territory.getUnits().addUnit(units);
                break;
            }
        }
        System.out.println("Move " + units.toString() + " from " + sourceName + " to " + destName);
    }

    private void checkAndExecuteAttackBehavior(ArrayList<Behavior> behaviorArrayList) {
        Random rand = new Random();
        ArrayList<Integer> playerIDs = new ArrayList<>();
        for (Behavior b : behaviorArrayList) {
            if (ruleChecker.checkMyRule(b, map) == null) {
                moveUnitCuzAttack(b);
                if (!playerIDs.contains(b.getOwnID())) {
                    playerIDs.add(b.getOwnID());
                }
            } else {
                System.out.println("Attack denied because: " + ruleChecker.checkMyRule(b, map));
                behaviorArrayList.remove(b);
                if (behaviorArrayList.isEmpty()) {
                    break;
                }
            }
        }
        List<Integer> orders = new ArrayList<>();
        for (int i = 1; i <= playerIDs.size(); i++) {
            orders.add(i);
        }
        Map<Integer, Map<String, Units>> unitMap = new HashMap<>();//playerID, destname, units
        for (Behavior b : behaviorArrayList) {
            if (unitMap.containsKey(b.getOwnID())) {
                if (unitMap.get(b.getOwnID()).containsKey(b.getDestination().getName())) {
                    unitMap.get(b.getOwnID()).get(b.getDestination().getName()).addUnit(b.getUnits());
                } else {
                    unitMap.get(b.getOwnID()).put(b.getDestination().getName(), b.getUnits());
                }
            } else {
                unitMap.put(b.getOwnID(), new HashMap<>());
                unitMap.get(b.getOwnID()).put(b.getDestination().getName(), b.getUnits());
            }
        }
        ArrayList<Behavior> mergedBehaviorList = new ArrayList<>();
        Map<Integer, Integer> givenOrder = new HashMap<>();//order, playerID
        for (Integer i : playerIDs) {
            int index = rand.nextInt(orders.size());
            givenOrder.put(orders.get(index), i);
            orders.remove(index);
        }
        for (int i = 1; i <= playerIDs.size(); i++) {
            Map<String, Units> curr = unitMap.get(givenOrder.get(i));
            for (Map.Entry<String, Units> e : curr.entrySet()) {
                Behavior addedBehavior = new Behavior(null, getTerritoryByName(e.getKey(), map), e.getValue().getNums(), givenOrder.get(i), "Attack");
                mergedBehaviorList.add(addedBehavior);
            }
        }
        for (Behavior b : mergedBehaviorList) {
            executeAttackBehavior(b);
        }
    }

    private Territory getTerritoryByName(String name, ArrayList<Territory> map) {
        for (Territory t : map) {
            if (name.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    private void moveUnitCuzAttack(Behavior b) {
        String sourceName = b.getOrigin().getName();
        Units units = b.getUnits();
//        System.out.println("Player" + b.getOwnID() + " uses " + units.getRemainUnits() + " foods to attack");
        for (Territory territory : map) {
            if (sourceName.equals(territory.getName())) {
                territory.getUnits().reduceUnit(units);
                break;
            }
        }
    }

    public void executeAttackBehavior(Behavior b) {
        String destName = b.getDestination().getName();
        Units units = b.getUnits();
        System.out.println("Attack " + destName + " using " + units.toString() + " units.");
        for (Territory territory : map) {
            if (destName.equalsIgnoreCase(territory.getName())) {
                int sig = 0;
                while (units.getNums() != 0 && territory.getUnits().getNums() != 0) {
//                    sig++;
//                    int attackerBonus = 0;
//                    int defenderBonus = 0;
//                    if (sig % 2 == 1) {
//                        attackerBonus = units.getHighestLevel();
//                        defenderBonus = territory.getUnits().getLowestLevel();
//                    } else {
//                        attackerBonus = units.getLowestLevel();
//                        defenderBonus = territory.getUnits().getHighestLevel();
//                    }

//                    if (getAttackResult(attackerBonus, defenderBonus)) {
//                        territory.getUnits().removeUnits(1, defenderBonus);
//                    } else {
//                        units.removeUnits(1, attackerBonus);
//                    }
                    if (getAttackResult()) {
                        territory.getUnits().reduceUnit(new Units(1));
                    } else {
                        units.reduceUnit(new Units(1));
                    }
                }
                if (units.getNums() != 0) {
                    System.out.println("Attack Success, remain " + units.toString() + " units");
                    territory.setOwnID(b.getOwnID());
                    territory.getUnits().setNums(units.getNums());
                    updateAllNeighbor(territory);
                } else {
                    System.out.println("Attack fail, remain " + territory.getUnits().toString() + " units");
                }
                break;
            }
        }
    }
    private void updateAllNeighbor(Territory t) {
        for (Territory territory : map) {
            for (Map.Entry<Integer, ArrayList<String>> e : territory.getNeighbor().entrySet()) {
                if (e.getValue().contains(t.getName())) {
                    territory.updateNeighbor(t);
                    break;
                }
            }
        }
    }

    private boolean getAttackResult() {
        Random rand = new Random();
        int attacker = rand.nextInt(20) + 1;
        int defender = rand.nextInt(20) + 1;
        return attacker > defender;
    }

    private void endGame() throws IOException {
        this.thisChannel.close();
    }
    @Override
    public void run() {
// TODO: 2023/5/19 main game logic
    }
}
