import java.util.Scanner;

public class Anagrammi {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);

    System.out.println("Inserisci la prima stringa...");
    String wh = s.nextLine();
    System.out.println("Inserisci la seconda stringa...");
    String of = s.nextLine();
    while(of.length() == 0) of = s.nextLine();

    System.out.println(isAnagramma(wh,of));
  }

  public static boolean isAnagramma(String wh, String of) {
    wh = normalize(wh);
    of = normalize(of);

    if(wh.length() != of.length()) return false;

    for(int i = 0; i < wh.length(); i++) {
      char c = wh.charAt(i);

      int index = of.indexOf(Character.toString(c));
      if(index == -1) return false;

      of = of.substring(0,index).concat(of.substring(index+1,of.length()));
    }

    return true;
  }

  private static String normalize(String s) {
    return s.toLowerCase().replaceAll("\\s", "");
  }
}
