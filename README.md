# Game of Life

### How to run game
`$ java -jar GameOfLife.jar arguments`  

The **arguments** should contain the y and x positions for the cells in the initial state. The size of the grid used in the JAR file is set to 20 rows and 35 columns. Make sure to use y values between 0 and 19 and x values between 0 and 34 when running the JAR file.

#### Example of input arguments with explanation
`$ java -jar GameOfLife.jar 2 3 2 4 2 5`  

This will create an initial state with three alive cells. The three cells will have the following positions in the start:
* Cell 1: y = 2, x = 3
* Cell 2: y = 2, x = 4
* Cell 3: y = 2, x = 5
