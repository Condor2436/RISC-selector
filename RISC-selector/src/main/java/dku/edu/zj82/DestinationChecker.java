package dku.edu.zj82;

import java.util.ArrayList;
import java.util.Map;

public class DestinationChecker extends BasicChecker{
    public DestinationChecker(BasicChecker next) {
        super(next);
    }

    private Territory findTerritory(String name, ArrayList<Territory> globalMap){
        for(Territory t: globalMap){
            if(t.getName().equalsIgnoreCase(name)){
                return t;
            }
        }
        return null;
    }

    private boolean findPath(Territory origin, String destination, ArrayList<Territory> globalMap, ArrayList<String> visited){
        String departureName = origin.getName();
        visited.add(departureName);
        for(Map.Entry<Integer,ArrayList<String>> e: origin.getNeighbor().entrySet()){
            if(e.getKey()==origin.getOwnID()){
                ArrayList<String> neighbor = e.getValue();
                for(String s: neighbor){
                    if(visited.contains(s)){
                        continue;
                    }
                    if(s.equalsIgnoreCase(destination)){
                        return true;
                    } else {
                        Territory nextTerritory = findTerritory(s,globalMap);
                        if (nextTerritory != null && findPath(nextTerritory, destination, globalMap, visited)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    public String checkMyRule(Behavior myBehavior, ArrayList<Territory> globalMap) {
        String type = myBehavior.getType();
        int starterID = myBehavior.getOwnID();

        if (type.equalsIgnoreCase("attack")) {
            int enemyID = myBehavior.getDestination().getOwnID();
            if (starterID == enemyID) {
                return "you are attacking you own place!";
            }
            //check if neighbor adjacent to it
            boolean adjacent = false;
            String enemyName = myBehavior.getDestination().getName();
            if (myBehavior.getOrigin().getNeighbor().containsKey(enemyID)) {
                for (int i = 0; i < myBehavior.getOrigin().getNeighbor().get(enemyID).size(); i++) {
                    if (enemyName.equals(myBehavior.getOrigin().getNeighbor().get(enemyID).get(i))) {
                        adjacent = true;
                        break;
                    }
                }
            }
            if (!adjacent) {
                return "Enemy not adjacent to you";
            }

        } else if (type.equalsIgnoreCase("move")) {
            int behavior_DestinationID = myBehavior.getDestination().getOwnID();
            if (starterID != behavior_DestinationID) {
                return "you are moving to a place which not belongs to you!";
            }
            //find path
            ArrayList<String> visited = new ArrayList<>();
            boolean res = findPath(myBehavior.getOrigin(), myBehavior.getDestination().getName(), globalMap, visited);
            if (!res) {
                return "No path to the destination";
            }
        }


        return null;
    }
}
