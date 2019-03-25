import java.util.Arrays;

public class Diagonali {

  public static void main(String[] args) {
    int[][] mat = {
      {1,2,3,4},
      {5,6,7,8},
      {9,10,11,12},
      {13,14,15,16}
    };

    System.out.println(matrixToString(mat));

    int[][] d = rightwiseDiagonals(mat);
    for(int[] dg : d) System.out.println(Arrays.toString(dg));
  }

  // quadrato!
  public static int[][] rightwiseDiagonals(int[][] mat) {
    int[][] diagonals = new int[(mat.length - 1) * 2 + 1][];

    int diagCounter = 0;

    // scorre le righe lungo la colonna 0
    for(int i = mat.length - 1; i >= 0; i--) {
      final int constant = i;
      final int diagonalSize = mat.length - i;
      int[] diagonal = new int[diagonalSize];

      for(int row = i; row < mat.length; row++) {
        int col = row - constant;
        diagonal[row - i] = mat[row][col];
      }

      diagonals[diagCounter++] = diagonal;
    }

    // scorre le colonne lungo la riga 0
    for(int j = 1; j < mat.length; j++) {
      final int constant = -1 * j;
      final int diagonalSize = mat.length - j;
      int[] diagonal = new int[diagonalSize];

      for(int col = j; col < mat.length; col++) {
        int row = col + constant;
        diagonal[col - j] = mat[row][col];
      }

      diagonals[diagCounter++] = diagonal;
    }

    return diagonals;
  }

  private static String matrixToString(int[][] mat) {
    StringBuilder builder = new StringBuilder();

    builder.append("   ");
    for(int i = 0; i < mat.length; i++) {
      builder.append(' ').append(i).append(' ');
    }
    builder.append('\n');
    builder.append("   ");
    for(int i = 0; i < mat.length; i++) {
      builder.append("---");
    }
    builder.append('\n');

    for(int y = 0; y < mat.length; y++) {
      builder.append(y).append(' ').append('|');
      for(int x = 0; x < mat.length; x++) {
        builder.append(' ').append(mat[y][x]).append(' ');
      }
      builder.append('|').append('\n');
    }

    builder.append("   ");
    for(int i = 0; i < mat.length; i++) {
      builder.append("---");
    }

    return builder.toString();
  }
}
