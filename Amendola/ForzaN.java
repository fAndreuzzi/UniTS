import java.util.Scanner;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;

public class ForzaN {

  public static void main(String[] args){
    Scanner s = new Scanner(System.in);

    System.out.println("Dimensione scacchiera... ");
    int n = s.nextInt();

    System.out.println("Forza... ");
    int winN = s.nextInt();

    System.out.println("Inserire un segnalino per ogni giocatore... ");
    Queue<Character> q = new ArrayDeque<Character>();
    while(true) {
      char mark = s.next().charAt(0);
      if(mark == '0') break;
      q.add(mark);
    }

    char[] markers = new char[q.size()];
    for(int count = 0; count < markers.length; count++) markers[count] = q.poll();

    Scacchiera table = new Scacchiera(n, markers, winN);

    int turn = 0;
    while(true) {
      System.out.println(String.format("Giocatore %d", turn + 1));

      String input = s.nextLine();
      while(input.length() == 0) input = s.nextLine();

      try {
        while(!table.insert(turn, Integer.parseInt(input))) {
          System.out.println("Colonna non valida");
          input = s.nextLine();
          while(input.length() == 0) input = s.nextLine();
        }
      } catch(Exception e) {
        if(input.equals("eq")) table.generateEarthquake();
      }

      table.show();

      int winner = table.checkWinner();
      if(winner != -1) {
        System.out.println(String.format("Il giocatore %d ha vinto", turn + 1));
        break;
      }

      turn = nextTurn(markers.length, turn);
    }
  }

  public static int nextTurn(int length, int now) {
    return (now+1) % length;
  }

  private static class Scacchiera {
    int[][] table;
    char[] markers;
    int winN;
    Random random;

    public Scacchiera(int n, char[] markers, int winN) {
      this.markers = markers;
      this.winN = winN;
      this.random = new Random();

      table = new int[n][n];
      emptyInit();
    }

    private void emptyInit() {
      for(int i = 0; i < table.length; i++) for(int j = 0; j < table[i].length; j++) table[i][j] = -1;
    }

    public boolean busy(int row, int col) {
      return table[row][col] != -1;
    }

    public int firstAvailableRow(int col) {
      if(col < 0 || col >= table.length) return -1;

      int i;
      for(i = table[col].length - 1; i >= 0 && busy(i,col); i--) {}
      return i;
    }

    public boolean insert(int turn, int col) {
      int index = firstAvailableRow(col);
      if(index != -1) table[index][col] = turn;
      return index != -1;
    }

    public void generateEarthquake() {
      boolean[] bm = new boolean[table.length];
      for(int i = 0; i < bm.length; i++) bm[i] = false;

      for(int col = 0; col < table.length; col++) {
        earthQuake(bm, col);
      }
    }

    private void earthQuake(boolean[] bitmap, int column) {
      if(bitmap[column]) return;
      bitmap[column] = true;

      int to = random.nextInt(3) - 1 + column;
      //System.out.println(String.format("shift col %d to %d", column, to));
      if(to < 0 || to >= table.length) reset(table, column);
      else if(column == to) {}
      else {
        int[] copy = getColumn(table, column);

        earthQuake(bitmap, to);
        pushColumn(table, column, to);

        if(eq(getColumn(table, column), copy)) reset(table,column);
      }
    }

    private static void pushColumn(int[][] matrix, int from, int to) {
      mergeColumns(matrix, getColumn(matrix, from), to);
    }

    private static int[] getColumn(int[][] matrix, int col) {
      int[] c = new int[matrix.length];
      for(int row = 0; row < matrix.length; row++) {
        c[row] = matrix[row][col];
      }
      return c;
    }

    private static boolean eq(int[] c1, int[] c2) {
      for(int i = 0; i < Math.min(c1.length, c2.length); i++) if(c1[i] != c2[i]) return false;
      return true;
    }

    // sovrascrive, o lascia se serve
    private static void mergeColumns(int[][] matrix, int[] values, int col) {
      for(int row = 0; row < matrix.length; row++) if(values[row] != -1) matrix[row][col] = values[row];
    }

    private static void reset(int[][] matrix, int col) {
      for(int row = 0; row < matrix.length; row++) matrix[row][col] = -1;
    }

    public int checkWinner() {
      for(int i = 0; i < table.length; i++) {
        int id = checkRow(i);
        if(id != -1) return id;

        id = checkColumn(i);
        if(id != -1) return id;
      }
      return checkDiagonals();
    }

