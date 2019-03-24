public class Palindroma {
  public static void main(String[] args) {
    palTest(args);
  }

  private static boolean vienePrima(String a, String b) {
    if(a.length() == 0) return true;
    else if(b.length() == 0) return false;
    else {
      return a.charAt(0) == b.charAt(0) ? vienePrima(a.substring(1), b.substring(1)) : a.charAt(0) < b.charAt(0);
    }
  }

  private static void palTest(String[] args) {
    long time = System.nanoTime();
    System.out.println(recPal(args[0]));
    System.out.println((System.nanoTime() - time) + "\n\n");

    time = System.nanoTime();
    System.out.println(itPal(args[0]));
    System.out.println((System.nanoTime() - time) + "\n\n");
  }

  private static boolean recPal(String s) {
    System.out.println("recPal: " + s);
    return s.length() <= 1 || (s.charAt(0) == s.charAt(s.length() - 1) && recPal(s.substring(1,s.length() - 1)));
  }

  private static boolean itPal(String s) {
    while(s.length() >= 1) {
      System.out.println("itPal: " + s);
      if(s.charAt(0) != s.charAt(s.length() - 1)) return false;
      else s = s.substring(1, s.length() - 1);
    }
    return true;
  }
}
