import java.io.IOException;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    //
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        // Add your code here

        Cell c = get(x, y);
        if (c != null) {
            ans = c.toString();
        }

        /////////////////////
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        if (cords == null || cords.isEmpty()) {
            return null;
        }
        cords = cords.toUpperCase();
        int col = cords.charAt(0) - 'A'; // עמודה לפי אות ראשונה
        int row;
        try {
            row = Integer.parseInt(cords.substring(1)); // שורה מהמספר לאחר האות
        } catch (NumberFormatException e) {
            return null;
        }

        if (!isIn(row, col)) {
            return null;
        }
        return get(row, col);
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;
        // Add your code here

        /////////////////////
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && xx < width() && yy >= 0 && yy < height();
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if (get(x, y) != null) {
            ans = get(x, y).toString();
        }
        // Add your code here

        /////////////////////
        return ans;
    }


    public Double computeForm(String form) {

        if (form == null || form.isEmpty()) {
            throw new IllegalArgumentException("Formula cannot be null or empty");
        }

        // בדיקה אם הפורמולה מתחילה עם '='
        if (form.charAt(0) != '=') {
            throw new IllegalArgumentException("Formula must start with '='");
        }

        // להסיר את ה- "=" מהפורמולה
        String expression = form.substring(1);

        // בדיקה אם הביטוי שנשאר לא ריק
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("Formula is invalid after removing '='");
        }

        // חישוב הביטוי
        return evaluate(expression);
    }

    // Function to find the main operator in an expression
    private int findMainOperator(String expression) {
        int openParentheses = 0;
        int index = -1;
        int currentPriority = Integer.MAX_VALUE; // Start with the highest possible priority.

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '(') {
                openParentheses++;
            } else if (c == ')') {
                openParentheses--;
            } else if (openParentheses == 0) {
                // Process only when not inside parentheses
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    int priority = operatorPriority(c);

                    // Choose the operator with the lowest priority (highest precedence)
                    if (priority <= currentPriority) {
                        currentPriority = priority;
                        index = i;
                    }
                }
            }
        }

        // Return the index of the operator with the lowest priority (highest precedence)
        return index;
    }

    // פונקציה רקורסיבית להערכת ביטוי

    private Double evaluate(String expression) {
        expression = expression.trim();

        // Remove surrounding parentheses if the entire expression is wrapped in them
        if (expression.startsWith("(") && expression.endsWith(")")) {
            int openCount = 0;
            boolean isWrapped = true;

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (c == '(') openCount++;
                else if (c == ')') openCount--;

                // If parentheses mismatch at any point other than the last character, it's not fully wrapped
                if (openCount == 0 && i < expression.length() - 1) {
                    isWrapped = false;
                    break;
                }
            }

            // If the expression is fully wrapped, strip the outer parentheses
            if (isWrapped) {
                return evaluate(expression.substring(1, expression.length() - 1));
            }
        }

        // Base case: If the expression is a number
        if (isNumber(expression)) {
            return Double.parseDouble(expression);
        }

        // Find the main operator
        int indexOfMainOperator = findMainOperator(expression);

        // If no operator is found, throw an error
        if (indexOfMainOperator == -1) {
            throw new IllegalArgumentException("No valid operator found in the expression");
        }

        // Split the expression into two parts
        String left = expression.substring(0, indexOfMainOperator).trim();
        String right = expression.substring(indexOfMainOperator + 1).trim();
        char operator = expression.charAt(indexOfMainOperator);

        // Recursively evaluate the left and right parts
        Double leftValue = evaluate(left);
        Double rightValue = evaluate(right);

        // Perform the operation
        switch (operator) {
            case '+':
                return leftValue + rightValue;
            case '-':
                return leftValue - rightValue;
            case '*':
                return leftValue * rightValue;
            case '/':
                return leftValue / rightValue;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private boolean isNumber(String expression) {
        try {
            Double.parseDouble(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // פונקציה למציאת האופרטור הראשי האחרון שיבוצע ותחזיר את המיקום של האופרטור האחרון שיבוצע


    // פונקציה לקבלת עדיפות של אופרטור
    private int operatorPriority(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1; // Addition/Subtraction - lowest precedence
            case '*':
            case '/':
                return 2; // Multiplication/Division - higher precedence
            default:
                return Integer.MAX_VALUE; // Invalid operator or no operator
        }
    }
}
