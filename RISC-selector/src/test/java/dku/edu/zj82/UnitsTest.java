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
}