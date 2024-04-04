package model;

import util.Manager;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Face {

    char[][] face;

    private int id;

    public Face(RandomAccessFile file) {
        face = new char[Manager.length][Manager.width];
        init(file);
    }

    private void init(RandomAccessFile file) {
        try {
            file.readLine();
            for(int i = 0; i < face.length; i++) {
                for(int j = 0; j < face[i].length; j++) {
                    int character = file.read();
                    if(character != -1) {
                        face[i][j] = (char)(character);
                    }
                }
            }
            file.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public int getID() {
        return id;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i<face.length; i++) {
            for(int j = 0; j<face[i].length; j++) {
                s.append(face[i][j]);
            }
        }
        return s.toString();
    }

    /**
     * Returns the feature vector of this face. This feature vector is guaranteed to have size *n*, and phi[i] is the number of pixels present in the region i.
     *
     * The image is split into *n* number of regions. For simplicity, these regions will have the same area. That is to say that the feature vector determines the number of pixels in each region of the image.
     *
     *
     * ----------
     *
     * We know that each face takes up a 60x70 grid. If we wish to split this into i even regions, we can define the boundaries of each region.
     *
     * The image can be split into n even regions with dimension a * b if and only if :
     *
     *   n * a * b = i * j
     *
     *   where the image has dimension i * j
     *
     * In other words, n must be divisible by i * j, and we can choose any factors of (i * j)/n to be the dimension of our region.
     *
     * For best results, it is recommended to brute force all of these to determine the best possible feature vector.
     *
     * @param n the length of the feature vector
     * @param a the length of each region
     * @param b the width of each region
     * @return a feature vector of size *n*
     */
    public int[] phi(int n, int a, int b) throws IllegalArgumentException {

        if(n * a * b != 60 * 70) {
            throw new IllegalArgumentException("n*a*b must equal 4200");
        }
        if(70%a != 0) {
            throw new IllegalArgumentException("a must divide 70");
        }
        if(60%b != 0) {
            throw  new IllegalArgumentException("b must divide 60");
        }
//        System.out.println("Calculting phi...");
        int[] arr = new int[n];

        //We need to split the image into *n* regions, each of dimension a*b

        //If we are on position (i,j), then we know that our region can be located at

        int cnt = 0;
        for (int i = 0; i <= face.length - a; i += a) {
            for (int j = 0; j <= face[i].length - b; j += b) {
                arr[cnt] = count(i, j, a, b);
//                System.out.println(arr[cnt]);
                cnt++;
            }
        }
//        System.out.println("Calculated phi.");
        return arr;
    }

    private int count(int startRow, int startCol, int a, int b) {
        int count = 0;
        for (int i = startRow; i < startRow + a; i++) {
            for (int j = startCol; j < startCol + b; j++) {
                if (face[i][j] == '#') {
                    count++;
                }
            }
        }
        return count;
    }
}
