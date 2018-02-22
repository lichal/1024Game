package game1024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/***********************************************************************
 * A GUI class enable player to play a 1024 game, the class contains the basic
 * function of 1024 game, and also to resize the board.
 * 
 * @author Cheng Li
 * @version 6-21-2017
 **********************************************************************/
public class Game1024GUI extends JPanel {

	/** An array of all current tiles */
	private JPanel[][] tiles;

	private JLabel[][] labels;

	/** This is buttons for resize and slide board */
	private JButton left, right, up, down, undoBtn;

	/** The current instance of 1024 game */
	private NumberGame game;

	/** JPanels for score, and direction buttons */
	private JPanel panelStats, panelButton, panelTiles, mainPanel;

	/** JLabel labels for game statistics */
	private JLabel gameLabel, moveLabel, highLabel, currentLabel;

	/** Integer value for the game statistics */
	private int numberSlide, HIGH_SCORE, currentScore, numberPlayed;

	/** This is the menu bar */
	private static JMenuBar menus;

	/** this is the menu */
	private JMenu gameMenu;

	/** determine if the board is moved */
	private boolean moved = false;

	/** This is the menu items */
	private JMenuItem resize, newGame, quitGame;

	/** The keyListener for arrow keys to work for slide direction */
	private MyListener kyListener;

	/** The ActionListener */
	private ButtonListener butListener;

	/** The game frame */
	private static JFrame frame;

	/** Font size the value displayed in the cell */
	private Font myFont;

	/** TextField for resizing the board */
	private JTextField textRow, textCol, textWin;

	/*******************************************************************
	 * Creates and displays a 1024 Game Frame
	 ******************************************************************/
	public static void main(String[] args) {
		frame = new JFrame("Game 1024");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game1024GUI panel = new Game1024GUI();
		frame.getContentPane().add(panel);
		frame.setJMenuBar(menus);

		frame.pack();
		frame.setVisible(true);

	}

	/*******************************************************************
	 * The main GUI for the 1024 game, this method set up and add all components
	 * of 1024 game to the Game Frame
	 ******************************************************************/
	public Game1024GUI() {
		// cell value's font size
		myFont = new Font("Serif", Font.PLAIN, 30);

		// array of panels and labels for the cell
		tiles = new JPanel[4][4];
		labels = new JLabel[4][4];

		// instantiates the JPanels
		mainPanel = new JPanel();
		panelTiles = new JPanel();
		panelStats = new JPanel();
		panelButton = new JPanel();

		setLayout(new FlowLayout());

		// set the layout for each panel
		mainPanel.setLayout(new GridLayout(1, 2));
		panelTiles.setLayout(new GridLayout(4, 4));
		panelStats.setLayout(new GridLayout(2, 2));
		panelButton.setLayout(new GridLayout(4, 1));

		// Instantiates a new JPanel for every cell
		// And set a JLabel for every cell with the correct font
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < 4; c++) {
				tiles[r][c] = panelTiles();
				labels[r][c] = new JLabel("");
				labels[r][c].setHorizontalAlignment(JLabel.CENTER);
				tiles[r][c].add(labels[r][c]).setFont(myFont);
				panelTiles.add(add(tiles[r][c]));
				tiles[r][c].setBackground(colorChange(0));
			}

		// create a new 1024 game
		game = new NumberGame();
		game.resizeBoard(4, 4, 1024);
		game.reset();

		// replace the empty cell with the desired value in the cell
		for (Cell cell : game.getNonEmptyTiles()) {
			int r = cell.getRow();
			int c = cell.getCol();
			int v = cell.getValue();

			labels[r][c].setText("" + v);
			tiles[r][c].setBackground(colorChange(v));
		}

		// Instantiates the game Statistics and Labels
		numberPlayed = 0;
		numberSlide = 0;
		HIGH_SCORE = 0;
		currentScore = 0;

		// Instantiates the game statistic label
		gameLabel = new JLabel("  Game Played: " + numberPlayed);
		moveLabel = new JLabel("  Moves: " + numberSlide);
		highLabel = new JLabel("  High Score: " + HIGH_SCORE);
		currentLabel = new JLabel("  Scores: " + currentScore);

		// add game statistic label to panelStats
		panelStats.add(gameLabel);
		panelStats.add(moveLabel);
		panelStats.add(highLabel);
		panelStats.add(currentLabel);
		panelStats.setBackground(Color.WHITE);

		// new JPanel for buttons
		JPanel upBut = new JPanel();
		JPanel leftRightBut = new JPanel();
		JPanel downBut = new JPanel();

		upBut.setLayout(new GridLayout(1, 1));
		leftRightBut.setLayout(new GridLayout(1, 3));
		downBut.setLayout(new GridLayout(1, 1));

