package com.ewb.FileSystem;

import java.io.*;

  public class FileAccess {
    
    public File inputFile;
    public FileInputStream in;
    public static FileWriter out;

    public void FileAccess() { }
    
    public String readAll(String inputFileName) throws Exception {
        openForRead(inputFileName);
        byte bt[] = new byte[(int)inputFile.length()];
        in.read(bt);
        String s = new String(bt);
        in.close();
        return s;
    }
    
    public String readLine() throws Exception {
        int i=0;
        byte bt;
        char c;
        String line="";
        if (in.available()==0) throw new Exception("end of file");
        while (in.available()>0 && i!=10 && i!=-1) {
            i=in.read();
            line+=(char) ((byte) i);
        }
        return line;
    }
    
    public char readChar() throws Exception {
        int i=in.read();
        byte bt = (byte) i;
        char c= (char) bt;
        return c;
    }
    
    public void openForWrite(String outputFileName) throws Exception {
        File outputFile = new File(outputFileName);
            out = new FileWriter(outputFileName);
    }

    public void openForWriteISO88592(String outputFileName) throws Exception {
        File outputFile = new File(outputFileName);
            out = new FileWriter(outputFileName);
    }

    public static void openForWriteUTF8(String outputFileName) throws Exception {
        File outputFile = new File(outputFileName);
            out = new FileWriter(outputFileName);
    }

    public void openForRead(String inputFileName) throws Exception {
        inputFile = new File(inputFileName);
        in = new FileInputStream(inputFile);
    }
    
    public static void write(String text) throws Exception {
        out.write(text);
        out.flush();
    }
    
    public static void closeForWrite() throws Exception {
        out.close();
    }

    public void writeToFile(String text, String fileLocation, String fileName) throws Exception {
        openForWrite(fileLocation + fileName);
        write(text);
        closeForWrite();
    }

    public void writeToFileISO88592(String text, String fileLocation, String fileName) throws Exception {
        openForWriteISO88592(fileLocation + fileName);
        write(text);
        closeForWrite();
    }

    public static void writeToFileUTF8(String text, String fileLocation, String fileName) throws Exception {
        openForWriteUTF8(fileLocation + fileName);
        write(text);
        closeForWrite();
    }
}