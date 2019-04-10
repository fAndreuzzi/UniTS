import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CoinChange{
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    CoinChange change = new CoinChange(new double[]{0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1, 2});

    while(true) {
      System.out.print("Inserire una moneta. Per cambiare, inserire -1... ");
      double d = s.nextDouble();

      if((int) d == -1) {
        List<Double> list = change.clear();
        if(list == null) System.out.println("C'è stato un errore");
        else System.out.println(list.toString());
      } else {
        if(!change.insert(d)) System.out.println("Moneta non valida!");
        else System.out.println(change.get());
      }
    }
  }

  //private final int[] coins;
  private final double[] coinsValue;
  private double input;

  public CoinChange(double[] coinsValue) {
    this.coinsValue = coinsValue;
  }

  public List<Double> clear() {
    //int[] tempCoins = deepCopy(this.coins);
    double tempInput = this.input;

    List<Double> ls = new ArrayList<>();
    for(int i = coinsValue.length - 1; tempInput > 0 && i >= 0; i--) {
      while(tempInput >= this.coinsValue[i] /*&& tempCoins[i] > 0*/) {
        ls.add(coinsValue[i]);
        tempInput -= coinsValue[i];
        //tempCoins[i]--;
      }
    }

    // impossibile
    if(tempInput > 0) return null;
    else {
      this.input = tempInput;
      //this.coins = tempCoins;

      return ls;
    }
  }

  public boolean insert(double coin) {
    for(int i = 0; i < coinsValue.length; i++) {
      if(coinsValue[i] == coin) {
        this.input += coin;
        //this.coins[i]++;
        return true;
      }
    }

    return false;
  }

  public double get() {
    return this.input;
  }

  public static int[] deepCopy(int[] i) {
    int[] temp = new int[i.length];
    System.arraycopy(i, 0, temp, 0, i.length);
    return temp;
  }
}
