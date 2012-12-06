package com.ewb.Logging;

import java.io.*;

public class Logger {
    
    public FileWriter out;
    private String logFileName=File.separatorChar+"temp"+File.separatorChar+"temp.log";
    
    public Logger(String logFileName) throws Exception {
        this.logFileName=logFileName;
        File outputFile = new File(logFileName);
        out = new FileWriter(logFileName);
    }
    
    public void log(String msg) {
        try {
            out.write(msg);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void logWithTimestamp(String msg) throws Exception {
            out.write(msg+" ("+new java.util.Date()+")");
            out.flush();
    }
}