		// instantiate slide buttons
		right = new JButton(">");
		left = new JButton("<");
		up = new JButton("^");
		down = new JButton("v");
		undoBtn = new JButton("U");

		// add slide button to the desired panel
		upBut.add(up);

		leftRightBut.add(left);
		leftRightBut.add(undoBtn);
		leftRightBut.add(right);

		downBut.add(down);

		// add small button panel to the panelButton
		panelButton.add(upBut);
		panelButton.add(leftRightBut);
		panelButton.add(downBut);
		panelButton.add(panelStats);
		
		// instantiates new listener
		butListener = new ButtonListener();

		// adds the buttons to the listeners
		up.addActionListener(butListener);
		down.addActionListener(butListener);
		left.addActionListener(butListener);
		right.addActionListener(butListener);
		undoBtn.addActionListener(butListener);

		// add all the panel to the main panel
		mainPanel.add(panelTiles);
		mainPanel.add(panelButton);

		// add main panel to the JFrame
		add(mainPanel, BorderLayout.CENTER);

		// Instantiates menu item
		gameMenu = new JMenu("Game");
		newGame = new JMenuItem("New Game");
		resize = new JMenuItem("Resize");
		quitGame = new JMenuItem("Quit");

		// add selection to the menu
		gameMenu.add(newGame);
		gameMenu.add(resize);
		gameMenu.add(quitGame);

		// add new menu
		menus = new JMenuBar();
		menus.add(gameMenu);

		// add menu item to the listener
		newGame.addActionListener(butListener);
		resize.addActionListener(butListener);
		quitGame.addActionListener(butListener);

		// instantiates a new keyListener
		kyListener = new MyListener();

