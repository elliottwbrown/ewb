package Bots.NLHoldem.ProbabilityBots.BotParts.TurnAnalyzer;

import Bots.NLHoldem.ProbabilityBots.BotParts.HeldCardsAnalyzer.HeldCardAnalyzerDAO;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.Deck;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;
import ca.ualberta.cs.poker.Hand;
import ca.ualberta.cs.poker.HandEvaluator;

public class TurnAnalyzer implements GameValues {
    
    public static HandEvaluator handEval =null;
    
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
        HeldCardAnalyzerDAO heca=new HeldCardAnalyzerDAO();
        heca.init();
        
        Cards community=new Cards(),holeCards=new Cards();
        holeCards.add(new Card("Qh"));
        holeCards.add(new Card("7d"));
        community.add(new Card("Ks"));
        community.add(new Card("Jc"));
        community.add(new Card("2s"));
        community.add(new Card("7c"));
        System.out.println("holeCards="+holeCards.print());
        System.out.println("flop="+community.print());
        System.out.println("result:"+getWinProbabilities(holeCards,community));
        System.out.println("result:"+getWinProbabilities(holeCards,community,heca));
    }
    
    public static float getWinProbabilities(Cards holeCards,Cards community,int numPlayers) throws Exception {
        float pForOne=getWinProbabilities(holeCards,community);
//        System.out.println("holeCards="+holeCards.print());
//        System.out.println("community="+community.print());
//        System.out.println("numPlayers="+numPlayers);
//        System.out.println("pForOne="+pForOne);
        //float pForN=(float) (1-Math.pow((double) (1-pForOne),numPlayers-1));
        return pForOne;
    }
    
    public static float getWinProbabilities(Cards holeCards,Cards community) throws Exception {
        float p=-1;
        int winner=-1,wins=0,r;
        Hand myHand;
        Card card1,card2,card3;
        Cards opponentsHoleCards;
        Hand opponentsHand=null;
        int sampleSize=10000;
        String myCards=holeCards.print()+" "+community.print();
        Deck deck=new Deck();
        deck.remove(holeCards);
        deck.remove(community);
        for (int c=0;c<sampleSize;c++) {
            Deck deck2=new Deck(deck);
            card1=deck2.remove((int) (Math.random()*46));
            card2=deck2.remove((int) (Math.random()*45));
            card3=deck2.remove((int) (Math.random()*44));
            myHand=new Hand(myCards+" "+card3.print());
            opponentsHand=new Hand(card1.print()+" "+card2.print()+" "+community.print()+" "+card3.print());
            winner=handEval.compareHands(myHand,opponentsHand);
//            System.out.println("============================================");
//            System.out.println("  holeCards=    "+holeCards.print());
//            System.out.println("  opponentsHand="+card1.print()+" "+card2.print());
//            System.out.println("  board=        "+community.print()+" "+card3.print());
//            System.out.println("  myHand=       "+myHand);
//            System.out.println("  opponentsHand="+opponentsHand);
//            System.out.println("  winner=       "+winner);
            if (winner==1) wins++;
            if (winner==0) wins+=.5;
        }
        p=(float) wins/ (float) sampleSize;
        return p;
    }
    
    public static float getWinProbabilities2(Cards holeCards,Cards community,HoleCardsAnalyzer hca) throws Exception {
        float p=-1;
        int winner=-1,wins=0,r;
        Hand myHand;
        Card card1,card2,card3;
        Cards opponentsHoleCards=null;
        Hand opponentsHand=null;
        int sampleSize=10000;
        String myCards=holeCards.print()+" "+community.print();
        Deck deck=new Deck();
        deck.remove(holeCards);
        deck.remove(community);
        float winP=0;
        for (int c=0;c<sampleSize;c++) {
            Deck deck2=new Deck(deck);
            card1=deck2.remove((int) (Math.random()*46));
            card2=deck2.remove((int) (Math.random()*45));
            card3=deck2.remove((int) (Math.random()*44));
            opponentsHoleCards = new Cards();
            opponentsHoleCards.add(card1);
            opponentsHoleCards.add(card2);
            myHand=new Hand(myCards+" "+card3.print());
            opponentsHand=new Hand(opponentsHoleCards+" "+community.print()+" "+card3.print());
            winner=handEval.compareHands(myHand,opponentsHand);
//            System.out.println("============================================");
//            System.out.println("  holeCards=    "+holeCards.print());
//            System.out.println("  opponentsHand="+card1.print()+" "+card2.print());
//            System.out.println("  board=        "+community.print()+" "+card3.print());
//            System.out.println("  myHand=       "+myHand);
//            System.out.println("  opponentsHand="+opponentsHand);
//            System.out.println("  winner=       "+winner);
            if (winner==1) winP+=hca.getWinProbabilty(opponentsHoleCards);
            if (winner==0) winP+=hca.getWinProbabilty(opponentsHoleCards)/2;
        }
        //p=(float) wins/ (float) sampleSize;
        p= winP/(float) sampleSize;
        return p;
    }
    
    public static float getWinProbabilities(Cards holeCards,Cards community,HeldCardAnalyzerDAO heca) throws Exception {
        float p=-1;
        int winner=-1,wins=0,r;
        Hand myHand;
        Card card1,card2,card3;
        Cards opponentsHoleCards=null;
        Hand opponentsHand=null;
        int sampleSize=10000;
        String myCards=holeCards.print()+community.print();
        Deck deck=new Deck();
        deck.remove(holeCards);
        deck.remove(community);
        float winP=0;
        float riverProbTotal=0;
        for (int c=0;c<sampleSize;c++) {
            Deck deck2=new Deck(deck);
            card1=deck2.remove((int) (Math.random()*46));
            card2=deck2.remove((int) (Math.random()*45));
            card3=deck2.remove((int) (Math.random()*44));
            opponentsHoleCards = new Cards();
            opponentsHoleCards.add(card1);
            opponentsHoleCards.add(card2);
            myHand=new Hand(myCards+card3.print());
            opponentsHand=new Hand(opponentsHoleCards.print()+community.print()+card3.print());
            winner=handEval.compareHands(myHand,opponentsHand);
            float holdProbability=heca.getHoldProbability(opponentsHoleCards,TURN);
            riverProbTotal+=holdProbability;
            if (winner==1) winP+=holdProbability;
            if (winner==0) winP+=holdProbability/2;
            //System.out.print(opponentsHand.toString()+":"+holdProbability+":"+winner+"\n");
        }
        p= winP/riverProbTotal;
        return p;
    }
    
}
