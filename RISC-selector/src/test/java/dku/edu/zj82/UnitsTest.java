package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitsTest {

    @Test
    void setNums() {
        Units u = new Units();
        u.setNums(10);
        assertEquals(10,u.getNums());
    }

    @Test
    void checkEnoughUnit() {
        Units u = new Units(15);
        Units h = new Units(20);
        Units t = new Units(5);
        assertTrue(u.checkEnoughUnit(t));
        assertFalse(u.checkEnoughUnit(h));
    }

    @Test
    void testToString() {
        Units u = new Units(15);
        assertEquals("Units{15}",u.toString());
    }
}