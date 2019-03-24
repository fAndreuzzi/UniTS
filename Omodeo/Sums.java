import java.util.Arrays;

public class Sums {
  public static void main(String[] s) {

    int[] array = new int[s.length - 1];
    for(int j = 0; j < s.length - 1; j++) array[j] = Integer.parseInt(s[j]);

    int P = Integer.parseInt(s[s.length - 1]);

    boolean[] comb = checkComb(array, P);
    if(comb == null) System.out.println("null");
    else System.out.println(Arrays.toString(translate(array, comb)));
  }

  private static boolean[] checkComb(int[] array, int P) {
    boolean[][] combs = buildCombs(array.length);

    for(int j = 0; j < combs.length; j++)
      if(sum(array, combs[j]) == P)
        return combs[j];

    return null;
  }

  private static boolean[][] buildCombs(int len) {
    boolean[][] combs = new boolean[(int) Math.pow(2, len)][len];

    combs[0][0] = true;
    combs[1][0] = false;

    for(int j = 1; j < len; j++) {
      // se siamo al j-esimo giro, significa che abbiamo già scritto sui primi 2^j posti dell'array combs
      int written = (int) Math.pow(2, j);

      // copia le prime 2^j righe nelle 2^j righe sottostanti. aggiungi il valore TRUE
      boolean[][] copy = getCopy(combs, 0, written - 1);
      for(int i = written; i < written * 2; i++) {
        combs[i] = copy[i - written];
        combs[i][j] = true;
      }

      // aggiungi il valore FALSE alle prime 2^j righe
      for(int i = 0; i < written; i++) {
        combs[i][j] = false;
      }
    }

    return combs;
  }

  // somma solo gli int aventi indice i tale che (onoff[i] == true)
  private static int sum(int[] array, boolean[] onoff) {
    int sum = 0;

    for(int j = 0; j < array.length; j++)
      if(onoff[j])
        sum += array[j];

    return sum;
  }

  // from INCLUSO, to INCLUSO
  // src deve essere RETTANGOLARE
  private static boolean[][] getCopy(boolean[][] src, int from, int to) {
    if(from > to) throw new UnsupportedOperationException();

    boolean[][] toReturn = new boolean[to - from + 1][src[0].length];
    for(int j = from; j <= to; j++) {
      System.arraycopy(src[j], 0, toReturn[j - from], 0, src[j].length);
    }

    return toReturn;
  }

  private static int[] translate(int[] array, boolean[] onoff) {
    int l = 0;
    for(boolean b : onoff) if(b) l++;

    int[] translated = new int[l];
    for(int j = 0, counter = 0; j < array.length; j++) {
      if(onoff[j]) translated[counter++] = array[j];
    }

    return translated;
  }

  // DEBUG
  private static void printBooleanMatrix(boolean[][] matrix) {
    for(boolean[] bs : matrix) {
      for(boolean b : bs) System.out.print(b + " ");
      System.out.print("\n");
    }
  }
}
