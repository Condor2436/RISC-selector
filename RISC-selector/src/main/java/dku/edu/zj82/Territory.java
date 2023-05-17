package dku.edu.zj82;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Territory {
    private String name;
    private int ownID;
    private Units units;
    private HashMap<Integer, ArrayList<String>> neighbor;

    public Territory(String name, int ownID) {
        this.name = name;
        this.ownID = ownID;
        this.units = new Units();
        this.neighbor = new HashMap<>();
    }

    public Territory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnID() {
        return ownID;
    }

    public void setOwnID(int ownID) {
        this.ownID = ownID;
    }

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public HashMap<Integer, ArrayList<String>> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(HashMap<Integer, ArrayList<String>> neighbor) {
        this.neighbor = neighbor;
    }

    private int getIDByName(Territory t){
        for(Map.Entry<Integer, ArrayList<String>> e: neighbor.entrySet()){
            if(e.getValue().contains(t.getName())){
                return e.getKey();
            }
        }
        return -1;
    }

    public void updateNeighbor(Territory t){
        int existKey = getIDByName(t);
        if(existKey!=-1){
            ArrayList<String> modifiedList = this.neighbor.get(existKey);
            modifiedList.remove(t.getName());
            this.neighbor.put(existKey,modifiedList);
        }
        // add the name to the hashmap
        ArrayList<String> tempList;
        if (this.neighbor.containsKey(t.getOwnID())) {
            tempList = this.neighbor.get(t.getOwnID());
        } else {
            tempList = new ArrayList<>();
        }
        tempList.add(t.getName());
        this.neighbor.put(t.getOwnID(), tempList);
    }
}
