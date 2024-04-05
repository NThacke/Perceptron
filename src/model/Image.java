package model;

import java.io.IOException;
import java.io.RandomAccessFile;

import util.Manager;

public class Image {
    
    private char[][] image;

    private int id;

    /**
     * Creates a new Image that Perceptron will train, validate, or test against.
     * 
     * @param filename the filename of which this image is being extracted from
     * @param file the RandomAccessFile which must point to filename
     */
    public Image(String filename, RandomAccessFile file) {

        if(filename.equals(Manager.FACE_TEST_DATA) || filename.equals(Manager.FACE_TRAINING_DATA) || filename.equals(Manager.FACE_VALIDATION_DATA)) {
            //read faces
            initFace(file);
        }
        else {
            //read digits
            initDigits(file);
        }
    }

    private void initFace(RandomAccessFile file) {
        image = new char[Manager.FACE_LENGTH][Manager.FACE_WIDTH];
        try {
            file.readLine();
            for(int i = 0; i < image.length; i++) {
                for(int j = 0; j < image[i].length; j++) {
                    int character = file.read();
                    if(character != -1) {
                        image[i][j] = (char)(character);
                    }
                }
            }
            file.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDigits(RandomAccessFile file) {
        image = new char[Manager.DIGIT_LENGTH][Manager.DIGIT_WIDTH];
        try {
            // file.readLine();
            for(int i = 0; i < image.length; i++) {
                for(int j = 0; j < image.length; j++) {
                    int c = file.read();
                    if(c != -1) {
                        image[i][j] = (char)(c);
                    }
                }
            }
            file.readLine();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public int[] phi(int n, int a, int b) {
        int[] arr = new int[n];

        int cnt = 0;
        for (int i = 0; i <= image.length - a; i += a) {
            for (int j = 0; j <= image[i].length - b; j += b) {
                arr[cnt] = count(i, j, a, b);
                cnt++;
            }
        }
        return arr;
    }

    private int count(int startRow, int startCol, int a, int b) {
        int count = 0;
        for (int i = startRow; i < startRow + a; i++) {
            for (int j = startCol; j < startCol + b; j++) {
                if(image[i][j] == '#' || image[i][j] == '+') {
                    count++;
                }
            }
        }
        return count;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                s.append(image[i][j]);
            }
        }
        return s.toString();
    }
}
