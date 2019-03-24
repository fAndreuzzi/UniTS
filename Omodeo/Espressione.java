import java.util.Stack;

public class Espressione {

  static final boolean DEBUG = false;

  public static void main(String[] args) {
    log("METODO RICORSIVO");
    long time = System.nanoTime();
    log("result: " + recEvaluate(args[0]).getValue());
    long recElapsed = System.nanoTime() - time;
    log("time: " + recElapsed + " ns");

    log("\n");

    log("METODO ITERATIVO");
    time = System.nanoTime();
    log("result: " + itEvaluate(args[0]));
    long itElapsed = System.nanoTime() - time;
    log("time: " + (System.nanoTime() - time) + " ns");

    log("");
    log("T(iterativo) = " + String.format("%.3f", ((double) itElapsed / (double) recElapsed)) + " * T(ricorsivo)");
  }

  public static Optional itEvaluate(String s) {
    Stack<String> toEval = new Stack<>();
    // numbers
    Stack<Optional> operands = new Stack<>();
    // + - * / ^
    Stack<Character> operators = new Stack<>();

    toEval.add(s);

    while(toEval.size() > 0) {
      if(DEBUG) {
        log(toEval);
        log(operands);
        log(operators);
        log("\n");
      }

      String now = toEval.pop();

      Optional i = attemptInt(now);
      if(!i.isNull()) {
        if(operands.size() > 0) {
          Optional second = operands.pop();
          if(second.isNull()) {
            operands.add(i);
          } else {
            operands.add(i.operation(second,operators.pop()));
          }
        } else {
          operands.add(i);
        }

        if(DEBUG) log("\n");
        continue;
      }

      if(!(now.startsWith("(") && now.endsWith(")"))) return new Optional();

      now = now.substring(1,now.length() - 1);
      Object[] pack = extract(now);
      if(pack == null) return new Optional();

      toEval.add((String) pack[0]);
      toEval.add((String) pack[1]);
      operators.add((char) pack[2]);
      operands.add(new Optional());
    }

    if(DEBUG) {
      log("no evaluate");
      log(operands);
      log(operators);
      log("\n");
    }

    Optional last = null;
    while(operands.size() > 0) {
      if(DEBUG) {
        log("no evaluate");
        log(last);
        log(operands);
        log(operators);
        log("\n");
      }

      Optional now = operands.pop();
      if(!now.isNull()) {
        if(last != null) {
          last = last.operation(now, operators.pop());
        } else {
          last = now;
        }
      }
    }

    return new Optional(last);
  }

  public static Optional recEvaluate(String s) {
    Optional i = attemptInt(s);
    if(!i.isNull()) return i;

    if(!(s.startsWith("(") && s.endsWith(")"))) return new Optional();

    s = s.substring(1,s.length() - 1);

    Object[] pack = extract(s);
    if(pack == null) return new Optional();

    Optional o1 = recEvaluate((String) pack[0]);
    Optional o2 = recEvaluate((String) pack[1]);
    return o1.operation(o2, (char) pack[2]);
  }

  // ------------------

  // estrae due stringhe in base alle regole di una espressione
  private static Object[] extract(String s) {
    int toClose = 0;
    for(int j = 0; j < s.length(); j++) {
      char c = s.charAt(j);

      if(isOperator(c)) {
          if(toClose != 0) continue;

          return new Object[] {s.substring(0, j), s.substring(j+1,s.length()), c};
      }
      else if(c == '(') toClose++;
      else if(c == ')') toClose--;

      if(toClose < 0) return null;
    }

    return null;
  }

  private static Optional attemptInt(String s) {
    try {
      return new Optional(Integer.parseInt(s));
    } catch(Exception e) {
      return new Optional();
    }
  }

  private static boolean isOperator(char c) {
    return c == '+' || c == '*' || c == '-' || c == '/' || c == '^';
  }

  private static int nonNull(int i1, int i2) {
    return i1 != -1 ? i1 : i2;
  }

  // log
  private static void log(String s){
    System.out.println(s);
  }

  private static void log(Object o) {
    log(o == null ? "null" : o.toString());
  }

  // classe di supporto
  private static class Optional {
    private Object value;

    public Optional(Object value) {
      this.value = value;
    }

    // build a null optional
    public Optional() {
      this.value = null;
    }

    public Optional operation(Optional o2, char c) {
      Optional o1 = this;
      if(o1.isNull() || o2.isNull()) return new Optional();

      switch(c){
        case '+':
          return new Optional(o1.getIntValue() + o2.getIntValue());
        case '-':
          return new Optional(o1.getIntValue() - o2.getIntValue());
        case '*':
          return new Optional(o1.getIntValue() * o2.getIntValue());
        case '/':
          return new Optional(o1.getIntValue() / o2.getIntValue());
        case '^':
          return new Optional((int) Math.pow(o1.getIntValue(), o2.getIntValue()));
        default:
          return new Optional();
      }
    }

    public Object getValue() {
      return isNull() ? "null" : value;
    }

    public int getIntValue() {
      if(isNull()) throw new UnsupportedOperationException();
      return (int) value;
    }

    public boolean isNull() {
      return value == null;
    }

    public String toString() {
      return getValue().toString();
    }
  }
}
