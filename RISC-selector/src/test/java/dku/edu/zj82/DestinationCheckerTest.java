package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DestinationCheckerTest {

    @Test
    void checkMyRule() {
        DestinationChecker destinationChecker = new DestinationChecker(null);
        Territory Q = new Territory("Q", 1);
        Territory W = new Territory("W", 2);
        Territory A = new Territory("A", 1);
        Territory S = new Territory("S", 1);
        Territory E = new Territory("E", 2);
        Q.setUnits(new Units(10));
        W.setUnits(new Units(10));
        A.setUnits(new Units(10));
        S.setUnits(new Units(10));
        E.setUnits(new Units(10));
        Q.updateNeighbor(W);
        Q.updateNeighbor(A);
        W.updateNeighbor(S);
        W.updateNeighbor(Q);
        W.updateNeighbor(E);
        E.updateNeighbor(W);
        S.updateNeighbor(W);
        A.updateNeighbor(Q);
        Behavior a1 = new Behavior(Q, W, 5, 1,"attack");
        Behavior a2 = new Behavior(Q, A, 5, 1,"attack");
        Behavior a3 = new Behavior(Q, E, 5, 1,"attack");
        Behavior m1 = new Behavior(Q, W, 5, 1, "move");
        Behavior m2 = new Behavior(Q, A, 5, 1, "move");
        Behavior m3 = new Behavior(Q, S, 5, 1, "move");
        ArrayList<Territory> globalMap = new ArrayList<>();
        globalMap.add(Q);
        globalMap.add(W);
        globalMap.add(A);
        globalMap.add(S);
        globalMap.add(E);
        assertNull(destinationChecker.checkMyRule(a1, globalMap));
        assertEquals("you are attacking you own place!", destinationChecker.checkMyRule(a2,globalMap));
        assertEquals("Enemy not adjacent to you", destinationChecker.checkMyRule(a3,globalMap));
        assertNull(destinationChecker.checkMyRule(m2, globalMap));
        assertEquals("you are moving to a place which not belongs to you!", destinationChecker.checkMyRule(m1,globalMap));
        assertEquals("No path to the destination", destinationChecker.checkMyRule(m3,globalMap));
    }
}