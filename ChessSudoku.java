package finalproject;

import java.util.*;
import java.io.*;


public class ChessSudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
     * a standard Sudoku puzzle, SIZE is 3 and N is 9.
     */
    public int SIZE, N;
    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0.
     */
    public int grid[][];

    /* Booleans indicating whether of not one or more of the chess rules should be
     * applied to this Sudoku.
     */
    public boolean knightRule;
    public boolean kingRule;
    public boolean queenRule;


    // Field that stores the same Sudoku puzzle solved in all possible ways
    public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();


    // returns false if knight rule is respected
    private boolean check_knight_rule(int row, int col, int num) {
        if(row-2 >= 0 && col-1 >= 0) {
            if(this.grid[row-2][col-1] == num)
                return true;
        }
        if(row-2 >= 0 && col+1 < this.N) {
            if(this.grid[row-2][col+1] == num)
                return true;
        }


        if(row+2 < this.N && col-1 >= 0) {
            if(this.grid[row+2][col-1] == num)
                return true;
        }
        if(row+2 < this.N && col+1 < this.N) {
            if(this.grid[row+2][col+1] == num)
                return true;
        }


        if(col-2 >= 0 && row-1 >= 0) {
            if(this.grid[row-1][col-2] == num)
                return true;
        }
        if(col-2 >= 0 && row+1 < this.N) {
            if(this.grid[row+1][col-2] == num)
                return true;
        }


        if(col+2 < this.N && row-1 >= 0) {
            if(this.grid[row-1][col+2] == num)
                return true;
        }
        if(col+2 < this.N && row+1 < this.N) {
            if(this.grid[row+1][col+2] == num)
                return true;
        }
        return false;

    }
    // return true if king rule respected
    private boolean check_king_rule(int row, int col, int num) {
        if(col-1 < 0) {
            if(row-1 < 0) {
                if(this.grid[row+1][1] == num)
                    return false;
            }
            if(row+1 < this.N) {
                if(this.grid[row-1][1] == num)
                    return false;
            }
            else{
                if(this.grid[row+1][1] == num || this.grid[row-1][1] == num)
                    return false;
            }
        }
        if(col+1 > this.N){
            if(row-1 < 0) {
                if(this.grid[row+1][col-1] == num)
                    return false;
            }
            if(row+1 < this.N) {
                if(this.grid[row-1][col-1] == num)
                    return false;
            }
            else{
                if(this.grid[row+1][col-1] == num || this.grid[row-1][col-1] == num)
                    return false;
            }
        }
        else {
            if(row-1 < 0) {
                if(this.grid[row+1][col+1] == num || this.grid[row+1][col-1] == num)
                    return false;
            }
            if(row+1 < this.N) {
                if(this.grid[row-1][col-1] == num || this.grid[row-1][col+1] == num)
                    return false;
            }
            else{
                if(this.grid[row-1][col-1] == num || this.grid[row+1][col-1] == num || this.grid[row-1][col+1] == num || this.grid[row+1][col+1] == num)
                    return false;
            }
        }
        return true;
    }
    // return true if queen rule respected
    private boolean check_queen_rule(int row, int col, int num) {
        int temp1a = row;
        int temp1b = col;
        int temp2a = row;
        int temp2b = col;
        int temp3a = row;
        int temp3b = col;
        int temp4a = row;
        int temp4b = col;
        boolean check = true;

        while(check) {

            if(temp1a != this.N-1 && temp1b != this.N-1){     // from top left to bottom right
                if(this.grid[temp1a++][temp1b++] == num)
                    return false;
            }
            else if( temp2a != 0 && temp2b != 0){             // from top right to bottom left
                if(this.grid[temp2a--][temp2b--] == num)
                    return false;
            }
            else if(temp3a != 0 && temp3b != this.N-1){        // from bottom left to top right
                if(this.grid[temp3a--][temp3b++] == num)
                    return false;
            }
            else if(temp4a != this.N-1 && temp4b != 0){         // from bottom right to top left
                if(this.grid[temp4a++][temp4b--] == num)
                    return false;
            }
            else{check = false;}
        }
        return true;
    }

        // check if a number is in a certain row more than once if not return false
    private boolean inRow(int row, int num) {
        for(int i = 0; i < this.N; i++) {
            if(this.grid[row][i] == num) {
                return true;
            }
        }
        return false;
    }

    // check if a number is in a certain column more than once if not return false
    private boolean inCol(int col, int num) {
        for(int i = 0; i < this.N; i++) {
            if(this.grid[i][col] == num) {
                return true;
            }
        }
        return false;
    }


    // check if a number is inside of the sub sudoku more than one time
    private boolean sub_Sudoku(int row, int col, int num) {
        int r = row - row%this.SIZE;
        int c = col - col%this.SIZE;
        for(int i = r; i<r+this.SIZE; i++) {
            for(int k = c; k<c+this.SIZE; k++) {
                if(this.grid[i][k] == num)
                    return true;
            }
        }
        return false;
    }

    private boolean check_constraint(int row, int col, int num) {

        if(this.knightRule && this.kingRule && this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num) && !check_knight_rule(row, col, num)
                    && check_queen_rule(row, col, num) && check_king_rule(row, col, num);
        else if(!this.knightRule && this.kingRule && this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num)
                    && check_queen_rule(row, col, num) && check_king_rule(row, col, num);
        else if(this.knightRule && !this.kingRule && this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num) && !check_knight_rule(row, col, num)
                    && check_queen_rule(row, col, num);
        else if(this.knightRule && this.kingRule && !this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num) && !check_knight_rule(row, col, num)
                    && check_king_rule(row, col, num);
        else if(!this.knightRule && !this.kingRule && this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num) && check_queen_rule(row, col, num);
        else if(this.knightRule && !this.kingRule && !this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num) && !check_knight_rule(row, col, num);
        else if(!this.knightRule && this.kingRule && !this.queenRule)
            return !inRow(row, num) && !inCol(col, num)
                    && !sub_Sudoku(row, col, num) && check_king_rule(row, col, num);

        else{
        return !inRow(row, num) && !inCol(col, num)
                && !sub_Sudoku(row, col, num);
        }
    }
    private boolean checkzeros(){   // check if the grid still has unsolved 0
        for(int i = 0; i<this.N; i++) {
            for(int k = 0; k<this.N;k++) {
                if(this.grid[i][k] == 0)
                    return false;
            }
        }
        return true;
    }

    /* The solve() method should remove all the unknown characters ('x') in the grid
     * and replace them with the numbers in the correct range that satisfy the constraints
     * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL
     * possible solutions and store them in the field named solutions. */
    public void solve(boolean allSolutions) {

        for(int row = 0; row<this.N; row++) {
            for(int col = 0; col<this.N; col++) {
                if(this.grid[row][col] == 0) {
                    for(int num = 1; num<= this.N; num++) {
                        if(check_constraint(row, col, num)){
                            this.grid[row][col] = num;
                            solve(allSolutions);

                            if(checkzeros()){
                                if(allSolutions){
                                    ChessSudoku allsol = new ChessSudoku(this.SIZE);
                                    for(int i = 0; i<this.N; i++) {
                                        for(int k = 0; k<this.N; k++){
                                            allsol.grid[i][k] = this.grid[i][k];
                                        }
                                    }
                                    this.solutions.add(allsol);
                                    this.grid[row][col] = 0;
                                }
                                return;
                            }
                        else{
                            this.grid[row][col] = 0;
                            }
                        }
                    }
                    return;
                }
            }
        }
    }



    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE METHODS BELOW THIS LINE. */
    /*****************************************************************************/

    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public ChessSudoku( int size ) {
        SIZE = size;
        N = size*size;

        grid = new int[N][N];
        for( int i = 0; i < N; i++ )
            for( int j = 0; j < N; j++ )
                grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width ) {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print() {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( grid[i][j] ), digits );
                // Print the vertical lines between boxes
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input,
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception {
        InputStream in = new FileInputStream("C:\\Users\\gzr03\\IdeaProjects\\finalproject\\medium3x3_twelveSolutions.txt");

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        ChessSudoku s = new ChessSudoku( puzzleSize );

        // You can modify these to add rules to your sudoku
        s.knightRule = false;
        s.kingRule = false;
        s.queenRule = false;

        // read the rest of the Sudoku puzzle
        s.read( in );

        System.out.println("Before the solve:");
        s.print();
        System.out.println();

        // Solve the puzzle by finding one solution.
        s.solve(false);

        // Print out the (hopefully completed!) puzzle
        System.out.println("After the solve:");
        s.print();
        Iterator<ChessSudoku> itr = s.solutions.iterator();
        while(itr.hasNext()){
            itr.next().print();
            System.out.println();
        }
    }
}

