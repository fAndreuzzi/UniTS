import java.util.Arrays;

public class Determinante {

  private static final boolean DEBUG = false;

  public static void main(String[] args) {
    int[][] matrix = {
      {1,4,5,7,8},
      {6,4,1,10,83},
      {8,8,6,0,37},
      {38,472,4,848,48},
      {38,72,3,27,3}
    };

    System.out.println(determinante(matrix));
  }

  // calcola il determinante scorrendo lungo la colonna 0
  public static int determinante(int[][] matrix) {
    if(DEBUG) printMatrix(matrix);
    if(!isSquare(matrix)) throw new UnsupportedOperationException("matrice non quadrata");

    // chiude l'albero ricorsivo
    if(matrix.length == 1) return matrix[0][0];
    else {
      int det = 0;

      for(int j = 0; j < matrix.length; j++) {
        det += Math.pow(-1, j) * matrix[j][0] * determinante(subMatrix(matrix, j, 0));
      }

      return det;
    }
  }

  // return a square subMatrix of size matrix.length - 1
  public static int[][] subMatrix(int[][] matrix, int skipRow, int skipColumn) {
    if(DEBUG) printMatrix(matrix);
    if(DEBUG) System.out.println("start: " + matrix.length);

    if(!isSquare(matrix)) throw new UnsupportedOperationException("matrice non quadrata");

    int[][] sub = new int[matrix.length - 1][matrix.length - 1];

    int row = 0, column = 0;
    int oldRow = -1, oldColumn = -1;

    while(row < matrix.length - 1) {
      if(DEBUG) System.out.println("row: " + row);

      oldRow++;

      if(skipRow == oldRow) continue;
      else {
        while(column < matrix.length - 1) {
          oldColumn++;

          if(skipColumn == oldColumn) continue;
          else {
            if(DEBUG) System.out.println(String.format("attempt wt %d,%d in %d,%d", oldRow, oldColumn, row, column));

            sub[row][column] = matrix[oldRow][oldColumn];

            column++;
          }
        }

        row++;
        column = 0;
        oldColumn = -1;
      }
    }

    if(DEBUG) System.out.println("end: " + sub.length);

    return sub;
  }

  private static void printMatrix(int[][] matrix) {
    if(matrix == null || matrix.length == 0) System.out.println("empty");
    else {
      for(int r = 0; r < matrix.length; r++) {
        System.out.println(Arrays.toString(matrix[r]));
      }
    }
  }

  public static boolean isSquare(int[][] matrice) {
    if(matrice == null || matrice.length == 0) return false;
    for(int j = 0; j < matrice.length; j++) {
      if(matrice[j].length != matrice.length) return false;
    }

    return true;
  }
}
