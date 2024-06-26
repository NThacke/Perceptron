package util;

import java.util.Random;

public abstract class Manager {
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

}
