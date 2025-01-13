import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellEntryTest {

    @Test
    void isValid() {
        // Valid cases
        assertTrue(new CellEntry(0, 0).isValid(), "Cell (0,0) should be valid");
        assertTrue(new CellEntry(5, 9).isValid(), "Cell (5,10) should be valid");
        assertTrue(new CellEntry(10, Ex2Utils.WIDTH).isValid(), "Cell within max width should be valid");

        // Invalid cases
        assertFalse(new CellEntry(-1, 0).isValid(), "Negative x should be invalid");
        assertFalse(new CellEntry(0, -1).isValid(), "Negative y should be invalid");
        assertFalse(new CellEntry(0, Ex2Utils.WIDTH + 1).isValid(), "y exceeding max width should be invalid");
    }

    @Test
    void testToString() {
        assertEquals("A1", new CellEntry(0, 1).toString(), "Cell (0,1) should be A1");
        assertEquals("B2", new CellEntry(1, 2).toString(), "Cell (1,2) should be B2");
        assertEquals("AA10", new CellEntry(26, 10).toString(), "Cell (26,10) should be AA10");
    }

    @Test
    void getX() {
        CellEntry cell = new CellEntry(5, 7);
        assertEquals(5, cell.getX(), "getX() should return the correct x value");
    }

    @Test
    void getY() {
        CellEntry cell = new CellEntry(5, 7);
        assertEquals(7, cell.getY(), "getY() should return the correct y value");
    }


}