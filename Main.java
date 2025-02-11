import java.util.Scanner;

public class Main {
    private static final int DIM = 9;

    public static void main(String[] args) {
        // initialize initial Sudoku board
        int[][] initialBoard = new int[DIM][DIM];

        // read inputs from the user
        Scanner input = new Scanner(System.in);
        String curRow;

        for (int i = 0; i < DIM; i++) {
            System.out.println("Enter row " + (i+1) + " (input 0 if the cell is blank)");
            curRow = input.nextLine();
            for (int j = 0; j < DIM; j++)
                initialBoard[i][j] = Character.getNumericValue(curRow.charAt(j));
        }

        Sudoku sudoku = new Sudoku(initialBoard);
        sudoku.run();
    }
}
