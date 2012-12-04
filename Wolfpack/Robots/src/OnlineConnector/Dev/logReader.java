package OnlineConnector.Dev;

import com.ewb.FileSystem.FileAccess;

public class logReader {
    
    public static final String
            dataFolder="C:/temp/",
            dataFileName=dataFolder+"bet365.log";
    
    public static void main(String args[]) throws Exception {
        go();
    }
    
    public static void go() throws Exception {
        FileAccess fa=new FileAccess();
        fa.openForRead(dataFileName);
        for (int i=1;i<=169;i++) {
            String line=fa.readLine();
            System.out.print(line);
        }
    }
    
}