package dku.edu.zj82;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransInfoTest {

    @Test
    void getType() {
        TransInfo ti = new TransInfo();
        ti.setType("test1");
        assertEquals("test1",ti.getType());
    }

    @Test
    void getInfo() {
        TransInfo ti = new TransInfo();
        ti.setInfo("test1");
        assertEquals("test1",ti.getInfo());
        TransInfo ti1 = new TransInfo("test", "test");
        assertEquals("test",ti1.getInfo());
    }
}