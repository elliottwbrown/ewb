package Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer;

import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.Utilities.HoleCardKeys;
import com.ewb.FileSystem.FileAccess;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.StringTokenizer;

public class genHoleCardXML {
    
    public static final String
            dataFolder="C:/data/dev/svn_wolfpack/dev/WolfPack/nb/Robots/ResultsData/",
            dataFileName=dataFolder+"holeCardOdds.data",
            XMLFileName=dataFolder+"holeCardOdds.xml";
    public HashMap<Integer,Float> results;
    
    public static void main(String[] args) throws Exception {
        genHoleCardXML h=new genHoleCardXML();
        h.convertCardLogsToXML();
    }
    
    
    public void init() throws Exception {
        readResultsFromXML();
    }
    
    public void readResultsFromXML() throws FileNotFoundException {
        System.out.println("  loading hole card data from:"+XMLFileName);
        XMLDecoder d = new XMLDecoder(
                new BufferedInputStream(
                new FileInputStream(XMLFileName)));
        results= (HashMap) d.readObject();
        System.out.println("results="+results);
        d.close();
    }
    
    public void convertCardLogsToXML() throws Exception {
        writeToXML(readResultsFromTableFile(),XMLFileName);
    }
    
    public HashMap readResultsFromTableFile() throws Exception {
        results=new HashMap();
        FileAccess fa=new FileAccess();
        fa.openForRead(dataFileName);
        fa.readLine(); // burn the column header line
        for (int i=1;i<=169;i++) {
            String line=fa.readLine();
            StringTokenizer st= new StringTokenizer(line);
            String index=st.nextElement().toString();
            boolean suited=index.contains("s");
            int face1=face.faceShortNamesStr.indexOf(index.charAt(0));
            int face2=face.faceShortNamesStr.indexOf(index.charAt(1));
            int indexInt=HoleCardKeys.getIntKeyFromInts(face1,face2,suited);
            float e=Float.parseFloat(st.nextElement().toString());
            results.put(indexInt,e);
            System.out.println(indexInt+" "+e);
        }
        return results;
    }
    
    private void writeToXML(HashMap results,String xmlFileName) throws Exception {
        XMLEncoder e = new XMLEncoder(
                new BufferedOutputStream(
                new FileOutputStream(xmlFileName)));
        e.writeObject(results);
        e.close();
    }
    
    private class ResultsBean implements Serializable {
        HashMap results = new HashMap();
        
        public ResultsBean(HashMap results) {
            this.results=results;
        }
        
        public HashMap getResults() {
            return results;
        }
    }
}