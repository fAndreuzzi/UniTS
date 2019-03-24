import java.util.Arrays;

public class Ordinamento {
  public static void main(String[] s) {
    if(s.length == 0) return;

    int[] array = new int[s.length - 2];
    for(int j = 0; j < s.length - 2; j++) array[j] = Integer.parseInt(s[j]);

    int what = Integer.parseInt(s[s.length - 2]);
    int P = Integer.parseInt(s[s.length - 1]);

    int[] copy = makeCopy(array);
    startWatcher();
    bubbleSort(copy, P > 0);
    log("bubble: " + Arrays.toString(copy) + " in " + stopWatcher() + " ns");

    copy = makeCopy(array);
    startWatcher();
    minMaxSort(copy, P > 0, 0);
    log("minmax: " + Arrays.toString(copy) + " in " + stopWatcher() + " ns");

    copy = makeCopy(array);
    startWatcher();
    bubbleSortOmodeo(copy, P > 0);
    log("bubbleOmodeo: " + Arrays.toString(copy) + " in " + stopWatcher() + " ns");

    copy = makeCopy(array);
    startWatcher();
    int i = binarySearchIterative(copy, what);
    log("binaryIterative found: " + i);
  }

  private static int binarySearchIterative(int[] array, int what) {
    minMaxSort(array, true, 0);

    int start = 0, end = array.length;

    int i = -1;
    while(end != start) {
      i = (end - start) / 2 + start;

      if(array[i] == what) return i;
      else if(array[i] > what) {
        end = i;
      } else {
        start = i;
      }
    }

    return i;
  }

  private static void bubbleSortOmodeo(int[] array, boolean ascendent) {
    for(int stop = array.length; stop >= 0; stop--) {
      for(int i = 0; i < stop - 1; i++) {
        if(check(array[i+1], array[i], ascendent))
          change(array, i, i+1);
      }
    }
  }

  private static void change(int[] array, int i1, int i2) {
    int temp = array[i1];
    array[i1] = array[i2];
    array[i2] = temp;
  }

  // i1 = i2             -> false
  // ascendent : i1 < i2 -> true
  //           : i1 > i2 -> false
  // !ascendent: i1 > i2 -> true
  //           : i1 < i2 -> false
  private static boolean check(int i1, int i2, boolean ascendent) {
    if(i1 == i2) return false;
    return (ascendent && i1 < i2) || (!ascendent && i1 > i2);
  }

  private static void bubbleSort(int[] array, boolean ascendent) {
    for(int j = 1; j < array.length; j++) {
      int item = array[j];

      int stop = j - 1;
      while(stop >= 0 && item != array[stop] && check(item, array[stop], ascendent))
        stop--;

      stop++;
      if(stop >= 0) {
        shift(array, stop, j - stop);
        array[stop] = item;
      }
    }
  }

  // from inclusive
  private static void minMaxSort(int[] array, boolean min, int from) {
    int valueIndex = min ? min(array, from) : max(array, from);
    int temp = array[valueIndex];
    shift(array, from, valueIndex - from);
    array[from] = temp;

    if(from < array.length - 2) minMaxSort(array, min, from + 1);
  }

  private static int min(int[] array, int from) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;
    for(int i = from; i < array.length; i++) {
      if(min > array[i]) {
        min = array[i];
        minIndex = i;
      }
    }

    return minIndex;
  }

  private static int max(int[] array, int from) {
    int max = Integer.MIN_VALUE;
    int maxIndex = -1;
    for(int i = from; i < array.length; i++) {
      if(max < array[i]) {
        max = array[i];
        maxIndex = i;
      }
    }

    return maxIndex;
  }

  // shift len-items from "from". array[from] is empty
  private static void shift(int[] array, int from, int len) {
    System.arraycopy(array, from, array, from + 1, len);
  }

  private static int[] makeCopy(int[] array) {
    int[] copy = new int[array.length];
    System.arraycopy(array, 0, copy, 0, array.length);
    return copy;
  }

  static long time;
  private static void startWatcher() {
    time = System.nanoTime();
  }

  private static long elapsed() {
    return System.nanoTime() - time;
  }

  private static long stopWatcher() {
    long el = elapsed();
    time = 0;
    return el;
  }

  private static void log(String s) {
    System.out.println(s);
  }

}
