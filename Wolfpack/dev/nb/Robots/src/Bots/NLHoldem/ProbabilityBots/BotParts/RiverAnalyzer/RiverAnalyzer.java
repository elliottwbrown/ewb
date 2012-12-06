package Bots.NLHoldem.ProbabilityBots.BotParts.RiverAnalyzer;

import Bots.NLHoldem.ProbabilityBots.BotParts.HeldCardsAnalyzer.HeldCardAnalyzerDAO;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.Deck;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;
import ca.ualberta.cs.poker.Hand;
import ca.ualberta.cs.poker.HandEvaluator;

public class RiverAnalyzer implements GameValues {
    
    public static  HandEvaluator handEval =null;
    
    public static void main(String[] args) throws Exception {
        System.out.println(">>> start test");
        init();
        testGetResults();
        System.out.println(">>> finish test");
    }
    
    public static void init() {
        handEval = new HandEvaluator();
    }
    
    private static void testGetResults() throws Exception {
        //init();
        Cards community=new Cards(),holeCards=new Cards();
        holeCards.add(new Card("Qh"));
        holeCards.add(new Card("9h"));
        community.add(new Card("Ts"));
        community.add(new Card("8h"));
        community.add(new Card("4d"));
        community.add(new Card("9d"));
        community.add(new Card("Jh"));
        System.out.println("holeCards="+holeCards.print());
        System.out.println("flop="+community.print());
        HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
        hca.init();
        System.out.println("result:"+getWinProbabilitiesOld(holeCards,community));
        System.out.println("result:"+getWinProbabilities(holeCards,community,hca));
    }
    
    public static float getWinProbabilities(Cards holeCards,Cards community,int numPlayers,HoleCardsAnalyzer hca) throws Exception {
        float pForOne=getWinProbabilities(holeCards,community, hca);
        float pForN=1-(1-pForOne)*(numPlayers-1);
        return pForN;
    }
    
    public static float getWinProbabilities(Cards holeCards,Cards community,HoleCardsAnalyzer hca) throws Exception {
        float p=-1;
        Deck deck=new Deck();
        deck.remove(holeCards);
        deck.remove(community);
        int c=0;
        float winP=0;
        Hand myHand=new Hand(holeCards.print()+" "+community.print());
        Card card1,card2;
        Cards opponentsHoleCards;
        Hand opponentsHand=null;
        for (int c1=0;c1<deck.size();c1++) {
            card1=deck.get(c1);
            for (int c2=c1+1;c2<deck.size();c2++) {
                card2=deck.get(c2);
                opponentsHoleCards = new Cards();
                opponentsHoleCards.add(card1);
                opponentsHoleCards.add(card2);
                opponentsHand=new Hand(opponentsHoleCards.print()+" "+community.print());
                int winner=handEval.compareHands(myHand,opponentsHand);
                float holdProbability=hca.getWinProbabilty(opponentsHoleCards);
                if (winner==1) winP+=holdProbability;
                if (winner==0) winP+=holdProbability/2;                
            }
        }
        return winP/990f;
    }
        
    public static float getWinProbabilities(Cards holeCards,Cards community,HeldCardAnalyzerDAO hca) throws Exception {
        float p=-1;
        Deck deck=new Deck();
        deck.remove(holeCards);
        deck.remove(community);
        int c=0;
        float winP=0;
        Hand myHand=new Hand(holeCards.print()+" "+community.print());
        Card card1,card2;
        Cards opponentsHoleCards;
        Hand opponentsHand=null;
        float riverProbTotal=0;
        for (int c1=0;c1<deck.size();c1++) {
            card1=deck.get(c1);
            for (int c2=c1+1;c2<deck.size();c2++) {
                card2=deck.get(c2);
                opponentsHoleCards = new Cards();
                opponentsHoleCards.add(card1);
                opponentsHoleCards.add(card2);
                opponentsHand=new Hand(opponentsHoleCards.print()+" "+community.print());
                int winner=handEval.compareHands(myHand,opponentsHand);
                float holdProbability=hca.getHoldProbability(opponentsHoleCards,RIVER);
                riverProbTotal+=holdProbability;
                if (winner==1) winP+=holdProbability;
                if (winner==0) winP+=holdProbability/2;
            }
        }
        return winP/riverProbTotal;
    }
    
    public static float getWinProbabilitiesOld(Cards holeCards,Cards community) throws Exception {
        float p=-1;
        Deck deck=new Deck();
        deck.remove(holeCards);
        deck.remove(community);
        int c=0,wins=0;
        float winP=0;
        Hand myHand=new Hand(holeCards.print()+" "+community.print());
        Card card1,card2;
        Cards opponentsHoleCards;
        Hand opponentsHand=null;
        for (int c1=0;c1<deck.size();c1++) {
            card1=deck.get(c1);
            for (int c2=c1+1;c2<deck.size();c2++) {
                card2=deck.get(c2);
                opponentsHoleCards = new Cards();
                opponentsHoleCards.add(card1);
                opponentsHoleCards.add(card2);
                opponentsHand=new Hand(opponentsHoleCards.print()+" "+community.print());
                int winner=handEval.compareHands(myHand,opponentsHand);
                if (winner==1) wins++;
                if (winner==0) wins+=.5;
            }
        }
        p=wins/990f;
        return p;
    }
    
}
