package util;

import java.util.Random;

public abstract class Manager {

    public static final int FACE_LENGTH = 70;

    public static final int FACE_WIDTH = 60;

    public static final int DIGIT_LENGTH = 28;

    public static final int DIGIT_WIDTH = 28;

    /**
     * The length (number of rows) in each image.
     */
    public static final int length = 70;

    /**
     * The width (number of columns) in each image.
     */
    public static final int width = 60;

    /**
     * A seed used for pseudo-random number generation.
     */
    private static final long seed = 1;

    /**
     * A Random class which is used to draw random numbers from. A seed can be used to have deterministic results.
     */
    public static final Random random = new Random(seed);

    /**
     * A delimiter to denote that Perceptron is in training mode.
     */
    public static final int TRAINING = 1;

    /**
     * A unique delimiter to denote that Perceptron is in validation mode.
     */
    public static final int VALIDATE = 2;

    /**
     * A unique delimiter to denote that Perceptron is in testing mode.
     */

    public static final int TESTING = 3;

    /**
     * The path to the face training labels.
     */
    public static final String FACE_TRAINING_LABELS = "data/facedata/facedatatrainlabels";

    /**
     * The path to the face validation labels.
     */
    public static final String FACE_VALIDATION_LABELS = "data/facedata/facedatavalidationlabels";

    /**
     * The path to the face testing labels.
     */
    public static final String FACE_TEST_LABELS = "data/facedata/facedatatestlabels";

    /**
     * The path to the face test data.
     */
    public static final String FACE_TEST_DATA = "data/facedata/facedatatest";

    /**
     * The path to the face validation data.
     */
    public static final String FACE_VALIDATION_DATA = "data/facedata/facedatavalidation";

    /**
     * The path to the face training data.
     */
    public static final String FACE_TRAINING_DATA = "data/facedata/facedatatrain";

    /**
     * The path to the digit training data.
     */
    public static final String DIGIT_TRAINING_DATA = "data/digitdata/trainingimages";

    /**
     * The path to the digit training labels.
     */
    public static final String DIGIT_TRAINING_LABELS = "data/digitdata/traininglabels";

    /**
     * The path to the digit validation data.
     */
    public static final String DIGIT_VALIDATION_DATA = "data/digitdata/validationimages";

    /**
     * The path to the digit validation labels.
     */
    public static final String DIGIT_VALIDATION_LABELS = "data/digitdata/validationlabels";

    /**
     * The path to the digit test data.
     */
    public static final String DIGIT_TEST_DATA = "data/digitdata/testimages";

    /**
     * The path to the digit test labels.
     */
    public static final String DIGIT_TEST_LABELS = "data/digitdata/testlabels";

    /**
     * 
     * THE FOLLOWING CONSTANTS ARE USED FOR PERPCEPTRON PARAMETERS.
     *  
     */


    /**
     * The parameter type that must be sent to a Perceptron instance to run on FACES.
     */
    public static final int PERCEPTRON_TYPE_FACE = -1;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 0 (ZERO).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_0 = 0;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 1 (ONE).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_1 = 1;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 2 (TWO).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_2 = 2;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 3 (THREE).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_3 = 3;


    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 4 (FOUR).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_4 = 4;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 5 (FIVE).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_5 = 5;


    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 6 (SIX).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_6 = 6;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 7 (SEVEN).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_7 = 7;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 8 (EIGHT).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_8 = 8;

