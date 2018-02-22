package game1024;

/*******************************************
Enumerated value for game status.  Helps
code to use words instead of numbers.

IN_PROGRESS - game still in progress 
USER_WON - player wins by reaching goal value
USER_LOST - no possible moves and no tiles with the goal value

Example Usage:
GameStatus stat = GameStatus.USER_WON;

@author Hans Dulimarta
@version Feb 08, 2016
*******************************************/
public enum GameStatus {
    IN_PROGRESS, USER_WON, USER_LOST    
}

