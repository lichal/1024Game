package game1024;

/************************************************
A simple data structure to maintain information
about one tile in the 1024 game:

@author Hans Dulimarta 
@version Summer 2014
************************************************/
public class Cell implements Comparable<Cell> {
    
    /** row position of the tile */
    private int row;
    
    /** column position of the tile */
    private int column;
    
    /** numerical value of the tile 
        a zero represents a blank tile */
    private int value;

/***********************************************
Default constructor invokes the primary
constructor with initial values
***********************************************/    
    public Cell(){
        this(0,0,0);
    }
    
/***********************************************
Constructor sets initial values
@param r row position
@param c column position
@param v numerical value
***********************************************/    
    public Cell (int r, int c, int v){
        row = r;
        column = c;
        value = v;
    }
    
/***********************************************
@return the row position
***********************************************/    
    public int getRow(){
        return row;
    }
    
/***********************************************
@return the column position
***********************************************/    
    public int getCol(){
        return column;
    }
    
/***********************************************
@return the numerical value
***********************************************/    
    public int getValue(){
        return value;
    }

/***********************************************
This override method allows ArrayList operations
to maintain sorted order.  Note that this class
'implements' Comparable which requires the following
method to be provided.

@param other a cell to be compared
@return a positive number if 'this' comes
after 'other' in sorted order
@Override
***********************************************/      
    public int compareTo (Cell other) {
        if (this.row < other.row) return -1;
        if (this.row > other.row) return +1;

        // break the tie using column 
        if (this.column < other.column) return -1;
        if (this.column > other.column) return +1;

        // break the tie using the value
        return this.value - other.value;
    }
}

