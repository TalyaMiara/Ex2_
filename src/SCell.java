// Add your documentation below:

public class SCell implements Cell {
    private String line;  // Stores the content of the cell
    private int type;     // Represents the type of data (TEXT, NUMBER, FORM)
    private int order;    // Represents the order of the cell

    /**
     * Constructor that initializes the cell with a string value.
     * @param s The initial value to set in the cell.
     */
    public SCell(String s) {
        // Add your code here
        setData(s);// Set initial value for the cell
    }
    /**
     * @return The order of the cell.
     */
    @Override
    public int getOrder() {
        return order;
    }
    /**
     * Sets the data for the cell, determining the type (NUMBER, FORM, or TEXT).
     * @param s The data to set in the cell.
     */
    @Override
public void setData(String s) {
        this.line=s;
        // Update the type when setting the data

        if (isNumber(s)) {
            this.line=String.valueOf(Double.parseDouble(s));
            type = Ex2Utils.NUMBER;
        }  else if (s.startsWith("=")&&isForm(s)) {
            type = Ex2Utils.FORM;
        } else {
            type = Ex2Utils.TEXT; // invalid formula or input
        }
    }
    /**
     * Returns the data stored in the cell.
     * @return The cell's data as a string.
     */
    @Override
    public String getData() {
        return line;
    }
    /**
     * Returns the type of the cell (NUMBER, FORM, TEXT).
     * @return The type of the cell.
     */
    @Override
    public int getType() {
        return type;
    }
    /**
     * Sets the type of the cell.
     * @param t The type to set for the cell.
     */
    @Override
    public void setType(int t) {
        type = t;
    }
    /**
     * Sets the order of the cell.
     * @param t The order to set for the cell.
     */
    @Override
    public void setOrder(int t) {
        order = t;
    }
    /**
     * Checks if a given string is a valid number.
     * @param text The string to check.
     * @return True if the string is a valid number, false otherwise.
     */
    public static boolean isNumber(String text) {
        if (text == null || text.isEmpty()) return false;
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Checks if the given string is a valid formula.
     * A formula must start with '=' and follow certain rules for operators and parentheses.
     * @param text The string to check.
     * @return True if the string is a valid formula, false otherwise.
     */
    public static boolean isForm(String text) {

        if (text == null || text.isEmpty()) return false;

        //create new string from the 'text' string that start from the undex-1(without =)
        if (text.charAt(0)!='=') return false;
        String content = text.substring(1);

        // Check for invalid consecutive operators (e.g., ++, --, etc.)
        if (content.matches(".*([+\\-*/])\\1{1,}.*")) {
            return false;
        }

        // Check parentheses matching
        int openCount = 0;
        for (char c : content.toCharArray()) {
            if (c == '(') openCount++;
            else if (c == ')') openCount--;
            if (openCount < 0) return false; // A closing parenthesis appeared before an opening parenthesis
        }
        if (openCount != 0) return false; // Unequal number of opening and closing parentheses

        StringBuilder sb = new StringBuilder(content);

        for (int i = 0; i < sb.length() - 1; i++) {
            char current = sb.charAt(i);
            char next = sb.charAt(i + 1);

            // Check for invalid closing parenthesis followed by an opening parenthesis
            if (current == ')' && next == '(') {
                return false;
            }
            // Check for invalid closing parenthesis followed by a valid number

            if (current == ')' && Character.isDigit(next)) {
                return false;
            }
            // Check for an invalid number followed by an opening parenthesis
            if (Character.isDigit(current) && next == '(') {
                return false;
            }
        }
        // Check if the formula ends with an opening parenthesis or starts with a closing parenthesis

        if (content.endsWith("(") || content.startsWith(")")) {
            return false;
        }

        return true;
    }

}
