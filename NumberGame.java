package game1024;

import java.util.*;

import javax.swing.text.Element;

/***********************************************************************
 * This class will have all basic functions of a 1024 game. It will be able to
 * perform shifting tiles, undo moves, return game states, and formating board.
 * 
 * @author Cheng Li
 * @version 6-08-2017
 **********************************************************************/
public class NumberGame implements NumberSlider {

	/** A 2-D array for 1024 game board */
	private int[][] board;

	/** The winning number, number rows, columns, and total scores */
	private int WIN, ROWS, COLS, SCORES;

	/** Current state of the game. (lose, win, or in progress */
	private GameStatus gameState;

	/** A stack stores all past steps of the 1024 game */
	private Stack<int[][]> record;
	
	/** A stack stores all past score of the 1024 game */
	private Stack<Integer> scoreRecord;

	/** Determines if the board is moved or not */
	private boolean moved;

	/** Random variable for random placement */
	private Random rand;

	/*******************************************************************
	 * This is the main constructor
	 ******************************************************************/
	public NumberGame() {
		rand = new Random();
		record = new Stack<int[][]>();
		scoreRecord = new Stack<Integer>();
		SCORES = 0;
		Queue <Integer> en = new LinkedList<Integer>();
		
		
	}

	/*******************************************************************
	 * Reset the game logic to handle a board of a given dimension
	 * 
	 * @param rows
	 *            the number of rows in the board
	 * @param columns
	 *            the number of columns in the board
	 * @param winningValue
	 *            such as 1024 or 2048
	 * @throws IllegalArgumentException
	 *             when winning value is not power of two or negative
	 ******************************************************************/
	@Override
	public void resizeBoard(int rows, int columns, int winningValue) {
		int checkValue = winningValue;

		// check if the winningValue is a power of 2
		if (checkValue % 2 == 0)
			while (checkValue != 2)
				checkValue /= 2;

		// throws IllegalArgument for negative and invalid win values
		if (winningValue < 0 || checkValue != 2)
			throw new IllegalArgumentException(
					"Winning value is negative or not a power of 2.");
		ROWS = rows;
		COLS = columns;
		WIN = winningValue;

		board = new int[ROWS][COLS];
	}

	/*******************************************************************
	 * @return numbers of rows in the board
	 ******************************************************************/
	@Override
	public int getRows() {
		return board.length;
	}

	/*******************************************************************
	 * @return numbers of columns in the board
	 ******************************************************************/
	@Override
	public int getCols() {
		return board[0].length;
	}

	/*******************************************************************
	 * @return current game scores
	 ******************************************************************/
	@Override
	public int getScore() {
		return SCORES;
	}

