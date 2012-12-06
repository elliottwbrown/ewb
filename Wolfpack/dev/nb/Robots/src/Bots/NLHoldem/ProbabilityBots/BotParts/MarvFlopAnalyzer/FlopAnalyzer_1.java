package Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.suit;
import java.sql.ResultSet;

public class FlopAnalyzer_1 extends FlopTableDAO {
    
    public static void main(String[] args) throws Exception {
        System.out.println(">>> start test");
        createDBfromFile();
        //testGetResults();
        System.out.println(">>> finish test");
    }
    
    private static void testGetResults() throws Exception {
        init();
        Cards flop=new Cards(),holeCards=new Cards();
        holeCards.add(new Card("2h"));
        holeCards.add(new Card("5d"));
        flop.add(new Card("Kd"));
        flop.add(new Card("Qc"));
        flop.add(new Card("Ks"));
        float[] results=getWinProbabilities(holeCards,flop);
        System.out.println("holeCards="+holeCards.print());
        System.out.println("flop="+flop.print());
        System.out.println("results:"+results[0]+","+results[1]+","+results[2]+","+results[3]);
    }
    
    public static float getWinProbabilities(Cards holeCards,Cards flop,int numPlayers) throws Exception {
        float[] results=getWinProbabilities(holeCards,flop);
        float result=0;
        if (numPlayers==2) result=results[0];
        if (numPlayers==3) result=(results[0]+results[1])/2;
//        System.out.println("===============================");
//        System.out.println("holeCards= "+holeCards.print());
//        System.out.println("flop=      "+flop.print());
//        System.out.println("numPlayers="+numPlayers);
//        System.out.println("result=    "+result);
        return result;
    }
    
    public static float[] getWinProbabilities(Cards holeCards,Cards flop) throws Exception {
        int[] foundSuits=new int[] {-1,-1,-1,-1};
        int[] transpositionMatrix= new int[] {2,1,0,3};
        if (flop.size()!=3) throw new Exception("not 3 cards exception");
        if (holeCards.get(0).Face>holeCards.get(1).Face) {
            Cards temp=new Cards();
            temp.add(holeCards.get(1));
            temp.add(holeCards.get(0));
            holeCards=temp;
        }
        int cnt=0;
        foundSuits[cnt++]=holeCards.get(0).Suit;
        if (holeCards.get(1).Suit!=foundSuits[0]) foundSuits[cnt++]=holeCards.get(1).Suit;
        holeCards.get(0).Suit=suit.HEARTS;
        if (holeCards.get(1).Suit==foundSuits[0]) holeCards.get(1).Suit=suit.HEARTS;
        else holeCards.get(1).Suit=suit.DIAMONDS;
        for (int i=0;i<3;i++) {
            if (flop.get(i).Suit==foundSuits[0]) flop.get(i).Suit=suit.HEARTS;
            else if (flop.get(i).Suit==foundSuits[1]) flop.get(i).Suit=suit.DIAMONDS;
            else if (flop.get(i).Suit==foundSuits[2]) flop.get(i).Suit=suit.CLUBS;
            else if (flop.get(i).Suit==foundSuits[3]) flop.get(i).Suit=suit.SPADES;
            else {
                foundSuits[cnt++]=flop.get(i).Suit;
                if (flop.get(i).Suit==foundSuits[1]) flop.get(i).Suit=suit.DIAMONDS;
                if (flop.get(i).Suit==foundSuits[2]) flop.get(i).Suit=suit.CLUBS;
                if (flop.get(i).Suit==foundSuits[3]) flop.get(i).Suit=suit.SPADES;
            }
        }
        if (transpositionMatrix[flop.get(0).Suit]>transpositionMatrix[flop.get(1).Suit]) flop = swap0and1(flop, transpositionMatrix);
        if (transpositionMatrix[flop.get(1).Suit]>transpositionMatrix[flop.get(2).Suit]) flop = swap1and2(flop, transpositionMatrix);
        if (transpositionMatrix[flop.get(0).Suit]>transpositionMatrix[flop.get(1).Suit]) flop = swap0and1(flop, transpositionMatrix);
        if (flop.get(0).Face>flop.get(1).Face &&
                flop.get(0).Suit==flop.get(1).Suit) flop = swap0and1(flop, transpositionMatrix);
        if (flop.get(1).Face>flop.get(2).Face &&
                flop.get(1).Suit==flop.get(2).Suit) flop = swap1and2(flop, transpositionMatrix);
        if (flop.get(0).Face>flop.get(1).Face &&
                flop.get(0).Suit==flop.get(1).Suit) flop = swap0and1(flop, transpositionMatrix);
        String  holeCard1=holeCards.get(0).getCardShortestName(),
                holeCard2=holeCards.get(1).getCardShortestName(),
                flopCard1=flop.get(0).getCardShortestName(),
                flopCard2=flop.get(1).getCardShortestName(),
                flopCard3=flop.get(2).getCardShortestName();
        
        return getWinProbabilities(holeCard1,holeCard2,flopCard1,flopCard2,flopCard3);
    }
    
    public static float[] getWinProbabilities(String holeCard1,String holeCard2,String flopCard1,String flopCard2,String flopCard3) throws Exception {
        float[] results={-1,-1,-1,-1};
        try {
            String cardIndex=holeCard1+holeCard2+flopCard1+flopCard2+flopCard3;
            //ResultSet rs=db.runQuery("select * from flop_probabilities where card_index='"+cardIndex+"'");
            db.ps.setString(1,cardIndex);
            ResultSet rs=db.ps.executeQuery();
            rs.next();
            results[0] =rs.getFloat("prob1");
            results[1] =rs.getFloat("prob3");
            results[2]= rs.getFloat("prob5");
            results[3] =rs.getFloat("prob9");
            rs.close();
            rs=null;
        } catch (java.sql.SQLException e) {
            System.out.println("Exception trapped.");
            e.printStackTrace();
            System.out.println(
                    "holecard1='"+holeCard1+"' and " +
                    "holecard2='"+holeCard2+"' and " +
                    "flopcard1='"+flopCard1+"' and " +
                    "flopcard2='"+flopCard2+"' and " +
                    "flopcard3='"+flopCard3+"'");
            init();
        }
        
        return results;
    }
    
    private static Cards swap0and1(Cards flop, final int[] transpositionMatrix) {
        Cards temp=new Cards();
        temp.add(flop.get(1));
        temp.add(flop.get(0));
        temp.add(flop.get(2));
        flop=temp;
        return flop;
    }
    
    private static Cards swap1and2(Cards flop, final int[] transpositionMatrix) {
        Cards temp=new Cards();
        temp.add(flop.get(0));
        temp.add(flop.get(2));
        temp.add(flop.get(1));
        flop=temp;
        return flop;
    }
    
}
