import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Sottoliste {
  public static void main(String[] args) {
    System.out.println("Inserire lista... ");
    Scanner s = new Scanner(System.in);
    String line = s.nextLine();
    String[] values = line.split(" ");

    ArrayList<String[]> list = new ArrayList<>();
    sottoListe(list, values, 0);
    for(String[] ss : list) System.out.println(Arrays.toString(ss));
  }

  private static void sottoListe(ArrayList<String[]> sottoliste, String[] values, int index) {
    if(index >= values.length) return;
    if(index == 0) {
      sottoliste.add(new String[0]);
      sottoliste.add(new String[] {values[index]});
    } else {
      int size = sottoliste.size();
      for(int count = 0; count < size; count++) {
        String[] src = sottoliste.get(count);
        String[] sn = new String[src.length + 1];
        System.arraycopy(src, 0, sn, 0, src.length);

        sn[sn.length - 1] = values[index];
        sottoliste.add(sn);
      }
    }

    sottoListe(sottoliste, values, index+1);
  }
}
