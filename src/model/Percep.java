package model;

import util.Manager;

import java.io.*;
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
    int[] labels;

    /**
     * The collection of Faces which this instance of Perceptron is either training, validating, or testing against.
     */
    Faces faces;

    List<Face> training;

    public Images images;

    List<Image> image_training;

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

    private int type;

    private String dir;

    private String outputDir;

    private double test_accuracy;

    private double validate_accuracy;

    public Percep(int n, int a, int b, double threshold) {
        weights = new double[n+1];
        training = new ArrayList<>();
        this.threshold = threshold;
        this.n = n;
        this.a = a;
        this.b = b;
        load();
    }

    /**
     * Creates a new instance of a Perceptron algorithm.
     * 
     * Perceptrion will be trained on *type* type of data, which must be either DIGITS or FACES. If an invalid option is chosen, a runtime exception will occur.
     * 
     * If the given type is DIGITS, a particular digit *must* be chosen within the range [0,9] inclusive. Perceptron is only trained on these digits, and one singular perceptron can only determine whether something is true or false, i.e., whether or not a digit is some type of digit or not.
     * 
     * @param type
     * @param n
     * @param a
     * @param b
     * @param threshold
     */
    public Percep(int type, int n, int a, int b, double threshold) {
        if(type == Manager.PERCEPTRON_TYPE_FACE) {
            if(n * a * b != 4200) {
                throw new IllegalArgumentException("n * a * b must be equal to 4200 for faces");
            }
            if(Manager.FACE_LENGTH % a != 0) {
                throw new IllegalArgumentException("70 must be divible by a for perceptron to operate on faces");
            }
            if(Manager.FACE_WIDTH % b != 0) {
                throw new IllegalArgumentException("60 must be divible by b for perceptron to operate on faces");
            }
        }
        else {
            if(n * a * b != 28 * 28) {
                throw new IllegalArgumentException("n * a * b must equal 28 * 28 for perceptron to operate on digits");
            }
            if(28%a != 0) {
                throw new IllegalArgumentException("28 must be divible by 'a' for perceptron to operate on digits");
            }
            if(28 % b != 0) {
                throw new IllegalArgumentException("28 msut be divible by 'b' for perceptron to operate on digits");
            }
        }
        weights = new double[n+1];
        this.type = type;
        this.a = a;
        this.b = b;
        this.n = n;
        this.threshold = threshold;
        init();
        load();
    }

    public double getTestAccuracy() {
        return test_accuracy;
    }

    public double getValidationAccuracy() {
        return validate_accuracy;
    }

    private void init() {
        switch(type) {
            case Manager.PERCEPTRON_TYPE_DIGIT_0 : {
                dir = Manager.DIGIT_ZERO_WEIGHTS;
                outputDir = Manager.DIGIT_ZERO_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_1 : {
                dir = Manager.DIGIT_ONE_WEIGHTS;
                outputDir = Manager.DIGIT_ONE_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_2 : {
                dir = Manager.DIGIT_TWO_WEIGHTS;
                outputDir = Manager.DIGIT_TWO_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_3 : {
                dir = Manager.DIGIT_THREE_WEIGHTS;
                outputDir = Manager.DIGIT_THREE_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_4 : {
                dir = Manager.DIGIT_FOUR_WEIGHTS;
                outputDir = Manager.DIGIT_FOUR_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_5 : {
                dir = Manager.DIGIT_FIVE_WEIGHTS;
                outputDir = Manager.DIGIT_FIVE_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_6 : {
                dir = Manager.DIGIT_SIX_WEIGHTS;
                outputDir = Manager.DIGIT_SIX_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_7 : {
                dir = Manager.DIGIT_SEVEN_WEIGHTS;
                outputDir = Manager.DIGIT_SEVEN_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_8 : {
                dir = Manager.DIGIT_EIGHT_WEIGHTS;
                outputDir = Manager.DIGIT_EIGHT_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_9 : {
                dir = Manager.DIGIT_NINE_WEIGHTS;
                outputDir = Manager.DIGIT_NINE_OUTPUT_DIR;
                break;
            }
            case Manager.PERCEPTRON_TYPE_FACE : {
                dir = Manager.FACE_WEIGHTS;
                outputDir = Manager.FACE_OUTPUT_DIR;
                break;
            }
            default : {
                throw new IllegalArgumentException("Perceptron was not given a valid TYPE parameter.");
            }
        }   
    }
    private void labels(String filename) {
        labels = new int[images.getImages().size()];
        try {
            Scanner s = new Scanner(new File(filename));
            int i = 0;
            while(s.hasNext()) {
                labels[i] = s.nextInt();
                i++;
            }
            s.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void getTrainingSet() {
        image_training = new ArrayList<>();
        if(threshold == 1.0) {
            image_training = images.getImages();
        }
        else {
            int n = (int)(threshold*images.getImages().size());
            for(int i = 0; i < n; i++) {
                int r = Manager.random.nextInt(images.getImages().size());
                image_training.add(images.getImages().remove(r));
            }
        }
    }

    public void train() {
        if(type == Manager.PERCEPTRON_TYPE_FACE) {
            images = new Images(Manager.FACE_TRAINING_DATA); 
            labels(Manager.FACE_TRAINING_LABELS);   
        }
        else {
            images = new Images(Manager.DIGIT_TRAINING_DATA);
            labels(Manager.DIGIT_TRAINING_LABELS);
        }
        getTrainingSet();

        long start = System.currentTimeMillis();
        boolean change = true;
        int i = 0;
        while(i < 10000 && change) {
            if(i % 1000 == 0) {
                System.out.println(i);
                System.out.println(this.toString());
                for(int j = 0; j < weights.length; j++) {
                    System.out.print(weights[j] + "\t");
                }
                System.out.println();
            }
            i++;
            change = false;
            for(Image image : image_training) {
                int[] phi = image.phi(n, a, b);
                if(update(phi, image.getID())) {
                    change = true;
                }
            }
        }
        long end = System.currentTimeMillis();
        this.training_time = (end-start);

        // //done training, save weights
        save();
        output();
    }

    private void output() {
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt");
            writer.write("Output File for Perceptron Model training at " + threshold*100 + "% training data\n\n");
            // Calculate minutes and seconds
            long seconds = training_time / 1000;
            long minutes = seconds / 60;
            seconds %= 60;

            // Format the result as "mm:ss"
            String formattedTime = String.format("%02d:%02d", minutes, seconds);
            writer.write("Training took " + formattedTime + "\n");
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
            FileWriter file = new FileWriter(dir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt");
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
            File file = new File(dir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt");
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

    private boolean update(int[] phi, int id) {
        switch(type) {
            case Manager.PERCEPTRON_TYPE_DIGIT_0 : {
                return updateZero(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_1 : {
                return updateOne(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_2 : {
                return updateTwo(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_3 : {
                return updateThree(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_4 : {
                return updateFour(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_5 : {
                return updateFive(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_6 : {
                return updateSix(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_7 : {
                return updateSeven(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_8 : {
                return updateEight(phi, id);
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_9 : {
                return updateNine(phi, id);
            }
            default : {
                return updateFace(phi, id);
            }
        }
    }

    private boolean updateFace(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 1) { //is a face
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] == 0) { //not a face
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateZero(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 0) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 0) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateOne(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 1) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 1) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateTwo(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 2) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 2) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateThree(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 3) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 3) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateFour(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 4) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 4) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateFive(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 5) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 5) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateSix(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 6) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 6) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateSeven(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 7) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 7) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateEight(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 8) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 8) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updateNine(int[] phi, int id) {
        double f = f(phi);
        if(f < 0 && labels[id] == 9) {
            return updatePlus(phi);
        }
        if(f >= 0 && labels[id] != 9) {
            return updateMinus(phi);
        }
        return false;
    }

    private boolean updatePlus(int[] phi) {
        boolean change = false;
        for(int i = 0; i < phi.length; i++) {
            if(Math.abs(weights[i+1] + (alpha)*(phi[i])) > alpha) {
                change = true;
            }
            weights[i+1] = weights[i+1] + ((alpha)*(phi[i]));
        }
        return change;
    }

    private boolean updateMinus(int[] phi) {
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
        switch(type) {
            case Manager.PERCEPTRON_TYPE_DIGIT_0 : {
                validateDigitZero();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_1 : {
                validateDigitOne();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_2 : {
                validateDigitTwo();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_3 : {
                validateDigitThree();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_4 : {
                validateDigitFour();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_5 : {
                validateDigitFive();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_6 : {
                validateDigitSix();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_7 : {
                validateDigitSeven();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_8 : {
                validateDigitEight();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_9 : {
                validateDigitNine();
                break;
            }
            default : {
                validateFaces();
            }
        }
    }

    private void validateFaces() {
        images = new Images(Manager.FACE_VALIDATION_DATA);
        labels(Manager.FACE_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 1) || (f < 0 && labels[image.getID()] == 0)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testFaces() {
        images = new Images(Manager.FACE_TEST_DATA);
        labels(Manager.FACE_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 1) || (f < 0 && labels[image.getID()] == 0)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitZero() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 0) || (f < 0 && labels[image.getID()] != 0)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitZero() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 0) || (f < 0 && labels[image.getID()] != 0)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitOne() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 1) || (f < 0 && labels[image.getID()] != 1)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitOne() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 1) || (f < 0 && labels[image.getID()] != 1)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitTwo() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 2) || (f < 0 && labels[image.getID()] != 2)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitTwo() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 2) || (f < 0 && labels[image.getID()] != 2)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitThree() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 3) || (f < 0 && labels[image.getID()] != 3)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitThree() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 3) || (f < 0 && labels[image.getID()] != 3)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitFour() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 4) || (f < 0 && labels[image.getID()] != 4)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitFour() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 4) || (f < 0 && labels[image.getID()] != 4)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitFive() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 5) || (f < 0 && labels[image.getID()] != 5)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitFive() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 5) || (f < 0 && labels[image.getID()] != 5)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitSix() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 6) || (f < 0 && labels[image.getID()] != 6)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitSix() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 6) || (f < 0 && labels[image.getID()] != 6)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitSeven() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 7) || (f < 0 && labels[image.getID()] != 7)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitSeven() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 7) || (f < 0 && labels[image.getID()] != 7)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void validateDigitEight() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 8) || (f < 0 && labels[image.getID()] != 8)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitEight() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 8) || (f < 0 && labels[image.getID()] != 8)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDigitNine() {
        images = new Images(Manager.DIGIT_VALIDATION_DATA);
        labels(Manager.DIGIT_VALIDATION_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 9) || (f < 0 && labels[image.getID()] != 9)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Validate : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.validate_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void testDigitNine() {
        images = new Images(Manager.DIGIT_TEST_DATA);
        labels(Manager.DIGIT_TEST_LABELS);
        int correct = 0;
        for(Image image : images.getImages()) {
            int[] phi = image.phi(n, a, b);
            double f = f(phi);
            if((f >= 0 && labels[image.getID()] == 9) || (f < 0 && labels[image.getID()] != 9)) {
                correct++;
            }
        }
        try {
            FileWriter writer = new FileWriter(outputDir + "n:" + n + "_a:" + a + "_b:" + b + " _d:" + threshold + ".txt", true);
            writer.write("Test : Model " + this.toString() + " was correct " + correct + " number of times out of " + labels.length + " for an accuracy of " + (double)(correct)/(double)(labels.length)*100 + " %\n");
            writer.close();
            this.test_accuracy = (double)(correct)/(double)(labels.length)*100;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        switch(type) {
            case Manager.PERCEPTRON_TYPE_DIGIT_0 : {
                testDigitZero();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_1 : {
                testDigitOne();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_2 : {
                testDigitTwo();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_3 : {
                testDigitThree();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_4 : {
                testDigitFour();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_5 : {
                testDigitFive();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_6 : {
                testDigitSix();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_7 : {
                testDigitSeven();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_8 : {
                testDigitEight();
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_9 : {
                testDigitNine();
                break;
            }
            default : {
                testFaces();
            }
        }
    }

    public String toString() {
        StringBuilder s= new StringBuilder();
        s.append("{");
        switch(type) {
            case Manager.PERCEPTRON_TYPE_DIGIT_0 : {
                s.append("Digit Zero,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_1 : {
                s.append("Digit One,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_2 : {
                s.append("Digit Two,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_3 : {
                s.append("Digit Three,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_4 : {
                s.append("Digit Four,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_5 : {
                s.append("Digit Five,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_6 : {
                s.append("Digit Six,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_7 : {
                s.append("Digit Seven,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_8 : {
                s.append("Digit Eight,");
                break;
            }
            case Manager.PERCEPTRON_TYPE_DIGIT_9 : {
                s.append("Digit Nine,");
                break;
            }
            default : {
                s.append("Faces,");
            }
        }
        s.append(" N : " + n);
        s.append(", A : " + a);
        s.append(", B : " + b);
        s.append(", Threshold : " + threshold + "}");
        return s.toString();
    }
}
