package model;

import util.Manager;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.*;

public class Faces {

    /**
     * The file pointer position.
     */
    long position;

    private List<Face> list;

    public Faces(int type) {
        list = new ArrayList<>();
        init(type);
    }

    private void init(int type) {
        String filename = "";
        switch(type) {
            case Manager.TESTING : {
                filename = Manager.FACE_TEST_DATA;
                break;
            }
            case Manager.VALIDATE : {
                filename = Manager.FACE_VALIDATION_DATA;
                break;
            }
            case Manager.TRAINING : {
                filename = Manager.FACE_TRAINING_DATA;
                break;
            }
            default : {
                throw new IllegalArgumentException(type + " is not a valid type to invoke");
            }
        }
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
//            System.setOut(new PrintStream(new FileOutputStream("output.txt")));
            while(file.getFilePointer() < file.length()) {
                Face f = new Face(file);
                f.setID(list.size());
//                System.out.print(f);
//                System.out.println(f.getID());
//                System.out.println();
                list.add(f);
            }
//            System.setOut(System.out);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Face> getFaces() {
        return list;
    }
}