    /**
     * The parameter type that must be sent to a Perceptron instance to run on the DIGIT 9 (NINE).
     */
    public static final int PERCEPTRON_TYPE_DIGIT_9 = 9;

    
    public static final int IMAGE_TYPE_DIGIT_0_TRAINING = 0;
public static final int IMAGE_TYPE_DIGIT_0_VALIDATION = 1;
public static final int IMAGE_TYPE_DIGIT_0_TEST = 2;

public static final int IMAGE_TYPE_DIGIT_1_TRAINING = 3;
public static final int IMAGE_TYPE_DIGIT_1_VALIDATION = 4;
public static final int IMAGE_TYPE_DIGIT_1_TEST = 5;

public static final int IMAGE_TYPE_DIGIT_2_TRAINING = 6;
public static final int IMAGE_TYPE_DIGIT_2_VALIDATION = 7;
public static final int IMAGE_TYPE_DIGIT_2_TEST = 8;

public static final int IMAGE_TYPE_DIGIT_3_TRAINING = 9;
public static final int IMAGE_TYPE_DIGIT_3_VALIDATION = 10;
public static final int IMAGE_TYPE_DIGIT_3_TEST = 11;

public static final int IMAGE_TYPE_DIGIT_4_TRAINING = 12;
public static final int IMAGE_TYPE_DIGIT_4_VALIDATION = 13;
public static final int IMAGE_TYPE_DIGIT_4_TEST = 14;

public static final int IMAGE_TYPE_DIGIT_5_TRAINING = 15;
public static final int IMAGE_TYPE_DIGIT_5_VALIDATION = 16;
public static final int IMAGE_TYPE_DIGIT_5_TEST = 17;

public static final int IMAGE_TYPE_DIGIT_6_TRAINING = 18;
public static final int IMAGE_TYPE_DIGIT_6_VALIDATION = 19;
public static final int IMAGE_TYPE_DIGIT_6_TEST = 20;

public static final int IMAGE_TYPE_DIGIT_7_TRAINING = 21;
public static final int IMAGE_TYPE_DIGIT_7_VALIDATION = 22;
public static final int IMAGE_TYPE_DIGIT_7_TEST = 23;

public static final int IMAGE_TYPE_DIGIT_8_TRAINING = 24;
public static final int IMAGE_TYPE_DIGIT_8_VALIDATION = 25;
public static final int IMAGE_TYPE_DIGIT_8_TEST = 26;

public static final int IMAGE_TYPE_DIGIT_9_TRAINING = 27;
public static final int IMAGE_TYPE_DIGIT_9_VALIDATION = 28;
public static final int IMAGE_TYPE_DIGIT_9_TEST = 29;

public static final int IMAGE_TYPE_FACE_TRAINING = 30;
public static final int IMAGE_TYPE_FACE_VALIDATE = 31;
public static final int IMAGE_TYPE_FACE_TEST = 32;

    

public static final String DIGIT_ZERO_WEIGHTS = "src/data/weights/digits/zero/";
public static final String DIGIT_ONE_WEIGHTS = "src/data/weights/digits/one/";
public static final String DIGIT_TWO_WEIGHTS = "src/data/weights/digits/two/";
public static final String DIGIT_THREE_WEIGHTS = "src/data/weights/digits/three/";
public static final String DIGIT_FOUR_WEIGHTS = "src/data/weights/digits/four/";
public static final String DIGIT_FIVE_WEIGHTS = "src/data/weights/digits/five/";
public static final String DIGIT_SIX_WEIGHTS = "src/data/weights/digits/six/";
public static final String DIGIT_SEVEN_WEIGHTS = "src/data/weights/digits/seven/";
public static final String DIGIT_EIGHT_WEIGHTS = "src/data/weights/digits/eight/";
public static final String DIGIT_NINE_WEIGHTS = "src/data/weights/digits/nine/";

public static final String FACE_WEIGHTS = "src/data/weights/faces/";

public static final String FACE_OUTPUT_DIR = "src/data/output/faces/";

public static final String DIGIT_ZERO_OUTPUT_DIR = "src/data/output/digits/zero/";
public static final String DIGIT_ONE_OUTPUT_DIR = "src/data/output/digits/one/";
public static final String DIGIT_TWO_OUTPUT_DIR = "src/data/output/digits/two/";
public static final String DIGIT_THREE_OUTPUT_DIR = "src/data/output/digits/three/";
public static final String DIGIT_FOUR_OUTPUT_DIR = "src/data/output/digits/four/";
public static final String DIGIT_FIVE_OUTPUT_DIR = "src/data/output/digits/five/";
public static final String DIGIT_SIX_OUTPUT_DIR = "src/data/output/digits/six/";
public static final String DIGIT_SEVEN_OUTPUT_DIR = "src/data/output/digits/seven/";
public static final String DIGIT_EIGHT_OUTPUT_DIR = "src/data/output/digits/eight/";
public static final String DIGIT_NINE_OUTPUT_DIR = "src/data/output/digits/nine/";


}
