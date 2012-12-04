package Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer;

import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Utilities.HoleCardKeys;

public class HoleCardsAnalyzer extends HoleCardsDAO {
    
    public static void main(String[] args) throws Exception {
        HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
        //hca.convertCardLogsToXML(xmlFileName2player);
        hca.init();
        hca.testGetResults();
    }
    
    private void testGetResults() throws Exception {
        readResultsFromXML();
        System.out.println("result:");
        int c=1;
        for (int firstCard=0;firstCard<13;firstCard++) {
            for (int secondCard=firstCard;secondCard<13;secondCard++) {
                float winProb=(float) getResults(firstCard,secondCard,false);
                System.out.println(c+++":["+ face.faceShortNames[firstCard] + " " + face.faceShortNames[secondCard]+"]u="+winProb);
            }
        }
        for (int firstCard=0;firstCard<13;firstCard++) {
            for (int secondCard=firstCard+1;secondCard<13;secondCard++) {
                //float results =(float) getResults(firstCard,secondCard,true);
                float winProb=(float) getResults(firstCard,secondCard,true);
                System.out.println(c+++":["+ face.faceShortNames[firstCard] + " " + face.faceShortNames[secondCard]+"]s="+winProb);
            }
        }
    }
    
    public float getWinProbabilty(Cards holeCards,int numPlayers) {
        return getWinProbabilty(holeCards.get(0),holeCards.get(1),numPlayers);
    }
    
    public float getWinProbabilty(Card card1, Card card2,int numPlayers) {
        float i=getResults(HoleCardKeys.getIntKeyFromCards(card1,card2));
        return i;
    }
    
    public float getWinProbabilty(Cards holeCards) {
        return getWinProbabilty(holeCards.get(0),holeCards.get(1));
    }
    
    public float getWinProbabilty(Card card1, Card card2) {
        float i=getResults(HoleCardKeys.getIntKeyFromCards(card1,card2));
        return i;
    }
    
    private float getResults(final int face1, final int face2, final boolean suited) {
        return getResults(HoleCardKeys.getIntKeyFromInts(face1,face2,suited));
    }
    
    private float getResults(int key) {
        return (Float) resultsXML2player.get(key);
    }
    
    private float getResults(int key,int numPlayers) {
        float r=0;
        if (numPlayers==2) r=(Float) resultsXML2player.get(key);
        // if (numPlayers==3) r=(float) resultsXML3player.get(key);
        return r;
    }
    
}