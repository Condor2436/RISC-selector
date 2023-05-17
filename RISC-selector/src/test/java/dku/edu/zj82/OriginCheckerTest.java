package dku.edu.zj82;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OriginCheckerTest {

    @Test
    void checkMyRule() {
        Territory t1 = new Territory("PlaceA", 1);
        t1.setUnits(new Units(15));
        Territory t2 = new Territory();
        Behavior b = new Behavior(t1,t2,5,1,"move");
        Behavior b1 = new Behavior(t1,t2, 25, 1, "ATTACK");
        Behavior b2 = new Behavior(t1, t2, 5, 0, "MOVE");
        Behavior b3 = new Behavior(t1,t2,25,0,"attack");
        OriginChecker originChecker = new OriginChecker(null);
        assertNull(originChecker.checkMyRule(b, new ArrayList<>()));
        assertEquals("No enough units, it has Units{15} but use Units{25}",originChecker.checkMyRule(b1, new ArrayList<>()));
        assertEquals("The behavior requires playerID equal to the ID of place of departure, playerID is 0, territory ID is 1",originChecker.checkMyRule(b2, new ArrayList<>()));
        assertEquals("The behavior requires playerID equal to the ID of place of departure, playerID is 0, territory ID is 1",originChecker.checkMyRule(b3, new ArrayList<>()));
    }
}