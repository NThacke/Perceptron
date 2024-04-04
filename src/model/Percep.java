package model;

import util.Manager;

import java.io.*;
import java.util.Scanner;
import java.util.*;

public class Percep {

    double[] weights;

    /**
     * The incremental amount that weights are adjusted on each training iteration.
     */
    final double alpha = 0.001;

    double threshold;

    /**
     * The true labels of each image. This can be either training, validation, or test labels, depending upon which mode Perceptron is running in.
     */
    boolean[] labels;

    /**
     * The collection of Faces which this instance of Perceptron is either training, validating, or testing against.
     */
    Faces faces;

    List<Face> training;

    /**
     * The number of regions to split each image into.
     */
    int n;
    /**
     * The length of each region that the image is split into.
     */
    int a;
    /**
     * The width of each region that the image is split into.
     */
    int b;

    /**
     * The number of milliseconds it took for this instance of Perceptron to train.
     */
    private long training_time;

    public Percep(int n, int a, int b, double threshold) {
        weights = new double[n+1];
        training = new ArrayList<>();
        this.threshold = threshold;
        this.n = n;
        this.a = a;
        this.b = b;
        load();
        init();
    }

    private void init() {
        faces = new Faces(Manager.TRAINING);

    }

    /**
     * Extracts the labeling of each face by accessing the training data labeling file and stores the result in memory.
     *
     * It is much faster to have the answer stored than to look at the file every time we interested in the answer.
     */
    private void labels(int type) {
        labels = new boolean[faces.getFaces().size()];
        String filename = "";
        switch(type) {
            case Manager.TRAINING -> {
                filename = "../data/facedata/facedatatrainlabels";
            }
            case Manager.VALIDATE -> {
                filename = "../data/facedata/facedatavalidationlabels";
            }
            case Manager.TESTING -> {
                filename = "../data/facedata/facedatatestlabels";
            }
        }
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            // Read characters from the file
            int character;
            int i = 0;
            while ((character = file.read()) != -1) {
                char c = (char)(character);
                int v = Character.getNumericValue(c);
                if(v == 0) {
                    labels[i] = false;
                    i++;
                }
                else if(v == 1){
                    labels[i] = true;
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getTrainingSet() {
        int n = (int)(threshold*faces.getFaces().size());
        for(int i = 0; i < n; i++) {
            int r = Manager.random.nextInt(0, faces.getFaces().size());
            training.add(faces.getFaces().remove(r));
        }
    }

    public void train() {
        faces = new Faces(Manager.TRAINING);
        labels(Manager.TRAINING);
        getTrainingSet();
        long start = System.currentTimeMillis();
        boolean change = true;
        int i = 0;
        while(i < 100000 && change) {
            if(i % 1000 == 0) {
                System.out.println(i);
                for(int j = 0; j < weights.length; j++) {
                    System.out.print(weights[j] + "\t");
                }
                System.out.println();
            }
            i++;
            change = false;
            for(Face face : training) {
                int[] phi = face.phi(n, a, b);
                if(update(phi, face.getID())) {
                    change = true;
                }
            }
        }
        long end = System.currentTimeMillis();
        this.training_time = (end-start);

        //done training, save weights
        save();
        output();
    }

    private void output() {
        try {
            FileWriter writer = new FileWriter("src/data/output/output" + threshold + "txt");
            writer.write("Output File for Perceptron Model training at " + threshold*100 + "% training data\n");
            // Calculate minutes and seconds
            long seconds = training_time / 1000;
            long minutes = seconds / 60;
            seconds %= 60;

            // Format the result as "mm:ss"
            String formattedTime = String.format("%02d:%02d", minutes, seconds);
            writer.write("Training took " + formattedTime);
            writer.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current weights to an output file. The model does not need to train everytime it runs.
     */
    private void save() {
        try {
            FileWriter file = new FileWriter("src/data/weights/weights" + threshold + ".txt");
            for(int i = 0; i< weights.length; i++) {
                file.write(String.valueOf(weights[i]) + "\n");
            }
            file.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            File file = new File("src/data/weights/weights" + threshold + ".txt");
            Scanner s = new Scanner(file);
            int i = 0;
            while(s.hasNext()) {
                weights[i] = s.nextDouble();
                i++;
            }
            s.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the weights of the function given the phi function and some x_i
     * @param phi phi of x_i
     * @param count x_i ; the image phi accounts for
     */
    private boolean update(int[] phi, int count) {
        double f = f(phi);
        if(f < 0 && labels[count]) { //got it wrong, need to increment the weights
            weights[0] = weights[0] + alpha;
            boolean change = false;
            for(int i = 0; i < phi.length; i++) {
                if(Math.abs(weights[i+1] + (alpha)*(phi[i])) > alpha) {
                    change = true;
                }
                weights[i+1] = weights[i+1] + ((alpha)*(phi[i]));
            }
            return change;
        }
        if(f >= 0 && !labels[count]) { //got it wrong, need to decrement the weights
            weights[0] = weights[0] - alpha;
            boolean change = false;
            for(int i = 0; i < phi.length; i++) {
                if(Math.abs(weights[i+1] - (alpha)*(phi[i])) > alpha) {
                    change = true;
                }
                weights[i+1] = weights[i+1] - ((alpha)*(phi[i]));
            }
            return change; //update occurred
        }
        return false;
    }

    private double f(int[] phi) {
        double f = weights[0];
        for(int i = 0; i < phi.length; i++) {
            f += weights[i+1]*phi[i];
        }
        return f;
    }

    public long trainingTime() {
        return training_time;
    }

    public void validate() {
        faces = new Faces(Manager.VALIDATE);
        labels(Manager.VALIDATE);
        int correct = 0;
        for(Face face : faces.getFaces()) {
            int[] phi = face.phi(n, a, b);
            double f = f(phi);
            if(f >= 0 && labels[face.getID()] || f < 0 && !labels[face.getID()]) {
                correct++;
            }
        }
        System.out.println("Model was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %");

    }

    public void test() {
        faces = new Faces(Manager.TESTING);
        labels(Manager.TESTING);
        int correct = 0;
        for(Face face : faces.getFaces()) {
            int[] phi = face.phi(n, a, b);
            double f = f(phi);
            if(f >= 0 && labels[face.getID()] || f < 0 && !labels[face.getID()]) {
                correct++;
            }
        }
        System.out.println("Model was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %");
    }
}
