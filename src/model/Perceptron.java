package model;

import java.io.*;

public class Perceptron {

    private int length;
    private int width;

    /**
     * The current file pointer position.
     */
    private long position;

    /**
     * The maximum file position that was reached when training. Move to this position when validating.
     */
    private long max_position;
    double[] weights;

    /**
     * Results from training labels which truly denote whether or not face[i] is indeed a face.
     */
    boolean[] face;

    double tests;

    /**
     * The scalar weight of updating weights. Small alpha results in smaller changes, which is slower learning.
     */
    double alpha;

    /**
     *
     * @param length the length of the image
     * @param width the width of the image
     * @param tests the proportion of test cases to train on (e,g 0.1 == 10% of training data is used as training data, 90% is used as validation data)
     */
    public Perceptron(int length, int width, double tests) {
        alpha = 0.001;
        this.length = length;
        this.width = width;
        weights = new double[(length*width)+1]; //weights[0] does not contribute to any phi[i]
        this.tests = tests;
        this.face = new boolean[451];
        init();
    }

    private void init() {
        labels();
        weights();
    }

    private void weights() {
        for(int i = 0; i<weights.length; i++) {
            weights[i] = 0.0;
        }
    }

    /**
     * Determines the labeling of each face by accessing the file and storing the result in memory.
     *
     * It is much faster to have the answer stored than to look at the file every time we interested in the answer.
     */
    private void labels() {
        String filename = "../data/facedata/facedatatrainlabels";
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            // Read characters from the file
            int character;
            int i = 0;
            while ((character = file.read()) != -1) {
                char c = (char)(character);
                int v = Character.getNumericValue(c);
                if(v == 0) {
                    face[i] = false;
                    i++;
                }
                else if(v == 1){
                    face[i] = true;
                    i++;
                }
            }
            System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void train() {
        int[] phi = new int[(length*width)];
        int count = 0; //the image we are currently on

        boolean change = true; //boolean variable denoting whether f(x, w) changed during this training process

        long start = System.currentTimeMillis();
        int i = 0;
        while(change) { //train until the function does not change (i.e. weights do not get updated and the function is correct)
            change = false;
            i++;
            while(count < (int)(tests*451)) { //train on tests * size amount of data, reserve the rest as validation
                phi = phi(this.position, "../data/facedata/facedatatrain");
                if(update(phi, count)) {
                    change = true;
                }
                count++;
            }
            max_position = position;
            position = 0; //reset the file pointer position
        }
        long end = System.currentTimeMillis();

        System.out.println("Training took " + ( end - start) + " ms and " + i + " iterations");
    }

    public void write() {
        String filePath = "data.txt";

        try {
            // Create a FileWriter object with the given file path
            FileWriter fileWriter = new FileWriter(filePath);

            // Wrap the FileWriter in a BufferedWriter for efficient writing
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int i = 0; i < weights.length; i++) {
                bufferedWriter.write(String.valueOf(weights[i]));
                bufferedWriter.newLine();
            }

            // Close the BufferedWriter
            bufferedWriter.close();

            System.out.println("Numbers have been written to the file successfully.");
        } catch (IOException e) {
            // Handle any potential IO exceptions
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }

    }

    /**
     * Updates the weights of the function given the phi function and some x_i
     * @param phi phi of x_i
     * @param count x_i ; the image phi accounts for
     */
    private boolean update(int[] phi, int count) {
        double f = f(phi);
        if(f < 0 && face[count]) { //got it wrong, need to increment the weights
            weights[0] = weights[0] + alpha;
            for(int i = 0; i < phi.length; i++) {
                weights[i+1] = weights[i+1] + ((alpha)*(phi[i]));
            }
            return true; //update occurred
        }
        if(f >= 0 && !face[count]) { //got it wrong, need to decrement the weights
            weights[0] = weights[0] - alpha;
            for(int i = 0; i < phi.length; i++) {
                weights[i+1] = weights[i+1] - ((alpha)*(phi[i]));
            }
            return true; //update occurred
        }
        return false;
    }

    private double f(int[] phi) {
        double f = weights[0];
        for(int i = 0; i < phi.length; i++) {
            f += phi[i] * weights[i+1];
        }
        return f;
    }

    /**
     * Determines from the training labels whether the given element is truly a face or not.
     * @param element
     * @return
     */
    private boolean face(int element) {
        return face[element];
    }

    public void validate() {

        boolean[] face = new boolean[301]; //300 validation tests
        String filename = "../data/facedata/facedatavalidationlabels";
        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            // Read characters from the file
            int character;
            int i = 0;
            while ((character = file.read()) != -1) {
                char c = (char)(character);
                int v = Character.getNumericValue(c);
                if(v == 0) {
                    face[i] = false;
                    i++;
                }
                else if(v == 1){
                    face[i] = true;
                    i++;
                }
            }
            System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        double correct = 0;
        while(count < 301) { //train on tests * size amount of data, reserve the rest as validation
            int[] phi = phi(this.position, "../data/facedata/facedatavalidation");
            double f = f(phi);
            if ((f >= 0 && face[count]) || (f < 0 && !face[count])) {
                correct++;
            }
            count++;
        }

        System.out.println("Perceptron was correct " + (correct/(double)(count))*(100) + "% of the time");
    }

    /**
     * A sanity check on Perceptron. If perceptron was trained on 100% of test data, then by definition, Perceptron should be able to predict whether or not the test data is a face with 100% accuracy.
     */
    public void sanity() {
        int count = 0;
        int correct = 0;
        while(count < (int)(tests*451)) {
            int[] phi = phi(this.position, "../data/facedata/facedatatrain");
            double f = f(phi);
            if((f >= 0 && face[count]) || f < 0 && !face[count]) {
                correct++;
            }
            count++;
        }
        double p = ((double)(correct))/((double)(count));
        System.out.println("Perceptron was correct " + (p*100) + "% of the time");
    }

    /**
     * Calculates Phi(X_i) where X_i is some image which starts at file pointer ~position~
     *
     * @param position the file pointer position of an image X_i
     * @return Phi(X_i) of image X_i
     */
    private int[] phi(long position, String filename) {
        int[] phi = new int[(length*width)];
        //calculate phi; go through the image and determine if the pixel is white or black
        int charactersToRead = (60*70);

        try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
            long filePointer = position;
            int charactersRead = 0;

            // Move the file pointer to the position where we left off
            file.seek(filePointer);

            // Read characters from the file
            int character;
            while ((character = file.read()) != -1 && charactersRead < charactersToRead) {
//                System.out.print((char) character);
                if((char)(character) == '#') { //black pixel
                    phi[charactersRead] = 1;
                }
                charactersRead++;
            }

            // Save the current file pointer
            this.position = file.getFilePointer();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return phi;
    }
}
