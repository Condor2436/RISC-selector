package dku.edu.zj82;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import static java.lang.Thread.sleep;

public class Server {
    private Map<String, Player> playerList;// userName + player object
    private Map<String, String> passWord; // userName + password
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int port;
    private ArrayList<Integer> gamePort;
    private ArrayList<Integer> freeGamePortList;// index for game port
    private Map<Integer, Game> availableGame;
    private Map<Integer, SocketChannel> gameChannel;// id + channel
    private Map<Integer, String> playerIDToName;
    private final int uid = 100;

    public Server(int port) {
        this.playerList = new HashMap<>();
        this.passWord = new HashMap<>();
        this.port = port;
        gamePort = new ArrayList<>();
        freeGamePortList = new ArrayList<>();
        availableGame = new HashMap<>();
        gameChannel = new HashMap<>();
        playerIDToName = new HashMap<>();
        gamePortAndFreeInit();
    }

    private void gamePortAndFreeInit() {
        for (int i = 0; i < 10; i++) {
            gamePort.add(8081 + i);
            freeGamePortList.add(i);
        }
    }

    private void registerNewPlayer(String userName, String passWD) {
        int nextID = uid + playerList.size();
        Player p = new Player(nextID, userName);
        playerList.put(userName, p);
        passWord.put(userName, passWD);
        playerIDToName.put(nextID, userName);
    }

    private void createNewGame(int maxPlayerNum) {
        Game g = new Game(gamePort.get(freeGamePortList.get(0)), maxPlayerNum, freeGamePortList.get(0), port);
        availableGame.put(freeGamePortList.get(0), g);
        freeGamePortList.remove(0);
        new Thread(g).start();
    }

    private void closeGame(int gameID) {
        availableGame.remove(gameID);
        freeGamePortList.add(gameID);
    }

    private void updatePlayer(Map<Integer, Player> playerMap) {
        for (Map.Entry<Integer, Player> e : playerMap.entrySet()) {
            playerList.get(playerIDToName.get(e.getKey())).setCurrentGame(e.getValue().getCurrentGame());
            playerList.get(playerIDToName.get(e.getKey())).setGameInfoMap(e.getValue().getGameInfoMap());
            playerList.get(playerIDToName.get(e.getKey())).setJoinGameList(e.getValue().getJoinGameList());
        }
    }

    private void start() throws IOException, InterruptedException {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress("localhost", port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    String message = "Do you want to register a new user? (yes/no)";
                    TransInfo reply = new TransInfo();
                    reply.setType("Accept");
                    reply.setInfo(message);
                    message = objectMapper.writeValueAsString(reply);
                    ByteBuffer messageBuffer = ByteBuffer.wrap(message.getBytes());
                    client.write(messageBuffer);
                    messageBuffer.clear();
                }
                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer dataBuffer = ByteBuffer.allocate(4096);
                    client.read(dataBuffer);
                    dataBuffer.flip();
                    String data = new String(dataBuffer.array()).trim();
                    TransInfo curr = objectMapper.readValue(data, TransInfo.class);
                    System.out.println("Received data from client: " + curr.getType() + ": " + curr.getInfo());

                    TransInfo reply = new TransInfo();
                    String response = "";

                    // communicate with client
                    if (curr.getType().equalsIgnoreCase("register")) {
                        String[] token = curr.getInfo().split(",");
                        String userName = token[0];
                        String passWD = token[1];
                        if (passWord.containsKey(userName)) {
                            reply.setType("registerFailure");
                            response = "User name already exist, check your input";
                        } else {
                            reply.setType("registerSuccess");
                            registerNewPlayer(userName, passWD);
                            response = "Register Success";
                        }
                    }
                    if (curr.getType().equalsIgnoreCase("login")) {
                        String[] token = curr.getInfo().split(",");
                        String userName = token[0];
                        String passWD = token[1];
                        if (!passWord.containsKey(userName)) {
                            reply.setType("loginFailure");
                            response = "Player does not exist";
                        } else {
                            if (!passWD.equals(passWord.get(userName))) {
                                reply.setType("loginFailure");
                                response = "Wrong password";
                            } else {
                                reply.setType("loginSuccess");
                                response = objectMapper.writeValueAsString(playerList.get(userName));
                            }
                        }
                    }
                    if (curr.getType().equalsIgnoreCase("stop")) {
                        key.cancel();
                        reply.setType("disconnect");
                    }
                    if (curr.getType().equalsIgnoreCase("get game list")) {
                        reply.setType("game list");
                        StringBuilder sb = new StringBuilder();
                        if (availableGame.isEmpty()) {
                            sb.append("No game is available now, do you want to create a game?");
                        } else {
                            sb.append("Currently, here is available game list: ").append(System.lineSeparator());
                            for (Map.Entry<Integer, Game> e : availableGame.entrySet()) {
                                if (e.getValue().isFull()) {
                                    sb.append("Game ").append(e.getKey()).append(System.lineSeparator());
                                }
                            }
                            response = sb.toString();
                        }
                    }

                    // communicate with game and client
                    if (curr.getType().equalsIgnoreCase("join game")) {// gameID, playerName
                        TransInfo playerInfo = new TransInfo("player info", objectMapper.writeValueAsString(playerList.get(curr.getMuliInfo()[1])));
                        gameChannel.get(curr.getMuliInfo()[0]).write(ByteBuffer.wrap((objectMapper.writeValueAsBytes(playerInfo))));
                        reply.setType("game info");
                        response = objectMapper.writeValueAsString(gamePort.get(Integer.parseInt(curr.getMuliInfo()[0])));
                    }
                    if (curr.getType().equalsIgnoreCase("create game")) {// maxPlayerNum, playerName
                        int portForGame = gamePort.get(freeGamePortList.get(0));
                        createNewGame(Integer.parseInt(curr.getMuliInfo()[0]));
                        sleep(10000);
                        TransInfo playerInfo = new TransInfo("player info", objectMapper.writeValueAsString(playerList.get(curr.getMuliInfo()[1])));
                        gameChannel.get(portForGame).write(ByteBuffer.wrap((objectMapper.writeValueAsBytes(playerInfo))));
                        reply.setType("game info");
                        response = objectMapper.writeValueAsString(portForGame);
                    }

                    // communicate with game
                    if (curr.getType().equalsIgnoreCase("game connect to server")) {
                        gameChannel.put(Integer.parseInt(curr.getInfo()), client);
                        continue;
                    }

                    if (curr.getType().equalsIgnoreCase("update player")) {
                        Map<Integer, Player> playerMap = objectMapper.readValue(curr.getInfo(), Map.class);
                        updatePlayer(playerMap);
                        continue;
                    }
                    if (curr.getType().equalsIgnoreCase("game end")) {
                        gameChannel.remove(Integer.parseInt(curr.getInfo()));
                        freeGamePortList.add(Integer.parseInt(curr.getInfo()));
                        availableGame.remove(Integer.parseInt(curr.getInfo()));
                        continue;
                    }


                    reply.setInfo(response);
                    String sendInfo = objectMapper.writeValueAsString(reply);
                    ByteBuffer responseDataBuffer = ByteBuffer.allocate(4096);
                    responseDataBuffer.clear();
                    responseDataBuffer.put(sendInfo.getBytes());
                    responseDataBuffer.flip();
                    client.write(responseDataBuffer);
                }
            }
            if (System.in.available() > 0) {
                String command = scanner.nextLine();
                if (command.equals("quit")) {
                    break;
                } else {
                    System.out.println("Your input is " + command);
                }
            }
        }
    }
}
