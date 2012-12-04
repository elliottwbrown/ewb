package Bots.NLHoldem.ProbabilityBots.BotParts.HeldCardsAnalyzer;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;
import PokerCommons.PokerObjects.Utilities.HoleCardKeys;

public class HeldCardAnalyzerDAO implements GameValues {
    
    public static final String
            dataFolder="C:/data/dev/svn_wolfpack/dev/WolfPack/nb/Robots/ResultsData/",
            xmlFileName2player=dataFolder+"2PlayerHoldingResults.xml",
            dataFileName=dataFolder+"Games.log";
    public HashMap<Integer,Integer[]>
            results;
    public static float riverProbTotal=0;
    
    public float getHoldProbability(Cards holeCards,int round) {
        return getHoldProbability(holeCards.get(0),holeCards.get(1),round);
    }
    
    public float getHoldProbability(Card card1, Card card2,int round) {
        return getHoldProbability(HoleCardKeys.getIntKeyFromCards(card1,card2),round);
    }
    private float getHoldProbability(final int face1, final int face2, final boolean suited,int round) {
        return getHoldProbability(HoleCardKeys.getIntKeyFromInts(face1,face2,suited),round);
    }
    
    private float getHoldProbability(int key,int round) {
        Integer[] result=results.get(key);
        return (float)result[round]/(float)result[0];
    }
    
    public void init() throws FileNotFoundException {
        System.out.println("  loading held card data from:"+xmlFileName2player);
        XMLDecoder d = new XMLDecoder(
                new BufferedInputStream(
                new FileInputStream(xmlFileName2player)));
        results= (HashMap) d.readObject();
        d.close();
    }
    
}