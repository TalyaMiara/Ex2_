public class CellEntry implements Index2D {
    private final int x; // Column
    private final int y; // Row

    // Constructor with (x, y) values
    public CellEntry(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Constructor from string format like "B3"
    public CellEntry(String cellIndex) {
        if (cellIndex == null || cellIndex.length() < 2) {
            throw new IllegalArgumentException("Invalid cell index: " + cellIndex);
        }
        // Extract the column (letters) and row (numbers)
        String columnPart = cellIndex.replaceAll("[^A-Za-z]", ""); // Letters only
        String rowPart = cellIndex.replaceAll("[^0-9]", ""); // Numbers only

        if (columnPart.isEmpty() || rowPart.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell index: " + cellIndex);
        }

        // Convert column letters to index (A=0, B=1, etc.)
        this.x = columnToIndex(columnPart);

        // Parse row as integer
        this.y = Integer.parseInt(rowPart);
    }

    // Helper: Convert column letters (e.g., "A", "B", "AA") to integer index
    private int columnToIndex(String column) {
        int index = 0;
        for (char c : column.toUpperCase().toCharArray()) {
            if (c < 'A' || c > 'Z') {
                throw new IllegalArgumentException("Invalid column: " + column);
            }
            index = index * 26 + (c - 'A' + 1);
        }
        return index - 1; // Adjust to 0-based index
    }

    // Helper: Convert integer index to column letters
    private String indexToColumn(int index) {
        StringBuilder column = new StringBuilder();
        while (index >= 0) {
            column.insert(0, (char) ('A' + index % 26));
            index = index / 26 - 1;
        }
        return column.toString();
    }

    @Override
    // Validates that x and y are non-negative and y is within the allowed width.
    public boolean isValid() {
        return x >= 0 && y >= 0 && y <= Ex2Utils.WIDTH;
    }

    @Override
    // Converts the x coordinate to a column name and combines it with the y coordinate.
    public String toString() {
        return indexToColumn(x) + y;
    }

    @Override
    // Returns the x coordinate.
    public int getX() {
        return x;
    }

    @Override
    // Returns the y coordinate.
    public int getY() {
        return y;
    }
}
