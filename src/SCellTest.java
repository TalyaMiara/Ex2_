import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SCellTest {

    // Test for getOrder() method
    @Test
    void getOrder() {
        SCell cell = new SCell("10");
        cell.setOrder(5);
        assertEquals(5, cell.getOrder(), "Order should be 5");
        cell.setOrder(0);
        assertEquals(0, cell.getOrder(), "Order should be 0");

        cell.setOrder(-1);
        assertEquals(-1, cell.getOrder(), "Order should be -1");

    }

    @Test
    void setData() {
        SCell cell = new SCell("10");
        assertEquals("10.0", cell.getData(), "Data should be 10.0");
        assertEquals(Ex2Utils.NUMBER, cell.getType(), "Type should be NUMBER");

        cell.setData("=A1+B1");
        assertEquals("=A1+B1", cell.getData(), "Data should be =A1+B1");
        assertEquals(Ex2Utils.FORM, cell.getType(), "Type should be FORM");

    }

    @Test
    void getData() {
        SCell cell = new SCell("100");
        assertEquals("100.0", cell.getData(), "Data should be 100.0");  // בדיקה של ערך כ-String
        cell.setData("=SUM(A1:A10)");
        assertEquals("=SUM(A1:A10)", cell.getData(), "Data should be =SUM(A1:A10)");

        cell.setData("Text Value");
        assertEquals("Text Value", cell.getData(), "Data should be Text Value");

    }


    // Test for getType() method
    @Test
    void getType() {
        SCell cell = new SCell("123");
        assertEquals(Ex2Utils.NUMBER, cell.getType(), "Type should be NUMBER");

        cell.setData("=A2+B2");
        assertEquals(Ex2Utils.FORM, cell.getType(), "Type should be FORM");

        cell.setData("Plain Text");
        assertEquals(Ex2Utils.TEXT, cell.getType(), "Type should be STRING");
    }

    @Test
    void setType() {
        SCell cell = new SCell("initial");
        cell.setType(Ex2Utils.NUMBER);
        assertEquals(Ex2Utils.NUMBER, cell.getType(), "Type should be set to NUMBER");

        cell.setType(Ex2Utils.FORM);
        assertEquals(Ex2Utils.FORM, cell.getType(), "Type should be set to FORM");

        cell.setType(Ex2Utils.TEXT);
        assertEquals(Ex2Utils.TEXT, cell.getType(), "Type should be set to STRING");
    }

    @Test
    void setOrder() {
        SCell cell = new SCell("10");
        cell.setOrder(10);
        assertEquals(10, cell.getOrder(), "Order should be set to 10");

        cell.setOrder(-5);
        assertEquals(-5, cell.getOrder(), "Order should be set to -5");

        cell.setOrder(0);
        assertEquals(0, cell.getOrder(), "Order should be set to 0");
    }

    @Test
    void isNumber() {
        assertTrue(SCell.isNumber("123"), "String '123' should be recognized as a number");
        assertTrue(SCell.isNumber("123.456"), "String '123.456' should be recognized as a number");

        assertFalse(SCell.isNumber("NotANumber"), "String 'NotANumber' should not be recognized as a number");
        assertFalse(SCell.isNumber("123abc"), "String '123abc' should not be recognized as a number");

        assertFalse(SCell.isNumber(""), "Empty string should not be recognized as a number");
        assertFalse(SCell.isNumber(null), "Null should not be recognized as a number");
    }

    @Test
    void isForm() {
        assertTrue(SCell.isForm("=A1+B1"), "String '=A1+B1' should be recognized as a formula");
        assertTrue(SCell.isForm("=SUM(A1:A10)"), "String '=SUM(A1:A10)' should be recognized as a formula");

        assertFalse(SCell.isForm("NotAFormula"), "String 'NotAFormula' should not be recognized as a formula");
        assertFalse(SCell.isForm("123"), "String '123' should not be recognized as a formula");

        assertFalse(SCell.isForm(""), "Empty string should not be recognized as a formula");
        assertFalse(SCell.isForm(null), "Null should not be recognized as a formula");
    }
}