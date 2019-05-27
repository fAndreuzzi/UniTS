import java.util.*;

class Palude {

    private static final boolean DEBUG = false;

    private final int[][] matrix;

    public Palude(int[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        return matrixAsString(this.matrix);
    }

    public boolean verificaPercorso(int startR, int startC, int endR, int endC) {
        Cell start = new Cell(startR, startC);
        Cell end = new Cell(endR, endC);

        if (DEBUG)
            System.out.println("--- verificaPercorso ---");
        return findPath(deepCopy(this.matrix), start, end) != null;
    }

    public int lunghezzaPercorso(int startR, int startC, int endR, int endC) {
        Cell start = new Cell(startR, startC);
        Cell end = new Cell(endR, endC);

        if (DEBUG)
            System.out.println("--- lunghezzaPercorso ---");
        LinkedList<Cell> path = findPath(deepCopy(this.matrix), start, end);

        return path == null ? -1 : path.size();
    }

    private static int[][] deepCopy(int[][] matrix) {
        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = new int[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++)
                copy[i][j] = matrix[i][j];
        }

        return copy;
    }

    private static String matrixAsString(int[][] matrix) {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++)
                builder.append(matrix[row][col]).append(' ');
            builder.append("\n");
        }

        return builder.toString();
    }

    private static LinkedList<Cell> findPath(int[][] matrixCopy, Cell start, Cell end) {
        if (DEBUG) {
            System.out.println("---- findPath ----");
            System.out.println(start);
            System.out.println(end);
            System.out.println(matrixAsString(matrixCopy));
        }

        LinkedList<Cell> pathFromHere = null;
        if (start.equals(end)) {
            if (DEBUG)
                System.out.println("END!");

            pathFromHere = new LinkedList<>();
            pathFromHere.addLast(end);
            return pathFromHere;
        }

        // nullify the current point
        matrixCopy[start.row][start.col] = 0;
        if (DEBUG)
            System.out.println("current point nullified: " + start.toString());

        // left
        if (start.col > 0 && matrixCopy[start.row][start.col - 1] == 1)
            pathFromHere = findPath(matrixCopy, new Cell(start.row, start.col - 1), end);
        // right
        if (pathFromHere == null && start.col < matrixCopy[start.row].length - 1
                && matrixCopy[start.row][start.col + 1] == 1)
            pathFromHere = findPath(matrixCopy, new Cell(start.row, start.col + 1), end);
        // top
        if (pathFromHere == null && start.row > 0 && matrixCopy[start.row - 1][start.col] == 1)
            pathFromHere = findPath(matrixCopy, new Cell(start.row - 1, start.col), end);
        // bottom
        if (pathFromHere == null && start.row < matrixCopy.length - 1 && matrixCopy[start.row + 1][start.col] == 1)
            pathFromHere = findPath(matrixCopy, new Cell(start.row + 1, start.col), end);

        if (pathFromHere != null)
            pathFromHere.addFirst(start);
        return pathFromHere;
    }

    private static class Cell {
        public final int row, col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return String.format("Cell: Row: %d, Col: %d", this.row, this.col);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell && ((Cell) obj).row == this.row && ((Cell) obj).col == this.col;
        }
    }
}
