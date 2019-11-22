import org.junit.Assert;
import org.junit.Test;

/** This class uses JUnit to test different things on the GameLogic class.
 * All the tests is about how the grid changes as the generations evolve.
 * See wikipedia page for better understanding of the different
 * patterns (e.g. blinker, toad).
 * @author Philip Andersson
 * @version 2019-11-22
 */
public class GameLogicTest {

    private final int rows = 23;
    private final int columns = 35;

    @Test
    public void testLonelyCell() {
        int[] lonelyCell = {0, 0};
        GameLogic gl = new GameLogic(rows, columns, lonelyCell);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[0][0],true);

        gl.buildNextGeneration();

        for(int row = 0; row < grid.length; row++) {
            for(int column = 0; column < grid[row].length; column++) {
                Assert.assertEquals(grid[row][column], false);
            }
        }
    }

    @Test
    public void testBlinkerOscillator() {
        int[] blinker = {5, 5, 5, 6, 5, 7};
        GameLogic gl = new GameLogic(rows, columns, blinker);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[5][5], true);
        Assert.assertEquals(grid[5][6], true);
        Assert.assertEquals(grid[5][7], true);
        Assert.assertEquals(grid[4][6], false);
        Assert.assertEquals(grid[6][6], false);

        for(int i = 0; i < 100; i++) {
            gl.buildNextGeneration();
            if(i % 2 == 0) {
                Assert.assertEquals(grid[5][5], false);
                Assert.assertEquals(grid[5][6], true);
                Assert.assertEquals(grid[5][7], false);
                Assert.assertEquals(grid[4][6], true);
                Assert.assertEquals(grid[6][6], true);
            } else {
                Assert.assertEquals(grid[5][5], true);
                Assert.assertEquals(grid[5][6], true);
                Assert.assertEquals(grid[5][7], true);
                Assert.assertEquals(grid[4][6], false);
                Assert.assertEquals(grid[6][6], false);
            }
        }
    }

    @Test
    public void testToadOscillator() {
        int[] toad = {3, 2, 3, 3, 3, 4, 4, 1, 4, 2, 4, 3};
        GameLogic gl = new GameLogic(rows, columns, toad);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[0][0],false);
        Assert.assertEquals(grid[3][2], true);

        gl.buildNextGeneration();

        Assert.assertEquals(grid[0][0], false);
        Assert.assertEquals(grid[3][2], false);
    }

    @Test
    public void testUncompletedBlockInTopLeftCorner() {
        int[] uncompletedBlock = {0, 1, 1, 0, 1, 1};
        GameLogic gl = new GameLogic(rows, columns, uncompletedBlock);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[0][0], false);

        for(int i = 0; i < 100; i++) {
            gl.buildNextGeneration();
            Assert.assertEquals(grid[0][0], true);
        }
    }


    @Test
    public void testDyingCellsInAll4Corners() {
        int[] fourDyingCells = {0, 0, 0, columns-1, rows-1, columns-1, rows-1, 0};
        GameLogic gl = new GameLogic(rows, columns, fourDyingCells);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[0][0], true);
        Assert.assertEquals(grid[0][columns-1], true);
        Assert.assertEquals(grid[rows-1][columns-1], true);
        Assert.assertEquals(grid[rows-1][0], true);

        gl.buildNextGeneration();

        Assert.assertEquals(grid[0][0], false);
        Assert.assertEquals(grid[0][columns-1], false);
        Assert.assertEquals(grid[rows-1][columns-1], false);
        Assert.assertEquals(grid[rows-1][0], false);
    }

    @Test
    public void testOneCellGrid() {
        int[] cell = {0, 0};
        GameLogic gl = new GameLogic(1, 1, cell);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[0][0], true);

        gl.buildNextGeneration();

        Assert.assertEquals(grid[0][0], false);
    }

    @Test
    public void test2x2Grid() {
        int[] block = {0, 0, 0, 1, 1, 0, 1, 1};
        GameLogic gl = new GameLogic(2, 2, block);
        boolean[][] grid = gl.getCurrentGrid();

        for(int i = 0; i < 100; i++) {
            Assert.assertEquals(grid[0][0], true);
            Assert.assertEquals(grid[0][1], true);
            Assert.assertEquals(grid[1][0], true);
            Assert.assertEquals(grid[1][1], true);
            gl.buildNextGeneration();
        }
    }

    @Test
    public void testGridWith2ColumnsAnd1Row() {
        int[] cell = {0, 1};
        GameLogic gl = new GameLogic(1, 2, cell);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[0][1], true);

        gl.buildNextGeneration();

        Assert.assertEquals(grid[0][1], false);
    }

    @Test
    public void testOverPopulatedCell() {
        int[] beacon = {0, 0, 0, 1, 1, 0, 1, 1, 2, 2, 2, 3, 3, 2, 3, 3};
        GameLogic gl = new GameLogic(rows, columns, beacon);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[1][1], true);
        Assert.assertEquals(grid[2][2], true);

        gl.buildNextGeneration();

        Assert.assertEquals(grid[1][1], false);
        Assert.assertEquals(grid[2][2], false);
    }

    @Test
    public void testStillGrid() {
        int[] cell = {6, 2};
        GameLogic gl = new GameLogic(rows, columns, cell);
        boolean[][] grid = gl.getCurrentGrid();

        Assert.assertEquals(grid[6][2], true);


        for(int i = 0; i < 100_000; i++) {
            gl.buildNextGeneration();

            for(int row = 0; row < grid.length; row++) {
                for(int column = 0; column < grid[row].length; column++) {
                    Assert.assertEquals(grid[row][column], false);
                }
            }
        }
    }
}