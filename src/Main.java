import model.Percep;
import model.Perceptron;
import java.util.*;

public class Main {

    
    /**
     * The best model we have trained achieved 93.34% accuracy on test data, and 87.04% accuracy on validation data.
     * 
     * {N = 168, A = 5, B = 5, Threshold = 0.7}
     */
     private static final Percep best = new Percep(168, 5, 5, 0.7);

    /**
     * {n, a, b} values for dimensions of interest
     */
    private static final int[][] dimensions = {{42,10,10},{168,5,5}, {1050,2,2}, {4200, 1, 1}};

    public static void main(String[] args) {

        for(int i = 1; i <= 10; i++) {
            double d = (double)(i) / 10.0;
            for(int j = 0; j < dimensions.length; j++) {
                int n = dimensions[j][0];
                int a = dimensions[j][1];
                int b = dimensions[j][2];
                Percep p = new Percep(n, a, b, d);
                // if(!(n == 42 || n == 168)) {
                //     p.train();
                // }
                p.sanity();
                p.validate();
                p.test();
                System.out.println();
            }
        }
    }
}