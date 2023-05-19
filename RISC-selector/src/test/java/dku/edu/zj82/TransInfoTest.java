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

    @Test
    void getMuliInfo() {
        TransInfo test = new TransInfo();
        String message = "1 2 3 4 5";
        String token[] = message.split(" ");
        TransInfo test1 = new TransInfo("test1","test1",token);
        test.setMuliInfo(token);
        assertEquals(token,test.getMuliInfo());
        assertEquals(token,test1.getMuliInfo());
    }
}