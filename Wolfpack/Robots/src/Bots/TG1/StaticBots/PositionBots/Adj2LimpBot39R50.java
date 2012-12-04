package Bots.TG1.StaticBots.PositionBots;

import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class Adj2LimpBot39R50 extends Adj2RaiseBot39R50 {
    
    public float 
        limpRaiseThreshold=     0.51f;

    BotParts bp=new BotParts();

    @Override
    public int makeDecision() throws Exception {
        oppOneBetThreshold=1f-rateHandsOppRaisedSB;
        if (!actFirst() && !beenRaisedTo) decision=adjustToLimpFrom39R50(myCardsFace,limpRaiseThreshold);   
        else if (beenRaisedTo) decision=bp.adj2Raise
                (numOppRaisedThisHand,potOdds,myCardsFace,oppOneBetThreshold, 
                twoBetThreshold, fourBetThreshold);
        else decision=bp.adj4Card
                (myCardsFace,oneBetThreshold,foldThreshold);
        return decision;
    }
    
    // anti origami39R50
    public int adjustToLimpFrom39R50(int myCard,float limpRaiseThreshold) throws Exception {  
        decision = GameValues.CALL;
        float adjustedWinProb=0f; 
        
        // a r.50 /f.39 bot will ;
        // raise with >=8 (53.8%)
        // fold =<6 (38.46%)
        // and so therfore it limps with 7
        // this is a limp rate of 7.69%
        // so ...
        if (myCard>=face.EIGHT) adjustedWinProb=1f;
        if (myCard==face.SEVEN) adjustedWinProb=.1f;
        if (myCard<=face.SIX) adjustedWinProb=.0f;
        if (adjustedWinProb>=limpRaiseThreshold) decision=GameValues.RAISE;
        
        return decision;
    }       
}