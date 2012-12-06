package Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer;

import com.ewb.FileSystem.FileAccess;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.HashMap;
import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Utilities.HoleCardKeys;

public class HoleCardsDAO {
    
    public HashMap
            //resultsXML3player,
            resultsXML2player;
    public static final String
            xmlFolder="C:/data/dev/svn_wolfpack/dev/WolfPack/nb/Robots/ResultsData/",
            logFolder="C:/temp/",
            outputFolder="C:/temp/",
            //xmlFileName2player=xmlFolder+"2PlayerResults.xml",
            xmlFileName2player=xmlFolder+"holeCardOdds.xml",
            //xmlFileName3player=xmlFolder+"3PlayerResults.xml",
            winningCardsLogFilename=logFolder+"WinningCards.log",
            allCardsLogFilename=logFolder+"AllCards.log";
    
    public static void main(String[] args) throws Exception {
        HoleCardsDAO h=new HoleCardsDAO();
        h.init();
        System.out.println(h.resultsXML2player);
       // h.convertCardLogsToXML();
    }
    
    public void init() throws Exception {
        readResultsFromXML();
    }
    
    private HashMap mergeCardLogs(HashMap map1,HashMap map2) throws Exception {
        HashMap merged=new HashMap();
        Iterator i=map1.keySet().iterator();
        while (i.hasNext()) {
            Integer key= (Integer) i.next();
            Integer[] results={(Integer) map1.get(key),(Integer) map2.get(key)};
            merged.put(key,results);
        }
        return merged;
    }
    
    private HashMap readAndParseCardsLog(String logFileName,String delim) throws Exception {
        FileAccess fa=new FileAccess();
        String log=fa.readAll(logFileName);
        if (!delim.equals("\r")) log=log.replaceAll("\n","").replaceAll("\r","");
        
        StringTokenizer st=new StringTokenizer(log,delim);
        HashMap appearances= new HashMap();
        HashMap wins= new HashMap();
        
        while (st.hasMoreTokens()) {
            String hand=st.nextToken();
            boolean winner=hand.contains("x");
            StringTokenizer cards=new StringTokenizer(hand," ");
            Card card1=null,card2=null;
            card1=new Card(cards.nextToken());
            card2=new Card(cards.nextToken());
            int key = HoleCardKeys.getIntKeyFromCards(card1, card2);
            if (appearances.containsKey(key)) appearances.put(key,(Integer) appearances.get(key)+1);
            else appearances.put(key,1);
            if (winner) {
                if (wins.containsKey(key)) wins.put(key,(Integer) wins.get(key)+1);
                else wins.put(key,1);
            }
        }
        return mergeCardLogs(wins,appearances);
    }
    
    private HashMap readAndParseCardsLogOld(String logFileName,String delim) throws Exception {
        FileAccess fa=new FileAccess();
        String log=fa.readAll(logFileName);
        if (!delim.equals("\r")) log=log.replaceAll("\n","").replaceAll("\r","");
        
        StringTokenizer st=new StringTokenizer(log,delim);
        HashMap totals = new HashMap();
        while (st.hasMoreTokens()) {
            String hand=st.nextToken();
            StringTokenizer cards=new StringTokenizer(hand," ");
            Card card1=null,card2=null;
            card1=new Card(cards.nextToken());
            card2=new Card(cards.nextToken());
            int key = HoleCardKeys.getIntKeyFromCards(card1, card2);
            if (totals.containsKey(key)) totals.put(key,(Integer) totals.get(key)+1);
            else totals.put(key,1);
        }
        return totals;
    }
    
    public void readResultsFromXML() throws FileNotFoundException {
        readResultsFromXML(xmlFileName2player);
    }
    
    public void readResultsFromXML(String fileName) throws FileNotFoundException {
        System.out.println("loading hole card data from:"+fileName);
        XMLDecoder d = new XMLDecoder(
                new BufferedInputStream(
                new FileInputStream(fileName)));
        resultsXML2player= (HashMap) d.readObject();
        d.close();
    }
    
    public void convertCardLogsToXML(String xmlFileName) throws Exception {
        HashMap index=readAndParseCardsLog(allCardsLogFilename,"\r");
        System.out.println("writing to file ...");
        writeToXML(index,xmlFileName);
    }
    
    public void convertCardLogsToXMLOld(String xmlFileName) throws Exception {
        System.out.println("merging winning cards log and cards log ...");
        HashMap index=
                mergeCardLogs(readAndParseCardsLog(winningCardsLogFilename,","),readAndParseCardsLog(allCardsLogFilename,"\r"));
        System.out.println("writing to file ...");
        writeToXML(index,xmlFileName);
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