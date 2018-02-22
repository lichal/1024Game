package game1024;

import java.util.ArrayList;
/*******************************************
Java interface for Number Slider.  Ensures
necessary methods are implemented.

@author Hans Dulimarta
@version Feb 7, 2016
**************************************************/
public interface NumberSlider {

    /**********************************************
     * Reset the game logic to handle a board of a 
     * given dimension
     *
     * @param rows the number of rows in the board
     * @param columns the number of columns in the board
     * @param winningValue such as 1024 or 2048
     * @throws IllegalArgumentException when the winning 
     * value is not power of two or negative
     **********************************************/
     void resizeBoard (int rows, int columns, int winningValue);


    /***********************************************
     @return number of rows
     **********************************************/
     int getRows();
     
    /***********************************************
     @return number of columns
     **********************************************/     
     int getCols();
     
    /***********************************************
     @return current game score
     **********************************************/     
     int getScore();
     
    /***********************************************
     * Use System.out.print() to display each value
     * Use a period for blank tiles (value of 0)
     **********************************************/
     void printBoard();
     
    /***********************************************
     * Remove all numbered tiles from the board and place
     * TWO random values at random location (2 or 4)
     **********************************************/
    void reset();

    /***********************************************
     * Set the game board to the desired values given 
     * in the 2D array. This method should use nested 
     * loops to copy each element from the provided array 
     * to the internal array. Do not use a single assignment
     * statement.  Otherwise, the internal array may get 
     * corrupted duriing testing. This method is mainly 
     * used for JUnit testing.
     * @param ref a 2D array of integer values
     **********************************************/
    void setValues(final int[][] ref);

    /**********************************************
     * Insert one random tile into an empty spot on the 
     * board. 80% of the time the random number wil
     * be 2.  The remaining times it will be 4.
     *
     * @return a Cell object with its row, column, 
     * and value attributes initialized properly
     *
     * @throws IllegalStateException when the board 
     * has no empty cell
     **********************************************/
    Cell placeRandomValue();

    /***********************************************
     * Slide all tiles in the requested direction
     * @param dir direction to move the tiles
     *
     * @return true when the board changes
     **********************************************/
    boolean slide (SlideDirection dir);

    /**********************************************
     * Create an ArrayList of all tiles with values.
     * @return an arraylist of Cells. 
     *********************************************/
    ArrayList<Cell> getNonEmptyTiles();

    /**********************************************
     * Return the current game status 
     * @return one of the possible game states
     *********************************************/
    GameStatus getStatus();

    /*********************************************
     * Undo the most recent action, i.e. restore 
     * the board to its previous state. Calling 
     * this method multiple times will ultimately restore
     * the game to the initial state holding two
     * values. Further attempt to undo beyond the 
     * initial state will throw an IllegalStateException.
     *
     * @throws IllegalStateException when undo is not possible
     ********************************************/
    void undo();
    
}
