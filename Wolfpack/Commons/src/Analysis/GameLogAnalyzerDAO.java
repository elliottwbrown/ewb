package Analysis;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;
import PokerCommons.PokerObjects.Utilities.HoleCardKeys;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.StringTokenizer;
import com.ewb.FileSystem.FileAccess;

public class GameLogAnalyzerDAO implements GameValues {
    
    public static final String
            dataFolder="C:/data/dev/svnco_server/wolfPack/dev/Robots/ResultsData",
            xmlFileName2player=dataFolder+"2PlayerHoldingResults.xml",
            dataFileName=dataFolder+"Games.log";
    public HashMap<Integer,Integer[]>
            results;
    
    public static void main(String args[]) throws Exception {
        GameLogAnalyzerDAO gladao= new GameLogAnalyzerDAO();
        //gladao.createXMLDataFromLog();
        gladao.testGetResults();
    }
    
    private void testGetResults() throws Exception {
        readResultsFromXML();
        System.out.println("result:");
        int c=1;
        for (int firstCard=0;firstCard<13;firstCard++)
            for (int secondCard=firstCard;secondCard<13;secondCard++) {
            System.out.print(c+++":["+ face.faceShortNames[firstCard] + " " + face.faceShortNames[secondCard]+"]u=\t");
            for (int t=1;t<5;t++) System.out.print(getResults(firstCard,secondCard,false,t)+"\t");
            System.out.println();
            }
        for (int firstCard=0;firstCard<13;firstCard++)
            for (int secondCard=firstCard+1;secondCard<13;secondCard++) {
            System.out.print(c+++":["+ face.faceShortNames[firstCard] + " " + face.faceShortNames[secondCard]+"]s=\t");
            for (int t=1;t<5;t++) System.out.print(getResults(HoleCardKeys.getIntKeyFromInts(firstCard,secondCard,true),t)+"\t");
            System.out.println();
            }
    }
    
    public void createXMLDataFromLog() throws Exception {
        writeToXML(readResultsFromLog(),xmlFileName2player);
    }
    
    public HashMap<Integer,Integer[]> readResultsFromLog() throws Exception {
        results=new HashMap();
        FileAccess fa=new FileAccess();
        fa.openForRead(dataFileName);
        try {
            int lastRound=-1;
            int foldedInRound=-1;
            String foldedPlayer="";
            while (true) {
                String line=fa.readLine();
                StringTokenizer st=new StringTokenizer(line);
                if(line.contains("started")) {
                    lastRound=0;
                    foldedInRound=0;
                    foldedPlayer="";
                    System.out.print(line);
                }
                if(line.contains("Deal")) lastRound=DEAL;
                if(line.contains("Flop")) lastRound=FLOP;
                if(line.contains("Turn")) lastRound=TURN;
                if(line.contains("River")) lastRound=RIVER;
                if(line.contains("Showdown")) lastRound=END_OF_HAND;
                if(line.contains("folded")) {
                    foldedPlayer=st.nextToken();
                    foldedInRound=lastRound;
                }
                if(line.contains("had")) {
                    String player=st.nextToken();
                    st.nextToken(); // burn "had"
                    Card Card1=new Card(st.nextToken());
                    Card Card2=new Card(st.nextToken());
                    int round=0;
                    if (lastRound==END_OF_HAND) round=END_OF_HAND;
                    else
                        if (foldedPlayer.equals(player)) round=foldedInRound;
                        else round=END_OF_HAND;
                    int key = HoleCardKeys.getIntKeyFromCards(Card1,Card2);
                    //System.out.println(key+":"+round);
                    if (results.containsKey(key)) {
                        Integer[] arr=results.get(key);
                        for (int i=0;i<round;i++) arr[i]++;
                        results.put(key,arr);
                    } else {
                        Integer[] arr={0,0,0,0,0};
                        for (int i=0;i<round;i++) arr[i]++;
                        results.put(key,arr);
                    }
                }
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        return results;
    }
    
    private float getResults(final int face1, final int face2, final boolean suited,int round) {
        return getResults(HoleCardKeys.getIntKeyFromInts(face1,face2,suited),round);
    }
    
    private float getResults(int key,int round) {
        Integer[] result=results.get(key);
        return (float)result[round]/(float)result[0];
    }
    
    public void readResultsFromXML() throws FileNotFoundException {
        System.out.println("loading hole card data from:"+xmlFileName2player);
        XMLDecoder d = new XMLDecoder(
                new BufferedInputStream(
                new FileInputStream(xmlFileName2player)));
        results= (HashMap) d.readObject();
        d.close();
    }
    
    private void writeToXML(HashMap results,String xmlFileName) throws Exception {
        XMLEncoder e = new XMLEncoder(
                new BufferedOutputStream(
                new FileOutputStream(xmlFileName)));
        e.writeObject(results);
        e.close();
    }
    
}