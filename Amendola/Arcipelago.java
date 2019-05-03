import java.awt.Point;
import java.util.Random;
import java.util.function.Function;

public class Arcipelago {

    public static void main(String[] args) {
        Arcipelago arci = new Arcipelago(10, 8);
        System.out.println(arci.toString());
        System.out.println(arci.getIslands());
    }

    private enum Color {
        WHITE, GRAY
    }

    // [row][col]
    private final boolean[][] grid;
    // units of earth
    private int earth;
    // no. of islands
    private int islands;

    // random init constructor
    public Arcipelago(int n, int m) {
        if (n <= 0 || m <= 0)
            throw new UnsupportedOperationException();

        grid = new boolean[n][m];
        randomInit(grid);

        countEarth();
        countIslands();
    }

    public int getKmqs() {
        return earth * 2;
    }

    public int getIslands() {
        return islands;
    }

    private void countEarth() {
        earth = 0;

        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
                earth += Integer.valueOf(grid[row][col] ? 1 : 0);
    }

    // DFS
    private void countIslands() {
        Color[][] colors = new Color[grid.length][grid[0].length];
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
                colors[row][col] = Color.WHITE;

        islands = 0;

        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
                if (grid[row][col] && colors[row][col] == Color.WHITE) {
                    colors[row][col] = Color.GRAY;
                    dfsVisit(row, col, colors);
                    // incremento qui perchè chiamare da qui dfsVisit rappresenta l'inizio di un
                    // nuovo sottoalbero nella foresta
                    ++islands;
                    System.out.println("new island!");
                }
    }

    private void dfsVisit(int row, int col, Color[][] colors) {
        System.out.println(row + ";" + col);
        // visit Adj[row][col]

        // up
        attemptVisit(row - 1, col, colors);
        // right
        attemptVisit(row, col + 1, colors);
        // down
        attemptVisit(row + 1, col, colors);
        // left
        attemptVisit(row, col - 1, colors);
    }

    private void attemptVisit(int row, int col, Color[][] colors) {
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[row].length && grid[row][col]
                && colors[row][col] == Color.WHITE) {
            colors[row][col] = Color.GRAY;
            dfsVisit(row, col, colors);
        }
    }

    private static void randomInit(boolean[][] grid) {
        final Random r = new Random();

        Function<Point, Boolean> initFunction = new Function<Point, Boolean>() {
            @Override
            public Boolean apply(Point t) {
                return r.nextInt(4) == 0;
            }
        };

        initMatrix(grid, initFunction);
    }

    // initialize a matrix with the given function
    private static void initMatrix(boolean[][] grid, Function<Point, Boolean> initFunction) {
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = initFunction.apply(new Point(row, col));
            }
    }

    private static boolean[][] deepCopy(final boolean[][] grid) {
        if (grid == null || grid.length == 0)
            throw new UnsupportedOperationException();

        boolean[][] copy = new boolean[grid.length][grid[0].length];

        Function<Point, Boolean> initFunction = new Function<Point, Boolean>() {
            @Override
            public Boolean apply(Point t) {
                return grid[t.x][t.y];
            }
        };

        initMatrix(copy, initFunction);
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++)
                builder.append(grid[row][col] ? '*' : '-').append(' ');
            builder.append('\n');
        }
        return builder.toString();
    }
}