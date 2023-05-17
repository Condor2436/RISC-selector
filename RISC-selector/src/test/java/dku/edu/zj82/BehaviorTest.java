package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BehaviorTest {

    @Test
    void getOrigin() {
        Territory t1 = new Territory("PlaceA", 0);
        Behavior b = new Behavior();
        b.setOrigin(t1);
        assertEquals(t1,b.getOrigin());
        Territory t2 = new Territory("PlaceB", 1);
        Behavior b1 = new Behavior(t1,t2,10,0,"Attack");
        assertEquals(t1,b1.getOrigin());
    }

    @Test
    void getDestination() {
        Territory t1 = new Territory("PlaceA", 0);
        Territory t2 = new Territory("PlaceB", 1);
        Behavior b1 = new Behavior(t1,t2,10,0,"Attack");
        assertEquals(t2,b1.getDestination());
        Behavior b = new Behavior();
        b.setDestination(t1);
        assertEquals(t1,b.getDestination());
    }

    @Test
    void getUnits() {
        Territory t1 = new Territory("PlaceA", 0);
        Territory t2 = new Territory("PlaceB", 1);
        Behavior b1 = new Behavior(t1,t2,10,0,"Attack");
        assertEquals(10,b1.getUnits().getNums());
        Behavior b = new Behavior();
        Units u = new Units(10);
        b.setUnits(u);
        assertEquals(u,b.getUnits());
        Behavior b2 = new Behavior();
        b2.setUnits(15);
        assertEquals(15,b2.getUnits().getNums());
    }

    @Test
    void getOwnID() {
        Behavior b = new Behavior();
        b.setOwnID(10);
        assertEquals(10,b.getOwnID());
    }

    @Test
    void getType() {
        Behavior b = new Behavior();
        b.setType("Attack");
        assertEquals("Attack",b.getType());
    }
}