package com.ewb.OSInteraction;

import com.ewb.FileSystem.FileAccess;
import java.io.*;

class StreamGobbler extends Thread {
    InputStream is;
    String type;
    String logLocation=".";
    
    StreamGobbler(String logLocation, InputStream is, String type) {
        this.is = is;
        this.type = type;
        this.logLocation=logLocation;
    }
    
    public void run() {
        FileAccess fs=new FileAccess();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fs.openForWrite(logLocation+"/exporta_"+type+".log");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                System.out.println(type + ">" + line);
                fs.write(line+"\n");
                fs.out.flush();
            }
            fs.closeForWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

