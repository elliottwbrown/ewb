package Bots.TG1.StaticBots.PositionBots;

import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class Adj2LimpBot39R76 extends Adj2RaiseBot39R76 {
    
    public float 
        limpRaiseThreshold=     0.51f;
 
    BotParts bp=new BotParts();
        
    @Override
    public int makeDecision() throws Exception {
        float oppOneBetThreshold=1f-rateHandsOppRaisedSB;
        if (!actFirst() && !beenRaisedTo) decision=adjustToLimpFrom39R76(myCardsFace,limpRaiseThreshold);   
        else if (beenRaisedTo) decision=bp.adj2Raise
                (numOppRaisedThisHand,potOdds,myCardsFace,oppOneBetThreshold, 
                twoBetThreshold, fourBetThreshold);
        else decision=bp.adj4Card
                (myCardsFace,oneBetThreshold,foldThreshold);
        return decision;
    }
  
    // anti origami39R76
    public int adjustToLimpFrom39R76(int myCard,float limpRaiseThreshold) throws Exception {  
        decision = GameValues.CALL;
        float adjustedWinProb=0f;     
        
        // a r.76 /f.39 bot will ;
        // raise with >=J (30.77%)
        // fold =<6 (38.46%)
        // and so therfore it limps with 7,8,9&T
        // this is a limp rate of 30.77%
        // so ...
        if (myCard>=face.TEN) adjustedWinProb=1f;
        if (myCard==face.NINE) adjustedWinProb=.75f;
        if (myCard>=face.EIGHT) adjustedWinProb=.5f;
        if (myCard==face.SEVEN) adjustedWinProb=.25f;
        if (adjustedWinProb>=limpRaiseThreshold) decision=GameValues.RAISE;
        return decision;
    }    
}