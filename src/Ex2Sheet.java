import java.io.*;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
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
        return get(x, y).getData() != null ? eval(x, y) : Ex2Utils.EMPTY_CELL; // Evaluate cell value if non-empty
    }

    /**
     * Return the Cell in the x,y, position (or null if not in).
     * @param x integer, x-coordinate of the cell.
     * @param y integer, y-coordinate of the cell.
     * @return the cell in the x,y coordinate.
     */
    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    /**
     * @return the cell at the X.Y coordinate, or null if cords is an illegal coordinate or is out of this SprayedSheet.
     */
    @Override
    public Cell get(String cords) {
        if (cords == null || cords.isEmpty()) {
            return null;
        }
        cords = cords.toUpperCase();
        int col = cords.charAt(0) - 'A';
        int row;
        try {
            row = Integer.parseInt(cords.substring(1));
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
        if(isIn(x,y)){
            table[x][y] = c;
        }
    }
    /**
     * Evaluates (computes) all the values of all the cells in this spreadsheet.
     */
    @Override
    public void eval() {
        for (int x=0; x<width();x++){
            for (int y = 0; y <height() ; y++) {
                 eval(x,y);
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && xx < width() && yy >= 0 && yy < height();
    }
    /**
     *  Computes a 2D array of the same dimension as this SpreadSheet, each entry holds its dependency depth.
     *  if a cell is not dependent on any other cell its depth is 0.
     *  else assuming the cell depends on cell_1, cell_2... cell_n, the depth of a cell is
     *  1+max(depth(cell_1), depth(cell_2), ... depth(cell_n)).
     *  In case a cell os a circular dependency (e.g., c1 depends on c2 & c2 depends on c1) its depth should be -1.
     */
    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];

        return ans;
    }
    //load the file i saved before
    @Override
    public void load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Clear the existing table
            table = new SCell[width()][height()];
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    table[x][y] = new SCell(Ex2Utils.EMPTY_CELL);
                }
            }

            // Validate the file header
            String header = reader.readLine();
            if (header == null || !header.equals("I2CS ArielU: SpreadSheet (Ex2) assignment\n")) {
                throw new IOException("Invalid file format: Missing or incorrect header.");
            }

            // Read and parse each line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new IOException("Invalid file format: Incorrect number of columns in line: " + line);
                }

                try {
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());
                    String data = parts[2].trim();

                    if (isIn(x, y)) {
                        table[x][y] = new SCell(data);
                    } else {
                        throw new IOException("Invalid cell coordinates in line: " + line);
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid cell coordinates format in line: " + line, e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    //save the values of the table
    @Override
    public void save(String fileName) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("I2CS ArielU: SpreadSheet (Ex2) assignment - this line should be ignored in the load method\n"); // Write header
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    Cell cell = get(x, y);
                    if (cell != null && !cell.getData().isEmpty()) {
                        writer.write(x + "," + y + "," + cell.getData() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * Evaluates (computes) the value of the cell in the x,y coordinate.
     * @param x integer, x-coordinate of the cell.
     * @param y integer, y-coordinate of the cell.
     * @return the string that will be presented in the x,y cell
     */
    @Override
    public String eval(int x, int y) {
        Cell cell=get(x,y);
        if(cell==null){
            return Ex2Utils.EMPTY_CELL;
        }
        if (cell.getType()==Ex2Utils.FORM || (!cell.getData().isEmpty() &&cell.getData().startsWith("=")) ){

            try {
                String str=computeForm(cell.getData()).toString();
                return str;
            }
            catch (IllegalArgumentException e){
                cell.setType(Ex2Utils.ERR_FORM_FORMAT);
                return Ex2Utils.ERR_FORM;
            }
            catch (StackOverflowError e){
                cell.setType(Ex2Utils.ERR_CYCLE_FORM);
                return Ex2Utils.ERR_CYCLE;
            }

        }
        if (cell.getType()==Ex2Utils.ERR_CYCLE_FORM){
            return Ex2Utils.ERR_CYCLE;
        }
        if (cell.getType()==Ex2Utils.ERR_FORM_FORMAT){
            return Ex2Utils.ERR_FORM;
        }
        return cell.getData();



    }

    //calculate the formula and return it as a double
    public Double computeForm(String form) {

        if (form == null || form.isEmpty()) {
            throw new IllegalArgumentException("Formula cannot be null or empty");
        }

        // check if the formula starts with =
        if (form.charAt(0) != '=') {
            throw new IllegalArgumentException("Formula must start with '='");
        }

        // remove = from the expression is not empty
        String expression = form.substring(1);

        // check if the new expression is
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("Formula is invalid after removing '='");
        }

        // call evaluate to return the calculate of the expression
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

    // a recursive function that return the result of the expression

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

            CellEntry ce = new CellEntry(expression);
            int x=ce.getX();
            int y=ce.getY();
            String s=get(x,y).getData();
            if (s.charAt(0)=='='){
                return  evaluate((computeForm(s)).toString());
            }
            return evaluate(s);
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
