import model.Percep;
import model.Perceptron;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        for(int i = 1; i <= 10; i++) {
            double d = (double)(i) / 10.0;
            Percep p = new Percep(25, 14, 12, d);
            p.train();
            p.validate();
            p.test();
        }
    }
}