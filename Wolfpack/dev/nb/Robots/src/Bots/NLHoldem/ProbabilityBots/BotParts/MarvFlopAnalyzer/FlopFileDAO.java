package Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FlopFileDAO {
    
    public static String line="";
    public static final String
            dataFolder="C:/data/dev/svn_wolfpack/dev/WolfPack/nb/Robots/ResultsData/",
            dataFileName=dataFolder+"flopProbabilities.data";
    public static RandomAccessFile file;
    
    public static void main(String[] args) throws Exception {
        System.out.println(">>> start test");
        test();
        System.out.println(">>> finish test");
    }
    
    public static void test() throws Exception {
        FlopFileDAO ffd=new FlopFileDAO();
        ffd.init();
        System.out.println("2h2d2c2s3s"+"="+ffd.getWinProb("2h2d2c2s3s")); //first  (.999193)
        System.out.println("2h2d2c2s4s"+"="+ffd.getWinProb("2h2d2c2s4s")); //second (.999187)
        System.out.println("ThJd8h7c2s"+"="+ffd.getWinProb("ThJd8h7c2s")); //middle (.453076)
        System.out.println("ThTdTsQsKs"+"="+ffd.getWinProb("ThTdTsQsKs")); //last   (.777454)
    }
    
    public static void init() throws Exception {
        file=new RandomAccessFile(dataFileName, "r");
    }
    
    public static String getIndex(String line) throws Exception {
        StringTokenizer st=new StringTokenizer(line,",");
        return st.nextToken();
    }
    
    public static float getWinProb(String index) throws Exception {
        //System.out.println("getWinProb");
        long length=file.length();
        file.seek(0);
        String index2;
        int comparison=1,skip=(int)length/2,i=0;
        for (i=1;i<400;i++) {
            if (comparison>0) {
                comparison = search(skip,index,true);
                skip=Math.max(30,skip/2);
            } else if (comparison<0) {
                comparison = search(skip,index,false);
                skip=Math.max(30,skip/2);
            } else break;
        }
        if (i==400) throw new Exception("index not found:"+index);
        String winProb=line.substring(line.indexOf(",")+1);
        return Float.parseFloat(winProb);
    }
    
    private static int search(final int skip, final String index,final boolean forwards) throws IOException, Exception {
        int comparison;
        String index2;
        if (forwards) file.skipBytes(skip);
        else file.seek(Math.max(0,file.getFilePointer()-skip));
        file.seek(Math.max(0,file.getFilePointer()-30));
        line=file.readLine();
        line=file.readLine();
        index2=getIndex(line);
        comparison=index.compareTo(index2);
        return comparison;
    }
    
}