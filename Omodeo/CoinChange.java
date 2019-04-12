import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

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
        else System.out.println(change.getTotal());
      }
    }
  }

  private final int[] coins;
  private final double[] coinsValue;
  private BigDecimal input = new BigDecimal(0);

  public CoinChange(double[] coinsValue) {
    this.coinsValue = coinsValue;
    this.coins = new int[]{3,3,3,3,3,3,3,3};
  }

  public CoinChange(int[] startCoins, double[] coinsValue) {
    this.coinsValue = coinsValue;
    this.coins = startCoins;
  }

  public int take(BigDecimal total, int[] coins, int index) {
    int temp = index;
    int spot = -1;
    for(int j = 0; j < coins.length; j++) {
      if(temp - coins[j] < 0) {
        spot = j;
        break;
      }
      temp -= coins[j];
    }

    System.out.println(String.format("spot: %d, index: %d, total: %f", spot, index, total.doubleValue()));

    if(spot == -1) return -1;
    index++;

    BigDecimal result = total.subtract(new BigDecimal(coinsValue[spot]));
    System.out.println(String.format("result: %f", result.doubleValue()));
    if(result.doubleValue() == 0) return 1;
    else if(result.doubleValue() < 0) {
      return take(total,coins,index);
    } else {
      int dontTake = take(total,coins,index);
      int take = take(result,coins,index);

      if(dontTake == -1 && take == -1) return -1;
      else if(dontTake == -1 && take != -1) return take + 1;
      else if(dontTake != -1 && take == -1) return dontTake;
      else {
        if(dontTake < take) return dontTake;
        else return take + 1;
      }
    }
  }

  public List<Double> clear() {
    System.out.println(take(this.input,this.coins,0));
    return null;
  }

  public boolean insert(double coin) {
    for(int i = 0; i < coinsValue.length; i++) {
      if(coinsValue[i] == coin) {
        this.input = this.input.add(new BigDecimal(coin));
        this.coins[i]++;
        return true;
      }
    }

    return false;
  }

  public double getTotal() {
    return this.input.doubleValue();
  }

  public static int[] deepCopy(int[] i) {
    int[] temp = new int[i.length];
    System.arraycopy(i, 0, temp, 0, i.length);
    return temp;
  }
}
