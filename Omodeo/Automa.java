import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Automa {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    Automa a = new Automa(randomTransTable(s));
    logN(a.toString());

    logN("Inserisci una parola...");
    String word = s.nextLine();
    while(word.length() == 0) word = s.nextLine();

    logN(a.run(word).toString());
  }

  private static void logN(String s) {
    System.out.println(s);
  }

  private static void log(String s) {
    System.out.print(s);
  }

  private static void logN(Object o) {
    System.out.println(o.toString());
  }

  private static void log(Object o) {
    System.out.print(o.toString());
  }

  private static int[][] randomTransTable(Scanner sc) {
    logN("Inserire il numero di stati...");
    int stati = sc.nextInt();

    logN("Inserire il numero di input possibili...");
    int nInput = sc.nextInt();

    int[][] tt = new int[stati][nInput];
    Random r = new Random();

    for(int stato = 0; stato < stati; stato++) for(int input = 0; input < nInput; input++) tt[stato][input] = r.nextInt(nInput + 2) - 1;

    return tt;
  }

  private static final int START = 0;

  // le RIGHE DI transTable sono il numero di stati, le COLONNE di transTable sono il numero di input possibili
  private int[][] transitionTable;

  public Automa(int[][] transTable) {
    this.transitionTable = transTable;
    //this.nStati = transTable.length;
    //this.nInput = transTable[0].length;
  }

  public String toString() {
    // per stampare un'automa stampiamo solamente la sua matrice di transizione
    StringBuilder builder = new StringBuilder();

    builder.append("S/I ");
    for(int input = 0; input < transitionTable[0].length; input++) builder.append(format(input));

    for(int state = 0; state < transitionTable.length; state++) {
      builder.append("\n");
      builder.append(format(state));
      for(int input = 0; input < transitionTable[0].length; input++) builder.append(format(transitionTable[state][input]));
    }

    return builder.toString();
  }

  private String format(int i) {
    return String.format("%-4d", i);
  }

  private List<Integer> feed(String word) {
    List<Integer> listaStati = new ArrayList<>();

    listaStati.add(START);

    for(int i = 0; i < word.length(); i++) {
      int stato = listaStati.get(listaStati.size() - 1);
      if(stato < 0 || stato >= transitionTable[0].length) return listaStati;

      int now = Integer.parseInt(Character.toString(word.charAt(i)));
      listaStati.add(transitionTable[stato][now]);
    }

    return listaStati;
  }

  public RunResult run(String word) {
    List<Integer> listaStati = feed(word);
    return new RunResult(word.substring(listaStati.size() - 1, word.length()), listaStati);
  }

  public static class RunResult {
    public String residuo;
    public List<Integer> listaStati;

    public RunResult(String r, List<Integer> list) {
      this.residuo = r;
      this.listaStati = list;
    }

    public String toString() {
      return "Residuo: " + this.residuo + " ---  Stati: " + listaStati.toString();
    }
  }
}
