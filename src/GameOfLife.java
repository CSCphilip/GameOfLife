import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/** This is an implementation of Conway's Game of Life where I use JavaFX
 * to visualize each generation as it evolves.
 * @author Philip Andersson
 * @version 2019-11-22
 */
public class GameOfLife extends Application {

    // All cells should have a square shape with the size specified by this field
    private final int squareSize = 100;

    // The number of rows and columns the grid will consist of
    private final int maxRows = 1;
    private final int maxColumns = 10;

    /**
     * main method
     * @param args which should be a sequence of pairs representing
     *             the y and x positions of the starting cells which
     *             will form the initial state. The input should have
     *             the following form:
     *             cell1_row cell1_column cell2_row cell2_column ...
     */
    public static void main(String[] args) {
        launch(args); // Will call the start method in the background
    }

    /** This is an abstract method in Application and must therefore be overridden.
     * The main entry point for the JavaFX application. It's in this method most of
     * the things happen in regards to the game.
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        int[] input = convertArgs();
        if(!validInput(input)) {
            closeGame();
        }

        // Set up the JavaFX application
        stage.setTitle("Game of Life");

        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        // The height and width of the canvas
        int height = maxRows*squareSize;
        int width = maxColumns*squareSize;
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D(); // Drawing tools

        stage.show(); // Will launch a window in JavaFX

        // Anonymous inner class which is used to create a game loop
        AnimationTimer gameLoop = new AnimationTimer() {

            // This variable is used to slow down the fps of the game
            int iterations = 30;
            GameLogic logicHandler = new GameLogic(maxRows, maxColumns, input);

            /**
             * This method will be called repeatedly at a rate of 60 times per second.
             * @param currentNanoTime
             */
            @Override
            public void handle(long currentNanoTime) {
                if(iterations >= 30) {
                    render(gc, logicHandler.getCurrentGrid(), height, width);
                    logicHandler.buildNextGeneration();
                    iterations = 0;
                } else {
                    iterations++;
                }
            }
        };

        gameLoop.start();
    }

    /**
     * This method will draw a black square on the positions
     * where there is a living cell.
     * @param gc
     * @param grid
     * @param height
     * @param width
     */
    private void render(GraphicsContext gc, boolean[][] grid, int height, int width) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.BLACK);

        for(int row = 0; row < maxRows; row++) {
            for(int column = 0; column < maxColumns; column++) {
                if(grid[row][column]) {
                    gc.fillRect(column * squareSize, row * squareSize, squareSize, squareSize);
                }
            }
        }
    }

    /**
     * This method will convert the command line arguments (args) from String to int.
     * @return an int array containing values from args
     */
    private int[] convertArgs() {
        Application.Parameters args = getParameters();
        List<String> argsList = args.getRaw();
        int[] input = new int[argsList.size()];

        for(int i = 0; i < argsList.size(); i++) {
            input[i] = Integer.parseInt(argsList.get(i));
        }

        return input;
    }

    /**
     * This method will make sure that the input values follow the correct
     * input form specified in the doc to the main method.
     * @param input
     * @return true if the input values are within the dimension of grid otherwise false
     */
    private boolean validInput(int[] input) {
        if(input.length % 2 != 0) { // Every cell must have a row and column number
            return false;
        }

        for(int i = 0; i < input.length; i++) {
            if(i % 2 == 0) { // input[i] will contain the row number of a cell
                if(input[i] < 0 || input[i] >= maxRows) {
                    return false;
                }
            } else { // input[i] will contain the column number of a cell
                if(input[i] < 0 || input[i] >= maxColumns) {
                    return  false;
                }
            }
        }

        return true;
    }

    /**
     * This method prints useful info about valid game input values to the user
     * and then it closes the program.
     */
    private void closeGame() {
        System.out.println("The input is invalid!");
        System.out.println("The y position for a cell should be between " + 0 +
                            " and " + (maxRows - 1) + ".");
        System.out.println("The x position for a cell should be between " + 0 +
                            " and " + (maxColumns - 1) + ".");
        System.exit(0);
    }
}