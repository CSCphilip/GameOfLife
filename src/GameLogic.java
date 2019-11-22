/**
 * This class will take care of the game logic to Game of Life.
 * The main idea is that I build the nextGenerationGrid based on
 * the values in the currentGrid and the rules of Game of Life.
 * @author Philip Andersson
 * @version 2019-11-22
 */
public class GameLogic {

    private boolean[][] currentGrid;
    private boolean[][] nextGenerationGrid;

    /**
     * Constructor
     * @param rows
     * @param columns
     * @param input
     */
    public GameLogic(int rows, int columns, int[] input) {
        // Initialize the grids
        currentGrid = new boolean[rows][columns];
        nextGenerationGrid = new boolean[rows][columns];

        // Form initial state with values from input
        int row, column;
        for(int i = 0; i < input.length; i += 2) {
            row = input[i];
            column = input[i + 1];
            currentGrid[row][column] = true;
        }
    }


    /**
     * This method builds the nextGenerationGrid based on the currentGrid and
     * the rules of Game of Life.
     */
    public void buildNextGeneration() {
        for(int row = 0; row < currentGrid.length; row++) {
            for(int column = 0; column < currentGrid[row].length; column++) {
                int neighbours;
                if(currentGrid.length == 1 && currentGrid[0].length == 1) {
                    neighbours = 0; // One cell grid doesn't have any neighbours
                } else if(currentGrid.length == 1) {
                    neighbours = countNeighboursOnOneRowGrid(column);
                } else if(currentGrid[0].length == 1) {
                    neighbours = countNeighboursOnOneColumnGrid(row);
                } else {
                    neighbours = countNeighbours(row, column);
                }

                boolean livingCell = currentGrid[row][column];

                if(livingCell && neighbours < 2) {
                    nextGenerationGrid[row][column] = false;
                } else if(livingCell && (neighbours == 2 || neighbours == 3)) {
                    nextGenerationGrid[row][column] = true;
                } else if(livingCell && neighbours > 3) {
                    nextGenerationGrid[row][column] = false;
                } else if(!livingCell && neighbours == 3) {
                    nextGenerationGrid[row][column] = true;
                } else {
                    nextGenerationGrid[row][column] = false;
                }
            }
        }

        copy2DArray();
    }

    /**
     * This method counts how many living neighbours a cell has.
     * It takes for granted that the grid has a minimum size
     * of 2 rows and 2 columns.
     * @param row
     * @param column
     * @return the amount of neighbours which are alive
     */
    private int countNeighbours(int row, int column) {
        if(row == 0 && column == 0) { // If the cell is in the top left corner
            return livingCell(currentGrid[row][column + 1]) + livingCell(currentGrid[row + 1][column + 1])
                    + livingCell(currentGrid[row + 1][column]);
        } else if(row == 0 && column == currentGrid[0].length - 1) { // If the cell is in the top right corner
            return livingCell(currentGrid[row + 1][column]) + livingCell(currentGrid[row + 1][column - 1])
                    + livingCell(currentGrid[row][column - 1]);
        } else if(row == currentGrid.length - 1 && column == currentGrid[currentGrid.length - 1].length - 1) { // If the cell is in the bottom right corner
            return livingCell(currentGrid[row][column - 1]) + livingCell(currentGrid[row - 1][column - 1])
                    + livingCell(currentGrid[row - 1][column]);
        } else if(row == currentGrid.length - 1 && column == 0) { // If the cell is in the bottom left corner
            return livingCell(currentGrid[row][column + 1]) + livingCell(currentGrid[row - 1][column])
                    + livingCell(currentGrid[row - 1][column + 1]);
        } else if(row == 0) { // If the cell is amongst the top cells excluding the corner cells
            return livingCell(currentGrid[row][column + 1]) + livingCell(currentGrid[row + 1][column + 1])
                    + livingCell(currentGrid[row + 1][column]) + livingCell(currentGrid[row + 1][column - 1])
                    + livingCell(currentGrid[row][column - 1]);
        } else if(column == currentGrid[row].length - 1) { // If the cell is amongst the rightmost cells excluding the corner cells
            return livingCell(currentGrid[row - 1][column]) + livingCell(currentGrid[row + 1][column])
                    + livingCell(currentGrid[row + 1][column - 1]) + livingCell(currentGrid[row][column - 1])
                    + livingCell(currentGrid[row - 1][column - 1]);
        } else if(row == currentGrid.length - 1) { // If the cell is amongst the bottom cells excluding the corner cells
            return livingCell(currentGrid[row - 1][column]) + livingCell(currentGrid[row - 1][column + 1])
                    + livingCell(currentGrid[row][column + 1]) + livingCell(currentGrid[row][column - 1])
                    + livingCell(currentGrid[row - 1][column - 1]);
        } else if(column == 0) { // If the cell is amongst the leftmost cells excluding the corner cells
            return livingCell(currentGrid[row - 1][column]) + livingCell(currentGrid[row - 1][column + 1])
                    + livingCell(currentGrid[row][column + 1]) + livingCell(currentGrid[row + 1][column + 1])
                    + livingCell(currentGrid[row + 1][column]);
        } else { // If the cell has neighbours in all 8 directions
            return livingCell(currentGrid[row - 1][column]) + livingCell(currentGrid[row - 1][column + 1])
                    + livingCell(currentGrid[row][column + 1]) + livingCell(currentGrid[row + 1][column + 1])
                    + livingCell(currentGrid[row + 1][column]) + livingCell(currentGrid[row + 1][column - 1])
                    + livingCell(currentGrid[row][column - 1]) + livingCell(currentGrid[row - 1][column - 1]);
        }
    }

    /**
     * This method takes care of the edge case where
     * the grid only has one row but more then one column.
     * @param column
     * @return the amount of neighbours which are alive
     */
    private int countNeighboursOnOneRowGrid(int column) {
        if(column == 0) { // If the cell is furthest to the left
            return livingCell(currentGrid[0][column + 1]);
        } else if(column == currentGrid[0].length - 1) { // If the cell is furthest to the right
            return livingCell(currentGrid[0][column - 1]);
        } else { // If the cell has cells to the left and right
            return livingCell(currentGrid[0][column - 1]) + livingCell(currentGrid[0][column + 1]);
        }
    }

    /**
     * This method takes care of the edge case where
     * the grid only has one column but more than one row.
     * @param row
     * @return the amount of neighbours which are alive
     */
    private int countNeighboursOnOneColumnGrid(int row) {
        if(row == 0) { // If the cell is at the top
            return livingCell(currentGrid[row + 1][0]);
        } else if(row == currentGrid.length - 1) { // If the cell is at the bottom
            return livingCell(currentGrid[row - 1][0]);
        } else { // If the cell has cells both above and below it
            return livingCell(currentGrid[row + 1][0]) + livingCell(currentGrid[row - 1][0]);
        }
    }

    /**
     * @param cell
     * @return 1 if the cell is alive (true) and 0 if the cell is dead (false)
     */
    private int livingCell(boolean cell) {
        if(cell) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * This method updates the currentGrid with values from the nextGenerationGrid.
     */
    private void copy2DArray() {
        for(int row = 0; row < currentGrid.length; row++) {
            for(int column = 0; column < currentGrid[row].length; column++) {
                currentGrid[row][column] = nextGenerationGrid[row][column];
            }
        }
    }

    /**
     * @return currentGrid
     */
    public boolean[][] getCurrentGrid() {
        return currentGrid;
    }
}
