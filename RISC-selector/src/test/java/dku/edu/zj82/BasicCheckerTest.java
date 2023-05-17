package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BasicCheckerTest {

    @Test
    void processCheck() {
        BasicChecker basicChecker = new BasicChecker(null) {
            @Override
            public String checkMyRule(Behavior myBehavior, ArrayList<Territory> globalMap) {
                return null;
            }
        };
        Behavior b = new Behavior();
        ArrayList<Territory> al = new ArrayList<>();
        assertNull(basicChecker.processCheck(b,al));
        BasicChecker errbasicChecker = new BasicChecker(null) {
            @Override
            public String checkMyRule(Behavior myBehavior, ArrayList<Territory> globalMap) {
                return "test error";
            }
        };
        BasicChecker errLinebasicChecker = new BasicChecker(errbasicChecker) {
            @Override
            public String checkMyRule(Behavior myBehavior, ArrayList<Territory> globalMap) {
                return null;
            }
        };
        assertNotNull(errLinebasicChecker.processCheck(b,al));
    }
}