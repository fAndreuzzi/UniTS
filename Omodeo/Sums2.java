import java.util.Arrays;

public class Sums2 {
  public static void main(String[] s) {

    int[] array = new int[s.length - 1];
    for(int j = 0; j < s.length - 1; j++) array[j] = Integer.parseInt(s[j]);

    int P = Integer.parseInt(s[s.length - 1]);

    boolean r = checkComb(array, P);
    System.out.println(r);
  }

  // controlla se tra le combinazioni restituite da buildCombs ce n'è una = P
  private static boolean checkComb(int[] array, int P) {
    int[] combs = buildCombs(array);

    for(int i : combs)
      if(i == P)
        return true;

    return false;
  }

  // costruisce 2^(array.length) combinazioni che esprimono tutte le possibili somme degli elementi dell'array
  private static int[] buildCombs(int[] array) {
    int[] combs = new int[(int) Math.pow(2, array.length)];

    combs[0] = array[0];
    combs[1] = 0;

    for(int j = 1; j < array.length; j++) {
      // se siamo al j-esimo giro, significa che abbiamo già scritto sui primi 2^j posti dell'array combs
      int written = (int) Math.pow(2, j);

      // copia le prime 2^j righe nelle 2^j righe sottostanti
      System.arraycopy(combs, 0, combs, written, written);

      // aggiungi il valore alle nuove righe copiate
      for(int i = written; i < written * 2; i++) {
        combs[i] += array[j];
      }

      // le prime written righe contengono le combinazioni in cui non è stato sommato il valore array[j]
    }

    return combs;
  }
}
