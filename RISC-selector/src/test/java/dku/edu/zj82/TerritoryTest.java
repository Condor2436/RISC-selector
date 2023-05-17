package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void getName() {
        Territory t = new Territory("PlaceA",0);
        assertEquals("PlaceA",t.getName());
        assertNotEquals("",t.getName());
    }

    @Test
    void getOwnID() {
        Territory t = new Territory("PlaceA",0);
        assertEquals(0,t.getOwnID());
        assertNotEquals(-1,t.getOwnID());
    }

    @Test
    void getAndSetUnits() {
        Territory t = new Territory("PlaceA",0);
        Units u = new Units(15);
        t.setUnits(u);
        assertEquals(15,t.getUnits().getNums());
        assertNotEquals(0,t.getUnits().getNums());
    }

    @Test
    void getAndSetNeighbor() {
        Territory t = new Territory("PlaceA",0);
        ArrayList<String> al1 = new ArrayList<>();
        al1.add("PlaceB");
        al1.add("PlaceC");
        ArrayList<String> al2 = new ArrayList<>();
        al2.add("PlaceD");
        al2.add("PlaceE");
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        map.put(1,al1);
        map.put(2,al2);
        t.setNeighbor(map);
        assertTrue(t.getNeighbor().get(1).contains("PlaceB"));
        assertTrue(t.getNeighbor().get(1).contains("PlaceC"));
        assertTrue(t.getNeighbor().get(2).contains("PlaceD"));
        assertTrue(t.getNeighbor().get(2).contains("PlaceE"));
    }

    @Test
    void updateNeighbor() {
        Territory t = new Territory("PlaceA",0);
        Territory t1 = new Territory("PlaceB",1);
        Territory t2= new Territory("PlaceC",1);
        Territory t3 = new Territory("PlaceD",2);
        Territory t4 = new Territory("PlaceE",2);
        t.updateNeighbor(t1);
        t.updateNeighbor(t2);
        t.updateNeighbor(t3);
        t.updateNeighbor(t4);
        assertTrue(t.getNeighbor().get(1).contains("PlaceB"));
        assertTrue(t.getNeighbor().get(1).contains("PlaceC"));
        assertTrue(t.getNeighbor().get(2).contains("PlaceD"));
        assertTrue(t.getNeighbor().get(2).contains("PlaceE"));
        t1.setOwnID(2);
        t3.setOwnID(1);
        t.updateNeighbor(t1);
        t.updateNeighbor(t3);
        assertFalse(t.getNeighbor().get(1).contains("PlaceB"));
        assertTrue(t.getNeighbor().get(1).contains("PlaceC"));
        assertFalse(t.getNeighbor().get(2).contains("PlaceD"));
        assertTrue(t.getNeighbor().get(2).contains("PlaceE"));
        assertTrue(t.getNeighbor().get(1).contains("PlaceD"));
        assertTrue(t.getNeighbor().get(2).contains("PlaceB"));
    }

    @Test
    void setOwnID() {
        Territory t = new Territory("PlaceA",0);
        assertEquals(0,t.getOwnID());
        t.setOwnID(1);
        assertEquals(1,t.getOwnID());
    }
    @Test
    void setName(){
        Territory t = new Territory();
        t.setName("PlaceA");
        assertEquals("PlaceA",t.getName());
    }
}