package game1024;

import java.util.ArrayList;
import java.util.Scanner;
/***********************************************
A simple text interface for the 1024 game

@author Hans Dulimarta 
@version Jun 27, 2016.
***********************************************/
public class TextUI {

    /** the game logic */
    private NumberSlider game;
    private int[][] grid;
    private int CELL_WIDTH = 3;
    private int ROWS = 2;
    private int COLS = 2;
    private int WIN = 16;
    private String NUM_FORMAT, BLANK_FORMAT;
    private Scanner inp;

    public static void main(String[] arg) {
        TextUI t = new TextUI();
        t.playLoop();
    }
    
    public TextUI() {
        game = new NumberGame();

        if (game == null) {
            System.err.println ("*---------------------------------------------*");
            System.err.println ("| You must first modify the UI program.       |");
            System.err.println ("| Look for the first TODO item in TextUI.java |");
            System.err.println ("*---------------------------------------------*");
            System.exit(0xE0);
        }
        grid = new int[ROWS][COLS];

        /* Set the string to %4d */
        NUM_FORMAT = String.format("%%%dd", CELL_WIDTH + 1);

        /* Set the string to %4s, but without using String.format() */
        BLANK_FORMAT = "%" + (CELL_WIDTH + 1) + "s";
        inp = new Scanner(System.in);
    }

    private void renderBoard() {
        
        // reset all the 2D array elements to ZERO
        for (int k = 0; k < grid.length; k++)
            for (int m = 0; m < grid[k].length; m++)
                grid[k][m] = 0;
                
        // fill the 2D array using information for non-empty tiles
        for (Cell c : game.getNonEmptyTiles())
            grid[c.getRow()][c.getCol()] = c.getValue();

        // print the 2D array using dots for a blank space
        System.out.println();
        for (int k = 0; k < grid.length; k++) {
            for (int m = 0; m < grid[k].length; m++)
                if (grid[k][m] == 0)
                    System.out.printf (BLANK_FORMAT, ".");
                else
                    System.out.printf (NUM_FORMAT, grid[k][m]);
            System.out.println();
        }
    }

    /*********************************************
     * The main loop for playing a SINGLE game. Notice
     * this method contains NO GAME LOGIC! Its only 
     * task is to accept user input and invoke the 
     * appropriate methods in the game engine.
     ********************************************/
    public void playLoop() {
        char choice;
        
        // start the game
        game.resizeBoard(ROWS, COLS, WIN);
        renderBoard();

        // continue until quit or game over
        do{
            // prompt player for choice
            System.out.print ("Slide direction (W, S, Z, A), " +
                "[U]ndo or [Q]uit? ");
            choice = inp.next().trim().toUpperCase().charAt(0);
            
            // respond to choice
            switch (choice) {
                case 'W':  
                    game.slide(SlideDirection.UP);
                    break;
                case 'S':
                    game.slide(SlideDirection.RIGHT);
                    break;
                case 'Z':
                    game.slide(SlideDirection.DOWN);
                    break;
                case 'A':
                    game.slide(SlideDirection.LEFT);             
                    break;
                case 'U':
                    try {
                        game.undo();
                    } catch (IllegalStateException exp) {
                        System.err.println ("Can't undo now.");
                    }
            }
            renderBoard();
        }while (choice != 'Q' && game.getStatus() == GameStatus.IN_PROGRESS);

        // Let player no how she did
        switch (game.getStatus()) {
            case IN_PROGRESS:
                System.out.println ("Thanks for playing!");
                break;
            case USER_WON:
                System.out.println ("Congratz");
                break;
            case USER_LOST:
                System.out.println ("Sorry....!");
                break;

        }
    }


}

