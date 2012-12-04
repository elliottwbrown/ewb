package Bots.TG1.DynamicBots.MemoryBots;

import Bots.TG1.GenericBots.GenericBot;
import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class FourPointBuck39R0 extends GenericBot {
    
    public float 
        oneBetThreshold=        2f,            
        twoBetThreshold=        0.75f,
        fourBetThreshold=       0.90f,  
        foldThreshold=          0.39f,
        limpRaiseThreshold=     0.49f;

    BotParts bp=new BotParts();    
    @Override
    public int makeDecision() throws Exception {
        decisionPointsIReached[DecisionPoint]++;
        if (DecisionPoint==0) decideFromDP0();
        else if (DecisionPoint==1) decideFromDP1();                     
        else if (DecisionPoint==4) decideFromDP4();
        else decideFromDPs23468();
        
        return decision;
    }
    
    public void decideFromDP4() throws Exception {
        decideFromDPs23468();
        int old=decision;
        if (myCardsFace>=face.KING) decision=GameValues.RAISE;
        if (myCardsFace==face.QUEEN || myCardsFace==face.JACK) decision=GameValues.CALL;
        if (myCardsFace<=face.TEN) decision=GameValues.CHECKFOLD;
//        if (old!=decision) {
//            System.out.print("decision "+decision+"\t");
//            System.out.print("old "+old+"\t");
//            System.out.print("myCard "+myCard+"\t");
//            System.out.print("numOppRaisedThisHand "+numOppRaisedThisHand+"\t");
//            System.out.println("");
//        }
    }
    
    public void decideFromDP0() throws Exception {
        decision=bp.adj4Card(myCardsFace,oneBetThreshold,foldThreshold);
    }
    
    public void decideFromDP1() throws Exception {
        decision=bp.adj2Limp
                (rateHandsOppFoldedSB,rateHandsOppRaisedSB, myCardsFace,
                limpRaiseThreshold);  
    }
    
    public void decideFromDPs23468() throws Exception {
        float oppOneBetThreshold=1f-rateHandsOppRaisedSB;
        decision=bp.adj2Raise
                (numOppRaisedThisHand,potOdds,myCardsFace,oppOneBetThreshold, 
                twoBetThreshold, fourBetThreshold);  
    }      
}