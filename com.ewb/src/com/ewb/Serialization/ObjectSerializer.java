package com.ewb.Serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer {

    public static void writeObject2File(Object model,String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(model);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Object readObjectFromFile(String filename) {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Object model = null;
        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            model = in.readObject();
            in.close();
        } catch (java.io.FileNotFoundException fne) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return model;
    }

}
