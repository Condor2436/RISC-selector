package dku.edu.zj82;

public class Behavior {
    private Territory origin;
    private Territory destination;
    private Units units;
    private int ownID;
    private String type;

    public Behavior(Territory origin, Territory destination, int num, int ownID, String type) {
        this.origin = origin;
        this.destination = destination;
        this.units = new Units(num);
        this.ownID = ownID;
        this.type = type;
    }

    public Behavior() {
    }

    public Territory getOrigin() {
        return origin;
    }

    public void setOrigin(Territory origin) {
        this.origin = origin;
    }

    public Territory getDestination() {
        return destination;
    }

    public void setDestination(Territory destination) {
        this.destination = destination;
    }

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }
    public void setUnits(int num) {
        this.units = new Units(num);
    }
    public int getOwnID() {
        return ownID;
    }

    public void setOwnID(int ownID) {
        this.ownID = ownID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
