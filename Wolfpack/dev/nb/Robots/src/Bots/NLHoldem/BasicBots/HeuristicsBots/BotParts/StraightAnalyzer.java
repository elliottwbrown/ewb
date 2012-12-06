package Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts;

import java.util.ArrayList;
import java.util.Collections;
import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;


public class StraightAnalyzer implements GameValues {
    
    public static int roundNo=0,cardsToCome;
    public static Cards myCards;
    public static ArrayList<Integer> faces=new ArrayList();
    
    public static void main(String[] args) throws Exception {
        test();
    }
    
    private static void test() throws Exception {
        System.out.println("start test");
        Cards communityCards=new Cards();
        communityCards.add(new Card("2H"));
        communityCards.add(new Card("6D"));
        communityCards.add(new Card("5D"));
        //communityCards.add(new Card("5S"));
        //communityCards.add(new Card("6S"));
        
        Cards holeCards=new Cards();
        holeCards.add(new Card("TD"));
        holeCards.add(new Card("JH"));
        
        float[] results=getStraightProbabilities(holeCards,communityCards,10);
        System.out.println("holeCards="+holeCards.print());
        System.out.println("communityCards="+communityCards.print());
        System.out.println("cardsToCome="+cardsToCome);
        System.out.println("pMGS="+results[0]);
        System.out.println("pAOGS="+results[1]);
        System.out.println("finish test");
    }
    
    public static void init(Cards holeCards,Cards communityCards) throws Exception {
        cardsToCome=5-communityCards.size();
        myCards=new Cards();
        myCards.add(communityCards);
        myCards.add(holeCards);
        if (communityCards.size()==0) roundNo=DEAL;
        if (communityCards.size()==3) roundNo=FLOP;
        if (communityCards.size()==4) roundNo=TURN;
        if (communityCards.size()==5) roundNo=RIVER;
        faces = getSortedFaces(myCards);
    }
    
    public static float[] getStraightProbabilities(Cards holeCards,Cards communityCards,int numPlayers) throws Exception {
        
        init(holeCards,communityCards);
        
        // get p of me hitting a flush
        float pMGS=getPStraightForMe(cardsToCome);
        
        // get p of any single opponent hitting a flush
        float pSOGS=0;//getPStraightFor1Opponent(communityCards,holeCards);
        
        // get p of any others hitting a flush
        float pAOGS=(float) (1-Math.pow((double) (1-pSOGS) ,numPlayers-1));
        
        return new float[] {pMGS,pAOGS};
    }
    
    public static float getPStraightForMe(int cardsToCome) throws Exception {
        float p=0;
        if (containsStraight(faces)) p=1;
        else
            if (roundNo==FLOP) {
                p=(float) countSingleOuts()/47;
                if (p==0) p=probOfDoubleOuts();
            } else if (roundNo==TURN) p=(float) countSingleOuts()/46;
        return p;
    }
    
    public static float getPStraightFor1Opponent(Cards communityCards,Cards holeCards) throws Exception {
        float p=0f;
        return p;
    }
    
    public static boolean containsStraight(ArrayList<Integer> faces) throws Exception {
        boolean answer=false;
        for (int i=0; i<13; i++)
            if (faces.contains(i) && faces.contains(i+1) && faces.contains(i+2)
            && faces.contains(i+3) && faces.contains(i+4)) answer=true;
        if (faces.contains(0)&&faces.contains(1)&&faces.contains(2)&&faces.contains(3)&&faces.contains(12)) answer=true;
        return answer;
    }
    
    public static boolean containsOpenEndedStraightDraw(ArrayList<Integer> faces) throws Exception {
        boolean answer=false;
        int cnt = countConsecutiveCards(faces);
        if (cnt>=4) answer=true;
        return answer;
    }
    
    private static int countConsecutiveCards(ArrayList<Integer> faces) throws Exception {
        int cnt=1,maxCnt=0;
        for (int i=1; i<faces.size(); i++) {
            if (faces.get(i)-faces.get(i-1)<=1) {
                cnt++;
                if (cnt>maxCnt) maxCnt=cnt;
            } else cnt=1;
        }
        return maxCnt;
    }
    
    private static ArrayList<Integer> getSortedFaces(final Cards myCards) throws Exception {
        ArrayList<Integer> faces= new ArrayList();
        for (int i=0; i<myCards.size(); i++) faces.add(myCards.get(i).Face);
        Collections.sort(faces);
        return faces;
    }
    
    private static int countSingleOuts() throws Exception {
        int outs=0;
        for (int i=0; i<13; i++) {
            ArrayList<Integer> possibleFaces = getSortedFaces(myCards);
            if (!possibleFaces.contains(i)) {
                possibleFaces.add(i);
                Collections.sort(possibleFaces);
                if (containsStraight(possibleFaces)) {
                    //System.out.println("hit");
                    outs+=4;
                }
            }
        }
        return outs;
    }
    
    private static float probOfDoubleOuts() throws Exception {
        float p=0;
        for (int i=0; i<13; i++) {
            for (int i2=i; i2<13; i2++) {
                ArrayList<Integer> possibleFaces = getSortedFaces(myCards);
                if (!possibleFaces.contains(i) && !possibleFaces.contains(i2)) {
                    possibleFaces.add(i);
                    possibleFaces.add(i2);
                    Collections.sort(possibleFaces);
                    if (containsStraight(possibleFaces)) p+=(8f/47f)*(4f/46f);
                }
            }
        }
        return p;
    }
    
}

