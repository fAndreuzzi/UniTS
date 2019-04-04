import java.util.Random;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;
import java.util.Scanner;

public class Canali {

  public static void main(String[] args) {
    Griglia griglia = new Griglia(10);
    System.out.println(griglia.toString());

    Scanner scanner = new Scanner(System.in);

    System.out.print("Verifica canale:\nda... ");
    int from = scanner.nextInt();
    System.out.print("a... ");
    int to = scanner.nextInt();
    System.out.println(griglia.findPath(from, to));

    /*System.out.println("\n\nInserire un percorso...");

    Queue<Point> path = new LinkedList<>();
    Point p;
    while((p = readPoint(scanner)) != null) path.add(p);

    System.out.println(path.toString());
    System.out.println(griglia.checkPath(path));*/
  }

  private static Point readPoint(Scanner scanner) {
    String line = scanner.nextLine();
    if(line.length() == 0) return null;

    return parsePoint(line);
  }

  private static Point parsePoint(String s) {
    // rimuove tutti gli spazi
    s = s.replaceAll("\\s", "");

    // taglia in base alla posizione delle virgole
    String[] split = s.split(",");
    if(split.length != 2) return null;

    try {
      int i1 = Integer.parseInt(split[0]);
      int i2 = Integer.parseInt(split[1]);

      return new Point(i1, i2);
    } catch(Exception e) {
      return null;
    }
  }

  private static class Griglia {
    boolean[][] table;

    // generazione casuale della griglia
    public Griglia(int n) {
      table = new boolean[n][];

      Random r = new Random();

      for(int row = 0; row < n; row++) {
        table[row] = new boolean[n];
        for(int col = 0; col < n; col++) {
          // vogliamo lasciare libera la diagonale
          if(col != row) table[row][col] = randomBoolean(r);
        }
      }
    }

    private Queue<Integer> findPath(int from, int to) {
      boolean[] visitato = new boolean[table.length];
      int[] distance = new int[table.length];
      int[] parents = new int[table.length];

      BFS(visitato, distance, parents, from, to);

      //System.out.println("parents: " + Arrays.toString(parents));
      //System.out.println("visitato: " + Arrays.toString(visitato));
      //System.out.println("distance: " + Arrays.toString(distance));

      Queue<Integer> path = new LinkedList<>();

      int now = to;
      path.add(to);

      while(now != from) {
        now = parents[now];
        path.add(now);
      }

      return path;
    }

    private void BFS(boolean[] visitato, int[] distance, int[] parents, int from, int to) {
      visitato[from] = true;
      distance[from] = 0;

      Queue<Integer> toVisit = new LinkedList<>();
      // aggiungo il primo nodo
      toVisit.add(from);

      while(toVisit.size() > 0) {
        int now = toVisit.poll();

        // visita le adiacenze
        for(int i = 0; i < table[now].length; i++) {
          // se posso andare in i da now
          if(table[now][i] && !visitato[i]) {
            // aggiorna le informazioni su i
            visitato[i] = true;
            distance[i] = distance[now] + 1;
            parents[i] = now;

            toVisit.add(i);
          }
        }
      }
    }

    private boolean checkPath(Queue<Point> points) {
      if(points.size() == 0) return false;

      int now = -1;
      int to;

      while(points.size() > 0) {
        Point p = points.poll();

        int row = p.x;
        if(now != -1 && now != row) return false;

        int col = p.y;
        if(!table[row][col]) return false;

        now = col;
      }

      return true;
    }

    private boolean randomBoolean(Random r) {
      return r.nextInt(5) == 4;
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("   ");
      for(int i = 0; i < table.length; i++) {
        builder.append(' ').append(i).append(' ');
      }
      builder.append('\n');
      builder.append("   ");
      for(int i = 0; i < table.length; i++) {
        builder.append("---");
      }
      builder.append('\n');

      for(int row = 0; row < table.length; row++) {
        builder.append(row).append(' ').append('|');
        for(int col = 0; col < table.length; col++) {
          builder.append(' ').append(boolToString(table[row][col])).append(' ');
        }
        builder.append('|').append('\n');
      }

      builder.append("   ");
      for(int i = 0; i < table.length; i++) {
        builder.append("---");
      }

      return builder.toString();
    }

    private String boolToString(boolean b) {
      return Integer.toString(b ? 1 : 0);
    }
  }
}