	/*******************************************************************
	 * System.out.print the game board with desired value in the array. 0 is
	 * represented as a decimal.
	 ******************************************************************/
	@Override
	public void printBoard() {
		String value = "";

		System.out.println("");
		// loop to print out values in rows and columns
		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++) {
				if (board[r][c] == 0)
					value = ".";
				else
					value = "" + board[r][c];
				System.out.print("\t" + value);
			}
			System.out.print("\n");
		}
	}

	/*******************************************************************
	 * Remove all numbered tiles from the board and place two random values at
	 * random location (2 or 4)
	 ******************************************************************/
	@Override
	public void reset() {
		for (int r = 0; r < getRows(); r++)
			for (int c = 0; c < getCols(); c++)
				board[r][c] = 0;
		SCORES = 0;
		
		scoreRecord.removeAllElements();
		record.removeAllElements();
		placeRandomValue();
		placeRandomValue();
	}

	/*******************************************************************
	 * Set the game board to the desired values given in the 2D array.
	 * 
	 * @param ref
	 *            a 2D array of integer values
	 * @throws IllegalArgumentException
	 *             if the board size is greater than the current board
	 ******************************************************************/
	@Override
	public void setValues(int[][] ref) {

		if (ref.length > board.length
				|| ref[0].length > board[0].length)
			throw new IllegalArgumentException();

		for (int r = 0; r < ref.length; r++)
			for (int c = 0; c < ref[r].length; c++)
				board[r][c] = ref[r][c];
	}

	/*******************************************************************
	 * Insert one random tile into an empty spot on the board. 80% of the time
	 * the random number will be 2. The remaining times it will be 4.
	 *
	 * @return a Cell object with its row, column, and value attributes
	 *         initialized properly
	 *
	 * @throws IllegalStateException
	 *             when the board has no empty cell
	 ******************************************************************/
	@Override
	public Cell placeRandomValue() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		Cell c1;

		// loop the Array to get the locations with a zero
		// and stores the location info into Cell
		for (int r = 0; r < getRows(); r++)
			for (int c = 0; c < getCols(); c++)
				if (board[r][c] == 0) {
					list.add(new Cell(r, c, randTwoFour()));
				}

		// Throws IllegalStateException if there is no empty space
		if (getNonEmptyTiles().size() == COLS * ROWS)
			throw new IllegalStateException("The board is full");

		// randomly assign a location to place new value
		c1 = list.get(rand.nextInt(list.size()));
		board[c1.getRow()][c1.getCol()] = c1.getValue();

		return c1;
	}

	/*******************************************************************
	 * Slides all the tiles to the requested direction
	 * 
	 * @param dir
	 *            the direction the tiles is moving to
	 * @return true when the tiles is moved
	 ******************************************************************/
	@Override
	public boolean slide(SlideDirection dir) {
		moved = false;
		int[][] copy;
		int copyScore = SCORES;

		copy = new int[getRows()][getCols()];

		// Create a copy of the current board
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				copy[r][c] = board[r][c];

		// Shift Right
		if (dir == SlideDirection.RIGHT)
			if (shiftRight())
				moved = true;

		// Shift Left
		if (dir == SlideDirection.LEFT)
			if (shiftLeft())
				moved = true;

		// Shift Up
		if (dir == SlideDirection.UP)
			if (shiftUp())
				moved = true;

		// Shift Down
		if (dir == SlideDirection.DOWN)
			if (shiftDown())
				moved = true;

		// stores board and place a random value if the board is moved
		if (moved) {
			record.add(copy);
			placeRandomValue();
			scoreRecord.add(copyScore);
		}
		return moved;
	}

	/*******************************************************************
	 * Create an ArrayList of all tiles with values.
	 * 
	 * @return an arrayList of Cells.
	 ******************************************************************/
	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
		ArrayList<Cell> tiles = new ArrayList<Cell>();

		// Add all the tiles with value in an ArrayList
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[0].length; c++)
				if (board[r][c] != 0)
					tiles.add(new Cell(r, c, board[r][c]));

		return tiles;
	}

	/*******************************************************************
	 * Return the current game status
	 * 
	 * @return one of the possible game states
	 ******************************************************************/
	@Override
	public GameStatus getStatus() {
		int boardSize = ROWS * COLS;

		// loop all the tiles to determine if the player wins the game
		for (Cell c : getNonEmptyTiles())
			if (c.getValue() == WIN)
				return GameStatus.USER_WON;

		// check if the board is full
		if (getNonEmptyTiles().size() == boardSize)
			gameState = GameStatus.USER_LOST;
		else
			gameState = GameStatus.IN_PROGRESS;

		// check if its possible for the full board to move horizontally
		for (int r = 0; r < getRows(); r++)
			for (int c = 0; c < getCols() - 1; c++)
				if (board[r][c] == board[r][c + 1])
					gameState = GameStatus.IN_PROGRESS;

		// check if its possible for the full board to move vertically
		for (int c = 0; c < getCols(); c++)
			for (int r = 0; r < getRows() - 1; r++)
				if (board[r][c] == board[r + 1][c])
					gameState = GameStatus.IN_PROGRESS;

		return gameState;
	}

	/*******************************************************************
	 * Undo the most recent action, i.e. restore the board to its previous
	 * state.
	 *
	 * @throws IllegalStateException
	 *             when undo is not possible
	 ******************************************************************/
	@Override
	public void undo() {
		// throws an IllegalState if no more history step
		if (record.size() < 1)
			throw new IllegalStateException("No more past steps!");

		// replace the current board
		board = record.pop();
		
		SCORES = scoreRecord.pop();
	}

	/*******************************************************************
	 * Generates a random number 2 or 4
	 * 
	 * @return 20% chance 4 and 80% chance 2
	 ******************************************************************/
	private int randTwoFour() {
		int value = rand.nextInt(10);

		// Generates a 2 or 4, with 80% chance 2, and 20% chance 4
		if (value < 8)
			value = 2;
		else
			value = 4;
		return value;
	}

	/*******************************************************************
	 * Shift all values up, and nearby tiles with same value is merged. And
	 * renew the score if two tiles is merged
	 * 
	 * @return true if the board is moved
	 ******************************************************************/
	private boolean shiftUp() {
		// true if the board is shifted
		boolean moved = false;

		// This loop perform the shifting Up of a 1024 game
		for (int c = 0; c < board[0].length; c++) {
			ArrayList<Integer> nonZeros = new ArrayList<Integer>();

			// Stores non empty tiles into ArrayList
			for (int r = 0; r < board.length; r++)
				if (board[r][c] != 0)
					nonZeros.add(board[r][c]);

			// merged and remove tiles, add the points to scores
			for (int i = 0; i < nonZeros.size() - 1; i++)
				if (nonZeros.get(i).equals(nonZeros.get(i + 1))) {
					SCORES += (nonZeros.get(i) + nonZeros.get(i));
					nonZeros.set(i, nonZeros.get(i) * 2);
					nonZeros.remove(i + 1);
				}

			// place the values in array back to the board but shifted
			for (int i = 0; i < nonZeros.size(); i++)
				if (board[i][c] != nonZeros.get(i)) {
					moved = true;
					board[i][c] = nonZeros.get(i);
				}

			// replace rest of the values with a zero
			for (int i = nonZeros.size(); i < board.length; i++)
				board[i][c] = 0;

		}

		return moved;
	}

	/*******************************************************************
	 * Shift all values Left, and nearby tiles with same value is merged
	 * 
	 * @return true if the board is moved
	 ******************************************************************/
	private boolean shiftLeft() {
		// true if the board is shifted
		boolean moved = false;

		// This loop perform the shifting Left of a 1024 game
		for (int r = 0; r < board.length; r++) {
			ArrayList<Integer> nonZeros = new ArrayList<Integer>();

			// Stores non empty tiles into ArrayList
			for (int c = 0; c < board[0].length; c++)
				if (board[r][c] != 0)
					nonZeros.add(board[r][c]);

			// merged and remove tiles, add the points to scores
			for (int i = 0; i < nonZeros.size() - 1; i++)
				if (nonZeros.get(i).equals(nonZeros.get(i + 1))) {
					SCORES += (nonZeros.get(i) + nonZeros.get(i));
					nonZeros.set(i, nonZeros.get(i) * 2);
					nonZeros.remove(i + 1);
				}

			// place the values in array back to the board but shifted
			for (int i = 0; i < nonZeros.size(); i++)
				if (board[r][i] != nonZeros.get(i)) {
					moved = true;
					board[r][i] = nonZeros.get(i);
				}

			// replace rest of the values with a zero
			for (int i = nonZeros.size(); i < board[0].length; i++)
				board[r][i] = 0;
		}

		return moved;
	}

	/*******************************************************************
	 * Shift all values Right, and merged the same value nearby tiles
	 * 
	 * @return true if the board is moved
	 ******************************************************************/
	private boolean shiftRight() {
		// true if the board is shifted
		boolean moved = false;

		// This loop perform the shifting Right of a 1024 game
		for (int r = 0; r < board.length; r++) {
			ArrayList<Integer> nonZeros = new ArrayList<Integer>();

			// Stores non empty tiles into ArrayList
			for (int c = board[0].length - 1; c >= 0; c--)
				if (board[r][c] != 0)
					nonZeros.add(board[r][c]);

			// merged and remove tiles, add the points to scores
			for (int i = 0; i < nonZeros.size() - 1; i++)
				if (nonZeros.get(i).equals(nonZeros.get(i + 1))) {
					SCORES += (nonZeros.get(i) + nonZeros.get(i));
					nonZeros.set(i, nonZeros.get(i) * 2);
					nonZeros.remove(i + 1);
				}

			// place the values in array back to the board but shifted
			for (int i = 0; i < nonZeros.size(); i++)
				if (board[r][board[0].length - i - 1] != nonZeros
						.get(i)) {
					board[r][board[0].length - i - 1] = nonZeros.get(i);
					moved = true;
				}

			// replace rest of the values with a zero
			for (int i = 0; i < board[0].length - nonZeros.size(); i++)
				board[r][i] = 0;

		}

		return moved;
	}

	/*******************************************************************
	 * Shift all values Down, and merged the same value nearby tiles
	 * 
	 * @return true if the board is moved
	 ******************************************************************/
	private boolean shiftDown() {
		// true if the board is shifted
		boolean moved = false;

		// This loop perform the shifting Down of a 1024 game
		for (int c = 0; c < board[0].length; c++) {
			ArrayList<Integer> nonZeros = new ArrayList<Integer>();

			// Stores non empty tiles into ArrayList
			for (int r = board.length - 1; r >= 0; r--)
				if (board[r][c] != 0)
					nonZeros.add(board[r][c]);

			// merged and remove tiles, add the points to scores
			for (int i = 0; i < nonZeros.size() - 1; i++)
				if (nonZeros.get(i).equals(nonZeros.get(i + 1))) {
					SCORES += (nonZeros.get(i) + nonZeros.get(i));
					nonZeros.set(i, nonZeros.get(i) * 2);
					nonZeros.remove(i + 1);
				}

			// place the values in array back to the board but shifted
			for (int i = 0; i < nonZeros.size(); i++)
				if (board[board.length - i - 1][c] != nonZeros.get(i)) {
					board[board.length - i - 1][c] = nonZeros.get(i);
					moved = true;
				}

			// replace rest of the values with a zero
			for (int i = 0; i < board.length - nonZeros.size(); i++)
				board[i][c] = 0;

		}
		return moved;
	}

}
