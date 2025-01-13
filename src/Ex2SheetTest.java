import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Ex2SheetTest {

    @Test
    void value() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "123");
        assertEquals("123.0", sheet.value(0, 0), "Value at (0,0) should be 123.0");

        sheet.set(1, 1, "=5+5");
        assertEquals("10.0", sheet.value(1, 1), "Value at (1,1) should evaluate to 10.0");

        sheet.set(2, 2, "");
        assertEquals("", sheet.value(2, 2), "Value at (2,2) should be empty");
    }

    @Test
    void get() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "Test");
        assertEquals("Test", sheet.get(0, 0).getData(), "Cell data at (0,0) should be 'Test'");

        sheet.set(1, 1, "=A1+B1");
        assertEquals("=A1+B1", sheet.get(1, 1).getData(), "Cell data at (1,1) should be '=A1+B1'");
    }

    @Test
    void testGet() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "42");
        assertEquals("42.0", sheet.get("A0").getData(), "Cell data at 'A0' should be '42.0'");
    }

    @Test
    void width() {
        Ex2Sheet sheet = new Ex2Sheet(10, 5);
        assertEquals(10, sheet.width(), "Width should be 10");
    }

    @Test
    void height() {
        Ex2Sheet sheet = new Ex2Sheet(10, 5);
        assertEquals(5, sheet.height(), "Height should be 5");
    }

    @Test
    void set() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(2, 2, "99");
        assertEquals("99.0", sheet.get(2, 2).getData(), "Cell data at (2,2) should be '99.0'");
    }

    @Test
    void eval() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(0, 0, "=1+1");
        sheet.eval();
        assertEquals("2.0", sheet.value(0, 0), "Value at (0,0) should evaluate to '2.0'");
    }

    @Test
    void isIn() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        assertTrue(sheet.isIn(0, 0), "(0,0) should be within bounds");
        assertFalse(sheet.isIn(3, 3), "(3,3) should be out of bounds");

    }

    @Test
    void depth() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        int[][] depth = sheet.depth();
        assertEquals(3, depth.length, "Depth array should have 3 rows");
        assertEquals(3, depth[0].length, "Depth array should have 3 columns in each row");
    }

    //check save and load both
    @Test
    void save() throws IOException {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(0, 0, "SaveTest");
        String testFile = "save_test.txt";

        sheet.save(testFile);

        Ex2Sheet loadedSheet = new Ex2Sheet(3, 3);
        loadedSheet.load(testFile);
        assertEquals("SaveTest", loadedSheet.value(0, 0), "Saved value at (0,0) should be 'SaveTest'");

    }

    @Test
    void testEval() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(0, 0, "=2+3");
        assertEquals("5.0", sheet.eval(0, 0), "Evaluated formula at (0,0) should be '5.0'");
    }

    @Test
    void computeForm() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        assertEquals(7.0, sheet.computeForm("=3+4"), 0.01, "Formula '=3+4' should evaluate to 7.0");
        assertEquals(50.0, sheet.computeForm("=5*10"),0.01, "Formula '=5*10' should evaluate to 50.0");
    }
}