import model.Percep;
import model.Perceptron;
import util.Manager;

import java.util.*;

public class Main {

    
    /**
     * The best model we have trained achieved 93.34% accuracy on test data, and 87.04% accuracy on validation data.
     * 
     * {N = 168, A = 5, B = 5, Threshold = 0.7}
     */
    //  private static final Percep best = new Percep(168, 5, 5, 0.7);

    /**
     * {n, a, b} values for dimensions of interest
     */
    private static final int[][] face_dimensions = {{42,10,10},{168,5,5}, {1050,2,2}, {4200, 1, 1}};

    private static final int[][] digit_dimensions = {{49, 4, 4}, {196, 2, 2,}, {784, 1, 1}};

    private static Percep[] digit_best = {new Percep(Manager.PERCEPTRON_TYPE_DIGIT_0, 196, 2, 2, 0.5),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_1, 196, 2, 2, 0.6),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_2, 784, 1, 1, 0.1),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_3, 784, 1, 1, 0.1),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_4, 196, 2, 2, 1.0),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_5, 196, 2, 2, 0.1),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_6, 784, 1, 1, 0.8),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_7, 196, 2, 2, 0.1),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_8, 784, 1, 1, 0.6),
                                            new Percep(Manager.PERCEPTRON_TYPE_DIGIT_9, 196, 2, 2, 0.7)};
    
    
                                            private static Percep face_best;

    public static void main(String[] args) {
        digit_best = new Percep[10];

        for(int i = 1; i <= 10; i++) {
            double threshold = (i)/(10.0);
            for(int j = 0; j < digit_dimensions.length; j++) {
                int n = digit_dimensions[j][0];
                int a = digit_dimensions[j][1];
                int b = digit_dimensions[j][2];
                
                Percep p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_0, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[0] == null || p.getTestAccuracy() > digit_best[0].getTestAccuracy()) {
                    digit_best[0] = p;
                }


                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_1, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[1] == null || p.getTestAccuracy() > digit_best[1].getTestAccuracy()) {
                    digit_best[1] = p;
                }


                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_2, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[2] == null || p.getTestAccuracy() > digit_best[2].getTestAccuracy()) {
                    digit_best[2] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_3, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[3] == null || p.getTestAccuracy() > digit_best[3].getTestAccuracy()) {
                    digit_best[3] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_4, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[4] == null || p.getTestAccuracy() > digit_best[4].getTestAccuracy()) {
                    digit_best[4] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_5, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[5] == null || p.getTestAccuracy() > digit_best[5].getTestAccuracy()) {
                    digit_best[5] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_6, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[6] == null || p.getTestAccuracy() > digit_best[6].getTestAccuracy()) {
                    digit_best[6] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_7, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[7] == null || p.getTestAccuracy() > digit_best[7].getTestAccuracy()) {
                    digit_best[7] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_8, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[8] == null || p.getTestAccuracy() > digit_best[8].getTestAccuracy()) {
                    digit_best[8] = p;
                }

                p = new Percep(Manager.PERCEPTRON_TYPE_DIGIT_9, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(digit_best[9] == null || p.getTestAccuracy() > digit_best[9].getTestAccuracy()) {
                    digit_best[9] = p;
                }
            }

            for(int j = 0; j < face_dimensions.length; j++) {
                int n = face_dimensions[j][0];
                int a = face_dimensions[j][1];
                int b = face_dimensions[j][2];

                Percep p = new Percep(Manager.PERCEPTRON_TYPE_FACE, n, a, b, threshold);
                // p.train();
                p.validate();
                p.test();
                if(face_best == null || p.getTestAccuracy() > face_best.getTestAccuracy()) {
                    face_best = p;
                }
            }
        }

        for(int i = 0; i < digit_best.length; i++) {
            System.out.println("The best accuracy for digit " + i + " is " + digit_best[i].getTestAccuracy() + " and is model '" + digit_best[i].toString() + "'");
        }
        System.out.println("The best accuracy for Faces is " + face_best.getTestAccuracy() + " and is model '" + face_best.toString() + "'");

        
    }
}