		// add the buttons to the keyListener
		undoBtn.addKeyListener(kyListener);
		up.addKeyListener(kyListener);
		down.addKeyListener(kyListener);
		right.addKeyListener(kyListener);
		left.addKeyListener(kyListener);
	}

	/*******************************************************************
	 * This class implements the ActionListener for the buttons
	 ******************************************************************/
	private class ButtonListener implements ActionListener {
		/***************************************************************
		 * This method determines which button is pressed, and performed the
		 * action for each button.
		 **************************************************************/
		public void actionPerformed(ActionEvent event) {

			// resize the board, only if the input values are valid
			if (resize == event.getSource()) {
				resizeDialog();
				slide();
				numberSlide = 0;
				moveLabel.setText("  Moves: " + numberSlide);
			}

			// slide the board right, and if moved, number moved +1
			if (right == event.getSource()) {
				moved = game.slide(SlideDirection.RIGHT);
				if (moved)
					slide();
			}

			// slide the board left, and if moved, number moved +1
			if (left == event.getSource()) {
				moved = game.slide(SlideDirection.LEFT);
				if (moved)
					slide();
			}

			// slide the board up, and if moved, number moved +1
			if (up == event.getSource()) {
				moved = game.slide(SlideDirection.UP);
				if (moved)
					slide();
			}

			// slide the board down, and if moved, number moved +1
			if (down == event.getSource()) {
				moved = game.slide(SlideDirection.DOWN);
				if (moved)
					slide();
			}

			// undo to the most recent step, only if undo is possible
			if (undoBtn == event.getSource()) {
				moved = false;
				checkUndo();
				slide();
			}

			// quit window
			if (event.getSource() == quitGame)
				System.exit(1);

			// start a new game with same win value and board size
			if (event.getSource() == newGame) {
				game.reset();
				numberPlayed++;
				slide();
				numberSlide = 0;
				moveLabel.setText("  Moves: " + numberSlide);
			}
		}
	}

	/*******************************************************************
	 * This method checks if its possible to under the game Minus 1 numberSlide
	 * if the board is undo
	 ******************************************************************/
	private void checkUndo() {
		try {
			game.undo();
		} catch (IllegalStateException exp) {
			JOptionPane.showMessageDialog(this, "Can't further undo!");
		}
		numberSlide--;
		if (numberSlide < 0)
			numberSlide = 0;
		
	}

	/*******************************************************************
	 * This method pop out a resize dialog with 3 textField
	 ******************************************************************/
	private void resizeDialog() {
		textCol = new JTextField();
		textRow = new JTextField();
		textWin = new JTextField();

		// sets the text field with a initial game format
		textCol.setText("" + 4);
		textRow.setText("" + 4);
		textWin.setText("" + 1024);

		int cols = 0;
		int rows = 0;
		int win = 0;

		
		// what is displayed on the dialog
		Object[] message = { "Rows: ", textRow, "Columns:", textCol,
				"Winning Value: ", textWin };

		int option = JOptionPane.showConfirmDialog(null, message,
				"Resize Board", JOptionPane.OK_CANCEL_OPTION);

		// close the dialog either if cancel and x is clicked
		if (option == JOptionPane.CANCEL_OPTION)
			System.out.println("Resize Canceled");
		else if (option == JOptionPane.CLOSED_OPTION)
			System.out.println("Resize Canceled");

		// check if the integer valid
		else if (checkInt(textCol.getText())
				&& checkInt(textRow.getText())
				&& checkInt(textWin.getText())) {

			cols = Integer.parseInt(textCol.getText());
			rows = Integer.parseInt(textRow.getText());
			win = Integer.parseInt(textWin.getText());

			try {
				// checks if user try to put in integer less than 1
				if (cols < 2 || rows < 2 || win < 2)
					throw new IllegalArgumentException(
							"Invalid Row and Column");
				// display an error message for negative integer
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this,
						"please use integer greater than 2!");
				return;
			}

			try {
				// check if the winningValue is a power of 2
				int checkValue = win;
				if (checkValue % 2 == 0)
					while (checkValue != 2)
						checkValue /= 2;
				if (checkValue != 2)
					throw new IllegalArgumentException();

				// display an message for invalid winning
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this,
						"Winning Value must be a power of 2!");
				return;
			}

			// resize the board
			if (option == JOptionPane.OK_OPTION) {
				resizing(rows, cols, win);
				numberPlayed++;
			}
		}
	}

	/*******************************************************************
	 * This method resize the game board, and change the winning value
	 * 
	 * @param row
	 *            the desired row for resized board
	 * @param col
	 *            the desired columns for resized board
	 * @param winValue
	 *            the desired winning value for the new game
	 ******************************************************************/
	private void resizing(int row, int col, int winValue) {
		panelTiles.removeAll();
		panelTiles.setLayout(new GridLayout(row, col));

		tiles = new JPanel[row][col];
		labels = new JLabel[row][col];

		// Instantiates a new JPanel for every cell
		// And set a JLabel for every cell with the correct font
		for (int r = 0; r < row; r++)
			for (int c = 0; c < col; c++) {
				tiles[r][c] = panelTiles();
				labels[r][c] = new JLabel(" ");
				labels[r][c].setHorizontalAlignment(JLabel.CENTER);
				tiles[r][c].add(labels[r][c]).setFont(myFont);
				panelTiles.add(add(tiles[r][c]));
				tiles[r][c].setBackground(colorChange(0));
			}

		// resize and reset the board
		game.resizeBoard(row, col, winValue);
		game.reset();
		frame.pack();

		// replace the desired empty cell with correct value
		for (Cell cell : game.getNonEmptyTiles()) {
			int r = cell.getRow();
			int c = cell.getCol();
			int v = cell.getValue();

			labels[r][c].setText("" + v);
			tiles[r][c].setBackground(colorChange(v));
		}

		panelTiles.revalidate();
		repaint();
	}

	/*******************************************************************
	 * This method checks if user entered text is a valid integer
	 * 
	 * @param num
	 *            the user entered text
	 * @return boolean return true or false foe valid integer or not
	 ******************************************************************/
	private boolean checkInt(String num) {
		boolean isValid = true;

		try {
			int val = Integer.parseInt(num);
		}
		// display an error message if the integer is not valid
		catch (NumberFormatException e) {
			isValid = false;
			JOptionPane.showMessageDialog(this,
					"Please enter an integer!");
		}
		return isValid;
	}

	/*******************************************************************
	 * This method renew the Game Label of cells to the Correct value
	 ******************************************************************/
	private void slide() {
		// replace the cell with all zeros, and set the background color
		for (int r = 0; r < game.getRows(); r++)
			for (int c = 0; c < game.getCols(); c++) {
				labels[r][c].setText(" ");
				tiles[r][c].setBackground(colorChange(0));
			}

		// replace the cell with correct value and background color
		for (Cell cell : game.getNonEmptyTiles()) {
			int r = cell.getRow();
			int c = cell.getCol();
			int v = cell.getValue();
			labels[r][c].setText("" + v);
			tiles[r][c].setBackground(colorChange(v));
		}

		// ask the player if they want to play again when win or lose
		winLoseDialog();

		// renew the game statistics every movement
		renewStat();
		
		frame.pack();
	}

	private void winLoseDialog() {
		// determine if the user win or lose
		if (game.getStatus() == GameStatus.USER_WON) {
			Object[] message = { "Congratz! You Win! New Game?" };
			int option = JOptionPane.showConfirmDialog(null, message,
					"You Win!", JOptionPane.YES_NO_OPTION);
			
			// call the resize panel for a new game
			if (option == JOptionPane.YES_OPTION) {
				resizeDialog();
			}
			// close the game if the user don't want to play again
			else if (option == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(this,
						"Thanks for Playing!");
				System.exit(1);
			} else if (option == JOptionPane.CLOSED_OPTION) {
				JOptionPane.showMessageDialog(this,
						"Thanks for Playing!");
				System.exit(1);
			}

		} else if (game.getStatus() == GameStatus.USER_LOST) {
			Object[] message = { "Sorry! You Lose! New Game?" };
			int option = JOptionPane.showConfirmDialog(null, message,
					"You Lose!", JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				resizeDialog();
			}
			// close the game if the user don't want to play again
			else if (option == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(this,
						"Thanks for Playing!");
				System.exit(1);
			} else if (option == JOptionPane.CLOSED_OPTION) {
				JOptionPane.showMessageDialog(this,
						"Thanks for Playing!");
				System.exit(1);
			}
		}
	}

	/*******************************************************************
	 * This method renews the game statistics in the game board
	 ******************************************************************/
	private void renewStat() {
		// renew the score after every slide
		currentScore = game.getScore();
		currentLabel.setText("  Scores: " + currentScore);

		// renew high score
		highLabel.setText("  High Score: " + highestScore());

		// renew number moved, only if the board is actually moved
		if (moved)
			numberSlide++;
		moveLabel.setText("  Moves: " + numberSlide);

		// renew the number game played
		gameLabel.setText("  Game Played: " + numberPlayed);
	}

	/*******************************************************************
	 * This method determines the current highest score and return it
	 * 
	 * @return Current Highest Score
	 ******************************************************************/
	private int highestScore() {
		if (game.getScore() > HIGH_SCORE)
			HIGH_SCORE = game.getScore();
		return HIGH_SCORE;
	}

	/*******************************************************************
	 * This class implements the KeyListener for the arrow keys
	 ******************************************************************/
	private class MyListener implements KeyListener {

		public void keyTyped(KeyEvent e) {

		}

		/***************************************************************
		 * Enable to play the game with arrow key
		 **************************************************************/
		public void keyPressed(KeyEvent e) {
			moved = false;

			// allow user to start a new game by just pressing N key
			if(KeyEvent.VK_N == e.getKeyCode()){
				game.reset();
				numberPlayed++;
				slide();
				numberSlide = 0;
				moveLabel.setText("  Moves: " + numberSlide);
			}
			
			// allow the user to undo by pressing the U key
			if (KeyEvent.VK_U == e.getKeyCode()) {
				moved = false;
				checkUndo();
				slide();
			}
			// allow user to resize the board by just pressing the key R
			if (KeyEvent.VK_R == e.getKeyCode()) {
				numberPlayed++;
				resizeDialog();
				slide();
				numberSlide = 0;
				moveLabel.setText("  Moves: " + numberSlide);
			}

			// allow user to slide direction with arrow key up
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				moved = game.slide(SlideDirection.UP);
				if (moved)
					slide();
			}

			// allow user to slide direction with arrow key down
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				moved = game.slide(SlideDirection.DOWN);
				if (moved)
					slide();
			}

			// allow user to slide direction with arrow key right
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				moved = game.slide(SlideDirection.RIGHT);
				if (moved)
					slide();
			}

			// allow user to slide direction with arrow key left
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				moved = game.slide(SlideDirection.LEFT);
				if (moved)
					slide();
			}
		}

		public void keyReleased(KeyEvent e) {

		}

	}

	/*******************************************************************
	 * This method creates a new JPanel to hold the tiles for the 1024 game
	 * value.
	 * 
	 * @return a new JPanel of cell
	 ******************************************************************/
	private JPanel panelTiles() {
		JPanel tilePanels = new JPanel();

		tilePanels.setLayout(new GridLayout());
		tilePanels.setPreferredSize(new Dimension(75, 75));

		// set the border to make it look better
		tilePanels.setBorder(
				BorderFactory.createLineBorder(Color.darkGray, 2));

		return tilePanels;
	}

	/*******************************************************************
	 * This method determine which color use for the cell background
	 * 
	 * @param value
	 *            the current value of the tile
	 * @return background color for the cell panels
	 ******************************************************************/
	private Color colorChange(int value) {
		Color color = Color.lightGray;

		switch (value) {
		case 0:
			color = Color.lightGray;
			break;
		case 2:
			color = Color.pink;
			break;
		case 4:
			color = new Color(200, 150, 170);
			break;
		case 8:
			color = new Color(200, 255, 170);
			break;
		case 16:
			color = new Color(255, 200, 128);
			break;
		case 32:
			color = new Color(255, 255, 85);
			break;
		case 64:
			color = new Color(200, 255, 43);
			break;
		case 128:
			color = new Color(255, 255, 0);
			break;
		case 256:
			color = new Color(213, 213, 0);
			break;
		case 512:
			color = new Color(170, 170, 0);
			break;
		case 1024:
			color = new Color(128, 128, 0);
			break;
		case 2048:
			color = new Color(85, 85, 0);
			break;
		case 4096:
			color = new Color(45, 45, 0);
			break;
		}
		return color;
	}

}
