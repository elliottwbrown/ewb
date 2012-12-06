package Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;

public class HoleCardsAnalyzer2 extends HoleCardsFileDAO {

    public HoleCardsAnalyzer2() {
        super();
    }

//    public static void main(String[] args) throws Exception {
//        HoleCardsAnalyzer2 hca=new HoleCardsAnalyzer2();
//        //hca.convertCardLogsToXML();
//        hca.init();
//        hca.testGetResults();
//    }
//    private void testGetResults() throws Exception {
//        //readResultsFromXML();        
//        System.out.println("result:");
//        int c=1;
//        for (int firstCard=0;firstCard<13;firstCard++)
//            for (int secondCard=firstCard;secondCard<13;secondCard++)
//                System.out.println(c+++":["+ face.faceShortNames[firstCard] + " " + face.faceShortNames[secondCard]+"]u="+getResults(firstCard,secondCard,false));
//        for (int firstCard=0;firstCard<13;firstCard++)
//            for (int secondCard=firstCard+1;secondCard<13;secondCard++) {
//            System.out.print(c+++":["+ face.faceShortNames[firstCard] + " " + face.faceShortNames[secondCard]+"]s=");
//            int key=HoleCardKeys.getIntKeyFromInts(firstCard,secondCard,true);
//            System.out.println(getResults(key));
//            }
//    }
    public float getWinProbabilty(Cards holeCards, int numPlayers) {
        return getWinProbabilty(holeCards.get(0), holeCards.get(1), numPlayers);
    }

    public float getWinProbabilty(Card card1, Card card2, int numPlayers) {
        return getResults(HoleCardKeys.getIntKeyFromCards(card1, card2));
    }

    public float getWinProbabilty(Cards holeCards) {
        return getWinProbabilty(holeCards.get(0), holeCards.get(1));
    }

    public float getWinProbabilty(Card card1, Card card2) {
        return getResults(HoleCardKeys.getIntKeyFromCards(card1, card2));
    }

    private float getResults(final int face1, final int face2, final boolean suited) {
        return getResults(HoleCardKeys.getIntKeyFromInts(face1, face2, suited));
    }

    private float getResults(int key) {
        float f = 0f;
        try {
            f = results.get(key) / 100;
        } catch (Exception e) {
            System.out.println("exception finding key");
            System.out.println("key=" + key);
            System.out.println("results=" + results);
            e.printStackTrace();
        }
        return f;
    }

    private float getResults(int key, int numPlayers) {
        return results.get(key);
    }
}