package Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;

public class FlushAnalyzer implements GameValues {
    
    public static int roundNo=0,cardsToCome;
    public static Cards myCards;
    
    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws Exception {
        System.out.println("start test");
        Cards community=new Cards();
        community.add(new Card("7D"));
        community.add(new Card("5D"));
        community.add(new Card("TD"));
        //community.add(new Card("8S"));
        //community.add(new Card("JS"));
        
        Cards holeCards=new Cards();
        holeCards.add(new Card("JD"));
        holeCards.add(new Card("KH"));
        
        float[] results=getFlushProbabilities(holeCards,community,3);
        
        System.out.println("holeCards="+holeCards.print());
        System.out.println("communityCards="+community.print());
        System.out.println("cardsToCome="+cardsToCome);
        System.out.println("pMGF="+results[0]);
        System.out.println("pAOGF="+results[1]);
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
    }
    
    public static float getPFlushForMe(Cards myCards, int cardsToCome) throws Exception {
        float p=0;
        int numSuits=BasicAnalyzer.countSuits(myCards);
        int suitsNeeded=Math.max(5-numSuits,0);
        int numSuitsRemaining=13-numSuits, numCardsRemaining=52-2-(5-cardsToCome);
        float pOf1stSuit=(float) numSuitsRemaining / (float) numCardsRemaining;
        float pOf2ndSuit=((float) numSuitsRemaining-1)/((float) numCardsRemaining-1);
        float pOfNoSuit=1-pOf1stSuit;
        if (suitsNeeded>cardsToCome) p=0;
        else if (suitsNeeded==0) p=1;
        else if (suitsNeeded==1) {
            if (cardsToCome==1) p=pOf1stSuit;
            else if (cardsToCome==2) p=1-pOfNoSuit*pOfNoSuit;
        } else if (suitsNeeded==2) {
            if (cardsToCome==1) p=0;
            else if (cardsToCome==2) p=pOf1stSuit*pOf2ndSuit;
        } else p=0;
        return p;
    }
    
    public static float getPFlushForSingleOpponent(Cards communityCards,Cards holeCards) throws Exception {
        float p=0f;
        for (int suit=0; suit<4 ; suit++) {
            float pFlushInThisSuit=0;
            int suitsInCommunity=BasicAnalyzer.countSuits(communityCards,suit);
            int suitsInHand=BasicAnalyzer.countSuits(holeCards,suit);
            int numSuitsRemaining=13-suitsInCommunity-suitsInHand;
            int numCardsRemaining=52-(5-cardsToCome)-2;
            float pOf1stSuit=(float) numSuitsRemaining/ (float) numCardsRemaining;
            float pOf2ndSuit=((float) numSuitsRemaining-1)/((float) numCardsRemaining-1);
            float pOf3rdSuit=((float) numSuitsRemaining-2)/((float) numCardsRemaining-3);
            float pOf4thSuit=((float) numSuitsRemaining-3)/((float) numCardsRemaining-3);
            float pOfNoSuit=1-pOf1stSuit;
            if (cardsToCome==0) {
                if (suitsInCommunity<=2) pFlushInThisSuit=0;
                if (suitsInCommunity==3) pFlushInThisSuit=pOf1stSuit*pOf2ndSuit;
                if (suitsInCommunity==4) pFlushInThisSuit=1-pOfNoSuit*pOfNoSuit;
                if (suitsInCommunity==5) pFlushInThisSuit=1;
            } else if (cardsToCome==1) {
                if (suitsInCommunity==1) pFlushInThisSuit=0;
                if (suitsInCommunity==2) pFlushInThisSuit=pOf1stSuit*pOf2ndSuit*pOf3rdSuit;
                if (suitsInCommunity==3) pFlushInThisSuit=3*(pOfNoSuit*pOf1stSuit*pOf2ndSuit)+(pOf1stSuit*pOf2ndSuit*pOf3rdSuit);
                if (suitsInCommunity==4) pFlushInThisSuit=1-pOfNoSuit*pOfNoSuit*pOfNoSuit;
            } else if (cardsToCome==2) {
                if (suitsInCommunity==1) pFlushInThisSuit=pOf1stSuit*pOf2ndSuit*pOf3rdSuit*pOf4thSuit;
                if (suitsInCommunity==2) pFlushInThisSuit=4*pOfNoSuit*pOf1stSuit*pOf2ndSuit*pOf3rdSuit+
                        pOf1stSuit*pOf2ndSuit*pOf3rdSuit*pOf4thSuit;
                if (suitsInCommunity==3)
                    p=6*pOfNoSuit*pOfNoSuit*pOf1stSuit*pOf2ndSuit+4*pOfNoSuit*pOf1stSuit*pOf2ndSuit*pOf3rdSuit+
                            pOf1stSuit*pOf2ndSuit*pOf3rdSuit*pOf4thSuit;
            }
            p+=pFlushInThisSuit;
        }
        return p;
    }
    
    public static float[] getFlushProbabilities(Cards holeCards,Cards communityCards,int numPlayers) throws Exception {
        
        init(holeCards,communityCards);
        
        // get p of me hitting a flush
        float pMGF=getPFlushForMe(myCards,cardsToCome);
        
        // get p of any single opponent hitting a flush
        float pSOGF=getPFlushForSingleOpponent(communityCards,holeCards);
        
        // get p of any others hitting a flush
        float pAOGF=(float) (1-Math.pow((double) (1-pSOGF) ,numPlayers-1));
        
        return new float[] {pMGF,pAOGF};
    }
    
}
