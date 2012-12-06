package com.ewb.FileSystem;

import java.io.*;
import java.util.*;

public class RenFSObject {

    public static void main(String[] args) {
    }

    public static void rename(String from, String to) {
        Runtime rt = Runtime.getRuntime();
        String cmdline = "cmd /c ren " + from + " " + to;
        System.out.println(cmdline);
        String line;
        try {
            Process p = Runtime.getRuntime().exec(cmdline);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
            System.out.println("Process exit code is: " + p.exitValue());
        } catch (IOException e) {
            System.err.println("IOException starting process!");
            e.printStackTrace();
        }
    }
}
