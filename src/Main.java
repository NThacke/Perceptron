import model.Percep;
import model.Perceptron;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        for(int i = 1; i <= 10; i++) {
            double d = (double)(i) / 10.0;
            for(int n = 1; n <= (60*70); n++) {
                for(int a = 1; a <= 70; a++) {
                    if(70%a == 0) {
                        for(int b = 1; b <= 60; b++) {
                            if(60%b == 0 && n * a * b == 4200) {
                                Percep p = new Percep(n,a,b,d);
                                p.train();
//                                p.sanity();
//                                p.validate();
//                                p.test();
                                System.out.println();
                            }
                        }
                    }
                }
            }
//            Percep p = new Percep(25, 14, 12, d);
////            p.train();
//            p.sanity();
//            p.validate();
//            p.test();
//            System.out.println();
        }
    }
}