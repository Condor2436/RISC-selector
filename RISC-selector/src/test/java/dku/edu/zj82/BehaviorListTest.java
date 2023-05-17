package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BehaviorListTest {

    @Test
    void getMoveList() {
        BehaviorList bl = new BehaviorList();
        assertEquals(0,bl.getMoveList().size());
        Behavior b = new Behavior();
        bl.addToMoveList(b);
        assertNotNull(bl.getMoveList());
        assertTrue(bl.getMoveList().contains(b));
    }

    @Test
    void getAttackList() {
        BehaviorList bl = new BehaviorList();
        assertEquals(0,bl.getAttackList().size());
        Behavior b = new Behavior();
        bl.addToAttackList(b);
        assertNotNull(bl.getAttackList());
        assertTrue(bl.getAttackList().contains(b));
    }

    @Test
    void getPlayID() {
        BehaviorList bl = new BehaviorList(1,0);
        assertEquals(1,bl.getPlayID());
    }

    @Test
    void getStatus() {
        BehaviorList bl = new BehaviorList(1,0);
        assertEquals(0,bl.getStatus());
    }
}