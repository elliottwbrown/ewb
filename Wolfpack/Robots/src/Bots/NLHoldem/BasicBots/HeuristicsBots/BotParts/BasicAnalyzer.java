package Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.face;

public class BasicAnalyzer {
    
    /**
     * testing
     *
     */
    public static void main(String args[]) throws Exception {
        System.out.println("start test");
        Cards community=new Cards();
        community.add(new Card("5S"));
        community.add(new Card("5C"));
        community.add(new Card("5D"));
        community.add(new Card("JS"));
        community.add(new Card("TS"));
        
        Cards holeCards=new Cards();
        holeCards.add(new Card("JS"));
        holeCards.add(new Card("KS"));
        
        System.out.println("cards: "+holeCards.print());
        System.out.println("cards: "+community.print());
        System.out.println("containsPair: "+containsPair(holeCards,community));
        System.out.println("containsFlush: "+containsFlush(holeCards,community));
        System.out.println("containsStrait: "+containsStrait(holeCards,community));
        System.out.println("containsStrait: "+containsStrait(holeCards,community));
        System.out.println("containsAceAndHigh: "+containsAceAndHigh(holeCards));
        System.out.println("containsKingAndHigh: "+containsKingAndHigh(holeCards));
        System.out.println("containsTrip: "+containsTrip(holeCards,community));
        
        System.out.println("finish test");
    }
    
    /**
     * single card methods
     *
     */
    public static boolean containsAce(Cards cards) throws Exception {
        return containsFace(cards,face.ACE);
    }
    
    public static boolean containsKing(Cards cards) throws Exception {
        return containsFace(cards,face.KING);
    }
    
    public static boolean containsFace(Cards cards,int FACE) throws Exception {
        boolean contains=false;
        for (int i=0 ; i<cards.size() ; i++) if (cards.get(i).Face==FACE) contains=true;
        return contains;
    }
    
    /**
     * hole card methods
     *
     */
    public static boolean holeCards(Cards holeCards) throws Exception {
        boolean containsPair=false;
        int faceCounts[]=new int[14];
        for (int i=0 ; i<holeCards.size() ; i++) faceCounts[holeCards.get(i).Face]++;
        for (int i=0; i<14 ; i++) if (faceCounts[i]>1);
        return containsPair;
    }
    
    public static boolean containsAceAndHigh(Cards cards) throws Exception {
        boolean contains=false;
        for (int i=0 ; i<cards.size() ; i++)
            if (cards.get(i).Face==face.ACE)
                for (int j=0 ; j<cards.size() ; j++)
                    if (cards.get(j).Face>face.TEN && j!=i) contains=true;
        return contains;
    }
    
    public static boolean containsKingAndHigh(Cards cards) throws Exception {
        boolean contains=false;
        for (int i=0 ; i<cards.size() ; i++)
            if (cards.get(i).Face==face.KING)
                for (int j=0 ; j<cards.size() ; j++)
                    if (cards.get(j).Face>face.TEN && j!=i) contains=true;
        return contains;
    }
    
    /**
     * more cards methods
     *
     */
    public static boolean containsPair(Cards cards) throws Exception {
        boolean contains=false;
        int faceCounts[]=new int[14];
        for (int i=0 ; i<cards.size() ; i++) faceCounts[cards.get(i).Face]++;
        for (int i=0; i<14 ; i++) if (faceCounts[i]>1) contains=true;
        return contains;
    }
    
    public static boolean containsPair(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        return containsPair(cards);
    }
    
    public static boolean containsPrivatePair(Cards holeCards,Cards communityCards) throws Exception {
        boolean contains=false;
        for (int i=0 ; i<communityCards.size() ; i++) if (communityCards.get(i).Face==holeCards.get(0).Face) contains=true;
        for (int i=0 ; i<communityCards.size() ; i++) if (communityCards.get(i).Face==holeCards.get(1).Face) contains=true;
        return contains;
    }
    
    public static boolean containsTrip(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        boolean contains=false;
        int faceCounts[]=new int[14];
        for (int i=0 ; i<cards.size() ; i++) faceCounts[cards.get(i).Face]++;
        for (int i=0; i<14 ; i++) if (faceCounts[i]>2) contains=true;
        return contains;
    }
    
    public static boolean containsFlush(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        boolean containsFlush=false;
        int suitCounts[]=new int[4];
        for (int i=0 ; i<cards.size() ; i++) suitCounts[cards.get(i).Suit]++;
        for (int i=0; i<4 ; i++) if (suitCounts[i]>=5) containsFlush=true;
        return containsFlush;
    }
    
    public static boolean containsFlushDraw(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        boolean contains=false;
        int suitCounts[]=new int[4];
        for (int i=0 ; i<cards.size() ; i++) suitCounts[cards.get(i).Suit]++;
        for (int i=0; i<4 ; i++) if (suitCounts[i]>=4) contains=true;
        return contains;
    }
    
    public static boolean containsBackdoorFlushDraw(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        boolean contains=false;
        int suitCounts[]=new int[4];
        for (int i=0 ; i<cards.size() ; i++) suitCounts[cards.get(i).Suit]++;
        for (int i=0; i<4 ; i++) if (suitCounts[i]>=3) contains=true;
        return contains;
    }
    
    public static int countSuits(Cards cards,int suit) throws Exception {
        boolean contains=false;
        int suitCount=0;
        for (int i=0 ; i<cards.size() ; i++) if (cards.get(i).Suit==suit) suitCount++;
        return suitCount;
    }
    
    public static int countSuits(Cards cards) throws Exception {
        boolean contains=false;
        int suitCounts[]=new int[4];
        for (int i=0 ; i<cards.size() ; i++) suitCounts[cards.get(i).Suit]++;
        int highestSuitCount=0;
        for (int i=0; i<4 ; i++) if (suitCounts[i]>highestSuitCount) highestSuitCount=suitCounts[i];
        return highestSuitCount;
    }
    
    public static int countSuits(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        return countSuits(cards);
    }
    
    public static boolean containsOutsideStraitDraw(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        boolean contains=false;
        int faceCounts[]=new int[14];
        for (int i=0 ; i<cards.size() ; i++) faceCounts[cards.get(i).Suit]++;
        for (int i=1; i<11 ; i++)
            if ((faceCounts[i]>0)&&(faceCounts[i+1]>0)&&(faceCounts[i+2]>0)&&(faceCounts[i+3]>0))
                contains=true;
        if (!contains && containsAce(cards))
            if (containsFace(cards,face.TWO) &&
                containsFace(cards,face.THREE) &&
                containsFace(cards,face.FOUR) &&
                containsFace(cards,face.FIVE)) contains=true;
        return contains;
    }
    
    public static boolean containsStrait(Cards holeCards,Cards communityCards) throws Exception {
        Cards cards=new Cards();
        cards.add(holeCards);
        cards.add(communityCards);
        boolean containsStrait=false;
        int faceCounts[]=new int[14];
        for (int i=0 ; i<cards.size() ; i++) faceCounts[cards.get(i).Suit]++;
        for (int i=1; i<10 ; i++)
            if ((faceCounts[i]>0)&&(faceCounts[i+1]>0)&&(faceCounts[i+2]>0)&&(faceCounts[i+3]>0)&&(faceCounts[i+4]>0))
                containsStrait=true;
        if (!containsStrait && containsAce(cards))
            if (containsFace(cards,face.TWO) &&
                containsFace(cards,face.THREE) &&
                containsFace(cards,face.FOUR) &&
                containsFace(cards,face.FIVE)) containsStrait=true;
        
        return containsStrait;
    }
    
    
}

