package model;

import java.io.RandomAccessFile;
import java.util.*;

import util.Manager;

public class Images {
    

    private List<Image> images;

    public Images(String filename) {
        images = new ArrayList<>();
        init(filename);
    }

    private void init(String filename) {
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            int id = 0;
            while(file.getFilePointer() < file.length()) {
                Image i = new Image(filename, file);
                i.setID(id);
                images.add(i);
                id++;
            }
            file.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Image> getImages() {
        return images;
    }
}
