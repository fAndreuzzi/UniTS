/*
Francesco Andreuzzi
IN0500630
*/

import java.lang.Math;
import java.awt.Point;
import java.util.Stack;
import java.util.Random;
import java.util.Collections;

public class Percorso {

  public static void main(String[] args) {
    int n;
    try {
      n = Integer.parseInt(args[0]);
    } catch(Exception e) {
      n = 10;
    }

    Griglia g = new Griglia(n);
    System.out.println(g.toString());

    Stack<Point> path = g.findPath();
    Griglia.printPath(path);
  }

  // classe "segnaposto", viene usata per indicare la fine di una serie di Point "figli" di uno stesso Point "padre"
  static class NullPoint extends Point {}

  static class Griglia {
    //[x][y]
    private boolean[][] map;

    public Griglia(boolean[][] values) {
      this.map = copy(values);
    }

    // genera la griglia n*n in modo casuale
    public Griglia(int n) {
      this.map = new boolean[n][n];
      Random r = new Random();

      for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
          int nr = r.nextInt(3);

          boolean b;
          switch(nr) {
            case 0:
              b = true;
              break;
            default:
              b = false;
          }

          map[i][j] = b;
        }
      }
    }

    public Stack<Point> findPath() {
      Stack<Point> stack = new Stack<>();
      Stack<Point> parents = new Stack<>();

      NullPoint placeholder = new NullPoint();

      for(int i = 0; i < map.length; i++) {
        if(map[0][i]) stack.add(new Point(0,i));
        map[0][i] = false;
      }

      Point now;
      while(stack.size() > 0) {
        now = stack.pop();
        while (now instanceof NullPoint) {
          if(stack.size() == 0) return null;
          else {
            parents.pop();
            now = stack.pop();
          }
        }

        if(now.x == map.length - 1) {
          // aggiungi l'ultima cella
          parents.add(now);
          return parents;
        }

        // non voglio poter ritornare sulla stessa roccia due volte (non ha senso)
        map[now.x][now.y] = false;

        stack.add(placeholder);
        int size_before = stack.size();
        appendRockNeighboroughs(stack, map, now);
        if(stack.size() != size_before) {
            parents.add(now);
        } else {
          // rimuovi il segnalino, è inutile
          stack.pop();
        }
      }

      return null;
    }

    public void appendRockNeighboroughs(Stack<Point> stack, boolean[][] map, Point now) {
      for(int i = Math.max(0,now.x-1); i <= Math.min(map[0].length-1,now.x+1); i++) {
        for(int j = Math.max(0,now.y-1); j <= Math.min(map.length-1, now.y+1); j++) {
          if(i == now.x && j == now.y) continue;

          if(map[i][j]) stack.add(new Point(i,j));
        }
      }
    }

    private boolean[][] copy(boolean[][] arr) {
      boolean[][] cp = new boolean[arr.length][];
      for(int i = 0; i < arr.length; i++) {
        cp[i] = new boolean[arr[i].length];
        for(int j = 0; j < arr[i].length; j++) cp[i][j] = arr[i][j];
      }

      return cp;
    }

    public String toString() {
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

    public static void printPath(Stack<Point> path) {
      if(path == null) {
        System.out.println("N/A");
        return;
      }

      System.out.print("Path:");
      while(path.size() > 0) {
        Point p = path.pop();
        System.out.print(" (" + p.x + ", " + p.y + ")" + (path.size() > 0 ? " <-" : "\n"));
      }
    }

    private String boolToString(boolean b) {
      return b ? "1" : "0";
    }
  }
}
