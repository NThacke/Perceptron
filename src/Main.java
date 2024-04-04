import model.Percep;
import model.Perceptron;
import java.util.*;

public class Main {

    /**
     * {n, a, b} values for dimensions of interest
     */
    private static final int[][] dimensions = {{42,10,10},{168,5,5}};

    public static void main(String[] args) {

        for(int i = 1; i <= 10; i++) {
            double d = (double)(i) / 10.0;
            for(int j = 0; j < dimensions.length; j++) {
                int n = dimensions[j][0];
                int a = dimensions[j][1];
                int b = dimensions[j][2];
                Percep p = new Percep(n, a, b, d);
                // p.train();
                p.validate();
                p.test();
                System.out.println();
            }
        }
    }
}