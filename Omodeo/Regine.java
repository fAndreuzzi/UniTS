public class Regine {
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    boolean[][] table = new boolean[n][n];

    table = recCheck(table, 0, n);
    System.out.println(table == null ? "null" : printMatrix(table));
  }

  private static boolean[][] recCheck(boolean[][] table, int placed, int stop) {
    placed++;

    for(int i = 0; i < table.length; i++)
      for(int j = 0; j < table.length; j++) {
        if(!canPlaceHere(i,j,table)) continue;

        table[i][j] = true;
        if(placed == stop) return table;

        boolean[][] t = recCheck(table, placed, stop);
        if(t != null) return t;
        else table[i][j] = false;
      }

    return null;
  }

  private static boolean canPlaceHere(int i, int j, boolean[][] table) {
    return !ScaccUtils.isBusyRow(table, i) && !ScaccUtils.isBusyColumn(table, j) && !ScaccUtils.isBusyDiagonal(table, i,j);
  }

  private static String printMatrix(boolean[][] map) {
    StringBuilder builder = new StringBuilder();

    builder.append("   ");
    for(int i = 0; i < map.length; i++) {
      builder.append(' ').append(i).append(' ');
    }
    builder.append('\n');
    builder.append("   ");
    for(int i = 0; i < map.length; i++) {
      builder.append("---");
    }
    builder.append('\n');

    for(int y = 0; y < map.length; y++) {
      builder.append(y).append(' ').append('|');
      for(int x = 0; x < map.length; x++) {
        builder.append(' ').append(boolToString(map[x][y])).append(' ');
      }
      builder.append('|').append('\n');
    }

    builder.append("   ");
    for(int i = 0; i < map.length; i++) {
      builder.append("---");
    }

    return builder.toString();
  }

  private static String boolToString(boolean b) {
    return b ? "1" : "0";
  }

  private static class ScaccUtils {
    public static boolean isBusyColumn(boolean[][] table, int column) {
      for(int i = 0; i < table.length; i++) for(int j = 0; j < table.length; j++) if(table[i][column]) return true;
      return false;
    }

    public static boolean isBusyRow(boolean[][] table, int row) {
      for(int i = 0; i < table.length; i++) for(int j = 0; j < table.length; j++) if(table[row][i]) return true;
      return false;
    }

    public static boolean isBusyDiagonal(boolean[][] table, int x, int y) {
      int leftConstant = x-y;
      int rightConstant = x+y;

      for(int i = 0; i < table.length; i++) {
        // diagonale \
        int j = i - leftConstant;
        if(j >= 0 && j < table.length && table[i][j]) return true;

        // diagonale /
        j = rightConstant - i;
        if(j >= 0 && j < table.length && table[i][j]) return true;
      }

      return false;
    }
  }
}