    private int checkColumn(int col) {
      int id = table[0][col];
      int count = 1;

      for(int i = 1; i < table.length; i++) {
        if(table[i][col] != id) {
          id = table[i][col];
          count = 1;
        } else if(id != -1) {
          count++;
          if(count >= winN) return id;
        }
      }

      return -1;
    }

    private int checkRow(int row) {
      int id = table[row][0];
      int count = 1;

      for(int i = 1; i < table.length; i++) {
        if(table[row][i] != id) {
          id = table[row][i];
          count = 1;
        } else if(id != -1) {
          count++;
          if(count >= winN) return id;
        }
      }

      return -1;
    }

    private int checkDiagonals() {
      /*int leftId = table[0][0];
      int rightId = table[0][table.length - 1];

      boolean testLeft = true, testRight = true;
      for(int row = 0; row < table.length; row++) {
        int col = row;
        if(testLeft && table[row][col] != leftId) testLeft = false;

        col = table.length - row - 1;
        if(testRight && table[row][col] != rightId) testRight = false;

        if(!testLeft && !testRight) return -1;
      }

      if(testLeft) return leftId;
      if(testRight) return rightId;
      return -1;*/

      for(int col = 0; col + winN <= table.length; col++) {
        int id = checkRightDiagonalFrom(0,col);
        if(id != -1) return id;
      }

      for(int row = 1; row + winN <= table.length; row++) {
        int id = checkRightDiagonalFrom(row,0);
        if(id != -1) return id;
      }

      for(int col = table.length - 1; col - winN + 1 >= 0; col--) {
        int id = checkLeftDiagonalFrom(0,col);
        if(id != -1) return id;
      }

      for(int row = 1; row + winN <= table.length; row++) {
        int id = checkLeftDiagonalFrom(row,table.length - 1);
        if(id != -1) return id;
      }

      return -1;
    }

    // controlla la diagonale destra verso il basso
    private int checkRightDiagonalFrom(int row, int col) {
      int id = table[row][col];
      int count = 0;

      int constant = row-col;

      for(; row < table.length && col >= 0 && col < table.length; row++, col = row-constant) {
        if(table[row][col] != id) {
          id = table[row][col];
          count = 1;
        } else if(id != -1) {
          count++;
          if(count >= winN) return id;
        }
      }

      return -1;
    }

    // controlla la diagonale destra verso il basso
    private int checkLeftDiagonalFrom(int row, int col) {
      //System.out.println(String.format("checking %d , %d", row, col));

      int id = table[row][col];
      int count = 0;

      int constant = row+col;

      for(; row < table.length && col >= 0 && col < table.length; row++, col = constant-row) {
        //System.out.println(String.format("loop: %d, %d, (%d, %d)", row, col, id, count));

        if(table[row][col] != id) {
          id = table[row][col];
          count = 1;
        } else if(id != -1) {
          count++;
          if(count >= winN) return id;
        }
      }
      //System.out.println(String.format("exit: %d, %d", row, col));

      return -1;
    }

    public void show() {
      System.out.println(toString());
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();

      int longestLabel = labelLength(table.length - 1);

      builder.append("  " + repeat(" ", longestLabel));
      for(int i = 0; i < table.length; i++) {
        builder.append(' ').append(repeat(" ", longestLabel - labelLength(i))).append(i).append(' ');
      }
      builder.append('\n');
      builder.append("  " + repeat(" ", longestLabel));
      for(int i = 0; i < table.length; i++) {
        builder.append("--" + repeat("-", longestLabel));
      }
      builder.append('\n');

      for(int row = 0; row < table.length; row++) {
        builder.append(row).append(repeat(" ", longestLabel - labelLength(row))).append(' ').append('|');
        for(int col = 0; col < table.length; col++) {
          int id = table[row][col];
          builder.append(' ').append(repeat(" ", longestLabel - 1)).append(id == -1 ? '.' : markers[id]).append(' ');
        }
        builder.append('|').append('\n');
      }

      builder.append("  " + repeat(" ", longestLabel));
      for(int i = 0; i < table.length; i++) {
        builder.append("--" + repeat("-", longestLabel));
      }

      return builder.toString();
    }

    public String repeat(String s, int count) {
      return count > 0 ? s + repeat(s, --count) : "";
    }

    public int labelLength(int i) {
      return (i + "").length();
    }
  }
}
