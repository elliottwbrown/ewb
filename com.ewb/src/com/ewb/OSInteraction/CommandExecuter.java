package com.ewb.OSInteraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecuter {

     private static final int debugLevel=1;

     public static void runDOSCommand(String cmdline) {
        if (debugLevel>1) System.out.println("runDOSCommand: " + cmdline);
        String line;
        try {
            Process p = Runtime.getRuntime().exec(cmdline);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (debugLevel>0) System.out.println(line);
            }
            input.close();
            if (debugLevel>1) System.out.println("Process exit code is: " + p.exitValue());
        } catch (IOException e) {
            System.err.println("IOException starting process!");
            e.printStackTrace();
        }
    }
}
