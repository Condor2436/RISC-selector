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

    @Test
    void addUnit() {
        Units u = new Units(15);
        u.addUnit();
        assertEquals(16,u.getNums());
    }

    @Test
    void testAddUnit() {
        Units u = new Units(15);
        u.addUnit(5);
        assertEquals(20,u.getNums());
    }

    @Test
    void testAddUnit1() {
        Units u = new Units(15);
        u.addUnit(new Units(15));
        assertEquals(30,u.getNums());
    }

    @Test
    void reduceUnit() {
        Units u = new Units(15);
        u.reduceUnit(new Units(10));
        assertEquals(5,u.getNums());
    }
}