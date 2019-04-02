import java.util.Stack;
import java.util.Scanner;

public class Espressione {

  // true -> mostra i messaggi di debug
  // false -> nascondi i messaggi di debug
  // ##### ATTENZIONE ######
  // siccome i messaggi di log sono concentrati solo nella versione iterativa, attivare il debug
  // rende la versione iterativa molto più pesante del normale
  static final boolean DEBUG = false;

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);

    // ciclo infinito, continua a richiedere stringhe finchè non si riceve in input la stringa "stop"
    while(true) {
      System.out.println("\nInserire un'espressione... ");
      String line = s.nextLine();
      while(line.length() == 0) line = s.nextLine();

      // stoppa l'esecuzione se viene inserita la stringa "stop"
      if(line.equals("stop")) return;

      // valutazione ricorsiva dell'espressione
      log("METODO RICORSIVO");
      long time = System.nanoTime();
      log("result: " + recEvaluate(line));
      long recElapsed = System.nanoTime() - time;
      log("time: " + recElapsed + " ns");

      log("\n");

      // valutazione iterativa dell'espressione
      log("METODO ITERATIVO");
      time = System.nanoTime();
      log("result: " + itEvaluate(line));
      long itElapsed = System.nanoTime() - time;
      log("time: " + itElapsed + " ns");

      log("\nT(iterativo) = " + String.format("%.3f", ((double) itElapsed / (double) recElapsed)) + " * T(ricorsivo)");
    }
  }

  // valutazione iterativa dell'espressione s
  // si può verificare che la versione iterativa diventa più pesante quando la parte destra dell'espressione
  // è più pesante. bisogna indagare...
  public static Optional itEvaluate(String s) {
    // contiene le stringhe che devono essere valutate. viene riempito da ogni nuova iterazione del ciclo while (a meno che
    // la stringa "now" non sia un intero, che non necessita di ulteriori valutazioni)
    Stack<String> toEval = new Stack<>();
    // contiene gli operandi
    Stack<Optional> operands = new Stack<>();
    // contiene gli operatori
    Stack<Character> operators = new Stack<>();

    // aggiunge l'espressione s alla pila. in questo modo il ciclo while comincerà a valutare l'espressione s
    toEval.add(s);

    // il ciclo viene stoppato quando la pila contenente le espressioni da valutare diventa vuota
    while(toEval.size() > 0) {
      if(DEBUG) {
        log(toEval);
        log(operands);
        log(operators);
        log("\n");
      }

      // estrae l'ultima stringa inserita dalla pila
      String now = toEval.pop();

      // cerca di convertire now in un intero
      Optional i = attemptInt(now);
      if(!i.isNull()) {
        // se now è un intero valido (e quindi non deve essere valutato ulteriormente)
        if(operands.size() > 0) {
          // cerchiamo di capire se è già presente un primo operando per l'operazione che vogliamo portare a termine.
          // possiamo usare pop() e non peek() dato che il segnaposto può essere tolto senza problemi (vogliamo cominciare
          // la valutazione dell'espressione)
          Optional second = operands.pop();

          // se second è un segnaposto
          // ==> non è ancora stato inserito un primo operatore
          if(second.isNull()) {
            operands.add(i);
          }
          // è già presente un operando. lo uso per valutare il valore dell'operazione
          else {
            Optional op = i.operation(second,operators.pop());

            // continuo ad estrarre elementi dalla pila finchè non trovo un segnaposto. uso gli elementi estratti per
            // effettuare le operazioni richieste in serie
            while(operands.size() > 0 && operators.size() > 0) {
              Optional o = operands.pop();
              if(o.isNull()) break;
              else op = op.operation(o,operators.pop());
            }

            operands.add(op);
          }
        } else {
          // questo sarà necessariamente il primo operando di un'operazione successiva (dato che operands.size() == 0)
          operands.add(i);
        }

        if(DEBUG) log("\n");
        continue;
      }

      // se s non è un intero, deve essere un'espressione valida, cioè una stringa del tipo "(op1[operatore]op2)"
      // controlla che s sia contenuta all'interno di una coppia di parentesi tonde
      if(!(now.startsWith("(") && now.endsWith(")"))) return Optional.nullOptional();
      // rimuove la coppia di parentesi per facilitare la valutazione dell'espressione
      now = now.substring(1,now.length() - 1);

      // usa il metodo extract per estrarre i due operandi e l'operatore dalla stringa s
      Object[] pack = extract(now);

      // se pack == null
      // ==> l'espressione s non è valida
      // viene ritornato un Optional nullo per comunicare l'insuccesso. l'insuccesso si propaga verso l'alto,
      // fino al metodo main
      if(pack == null) return Optional.nullOptional();

      // la stringa now è stata digerita. ha generato due nuove espressioni da valutare, che vengono inserite
      // nella pila delle stringhe da valutare
      toEval.add((String) pack[0]);
      toEval.add((String) pack[1]);
      // l'operatore che separa le due espressioni ottenute da now viene aggiunto alla pila degli operatori
      operators.add((char) pack[2]);
      // viene inserito un segnaposto all'interno della pila degli operandi, che indica che le due espressioni
      // successive derivano dalla stessa espressione parente, e quindi sono un'unica operazione
      operands.add(Optional.nullOptional());
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

  //valuta ricorsivamente il valore dell'espressione. ritorna un valore Optional (null o int)
  public static Optional recEvaluate(String s) {
    // cerca di convertire la stringa in un intero
    Optional i = attemptInt(s);
    // se ci riesce, il metodo ritorna l'intero ottenuto
    if(!i.isNull()) return i;

    // se s non è un intero, deve essere un'espressione valida, cioè una stringa del tipo "(op1[operatore]op2)"
    // controlla che s sia contenuta all'interno di una coppia di parentesi tonde
    if(!(s.startsWith("(") && s.endsWith(")"))) return Optional.nullOptional();
    // rimuove la coppia di parentesi per facilitare la valutazione dell'espressione
    s = s.substring(1,s.length() - 1);

    // usa il metodo extract per estrarre i due operandi e l'operatore dalla stringa s
    Object[] pack = extract(s);

    // se pack == null
    // ==> l'espressione s non è valida
    // viene ritornato un Optional nullo per comunicare l'insuccesso. l'insuccesso si propaga verso l'alto,
    // fino al metodo main
    if(pack == null) return Optional.nullOptional();

    // valutazione ricorsiva dell'operando di sinistra
    Optional o1 = recEvaluate((String) pack[0]);
    // valutazione ricorsiva dell'operando di destra
    Optional o2 = recEvaluate((String) pack[1]);
    // unione dei risultati ottenuti
    return o1.operation(o2, (char) pack[2]);
  }

  // cerca di estrarre le componenti dall'espressione s. se s non è un'espressione valida, verrà ritornato il valore null.
  // altrimenti, viene restituito un array di lunghezza 3:
  // * array[0] -> l'operando di sinistra
  // * array[1] -> l'operando di destra
  // * array[2] -> l'operatore che separa i due operandi
  //
  // il controllo delle parentesi da chiudere in toClose è necessario, infatti se ritornassi il primo operatore trovato
  // dal ciclo for (senza il controllo delle parentesi) potrei ottenere risultati sbagliati.
  // ESEMPIO (di risultato sbagliato):
  // s = "(3+3)*4"
  // OUTPUT:
  // array[0] = "(3"
  // array[1] = "3)*4"
  // array[2] = '+'
  // chiaramente questo non è il risultato desiderato
  private static Object[] extract(String s) {
    // memorizza il numero di parentesi da chiudere
    int toClose = 0;
    for(int j = 0; j < s.length(); j++) {
      // memorizza l'elemento di posto j della stringa s
      char c = s.charAt(j);

      if(isOperator(c)) {
          // se c è un operatore, e non dobbiamo chiudere nessuna parentesi, allora abbiamo trovato l'operatore in mezzo
          // alla espressione (cioè l'elemento di posto j della stringa s).
          // usiamo il metodo substring per "tagliare" la stringa nei punti giusti
          if(toClose == 0) return new Object[] {s.substring(0, j), s.substring(j+1,s.length()), c};
          else continue;
      }
      // se c è una parentesi aperta, incrementiamo il contatore delle parentesi da chiudere
      else if(c == '(') toClose++;
      // se c è una parentesi chiusa, decrementiamo il contatore delle parentesi da chiudere
      else if(c == ')') toClose--;

      // se toClose è negativo
      // ==> abbiamo chiuso una parentesi senza trovare una parentesi aperta corrispondente.
      // ==> l'espressione è malformata
      if(toClose < 0) return null;
    }

    // nessun operatore è stato trovato in una posizione valida
    // ==> l'espressione è malformata
    return null;
  }

  // cerca di convertire s in un intero. se s non è un intero valido ritorna un Optional vuoto
  private static Optional attemptInt(String s) {
    try {
      return new Optional(Integer.parseInt(s));
    } catch(Exception e) {
      return Optional.nullOptional();
    }
  }

  // se c è un operatore valido (+,-,*,/,^) ritorna true. false altrimenti
  private static boolean isOperator(char c) {
    return c == '+' || c == '*' || c == '-' || c == '/' || c == '^';
  }

  // log
  private static void log(String s){
    System.out.println(s);
  }

  // log
  private static void log(Object o) {
    log(o == null ? "null" : o.toString());
  }

  // classe di supporto. contiene un intero oppure null. espone alcuni metodi che semplificano il codice dei metodi
  // veri e propri della classe Espressione
  private static class Optional {
    // contiene il valore dell'Optional (null oppure int)
    private Object value;

    public Optional(Object value) {
      this.value = value;
    }

    // costruisce un Optional vuoto
    private Optional() {
      this.value = null;
    }

    private static Optional nullOpt = new Optional();
    // ritorna un Optional null
    public static Optional nullOptional() {
      return nullOpt;
    }

    // valuta un'operazione con l'operando di destra o2 e l'operatore c
    public Optional operation(Optional o2, char c) {
      Optional o1 = this;
      if(o1.isNull() || o2.isNull()) return Optional.nullOptional();

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
          return Optional.nullOptional();
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
