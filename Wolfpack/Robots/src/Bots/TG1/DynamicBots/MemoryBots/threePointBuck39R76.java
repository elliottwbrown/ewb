package Bots.TG1.DynamicBots.MemoryBots;

import Bots.TG1.GenericBots.GenericBot;

public class threePointBuck39R76 extends GenericBot {
    
    public float 
        oneBetThreshold=        0.76f,            
        twoBetThreshold=        0.75f,
        fourBetThreshold=       0.90f,  
        foldThreshold=          0.39f,
        limpRaiseThreshold=     0.49f;

    BotParts bp=new BotParts();
        
    @Override
    public int makeDecision() throws Exception {
        if (!actFirst() && !beenRaisedTo) decideFromDP1(); 
        else if (beenRaisedTo) decideFromDPs2andUp();
        else decideFromDP0();
        return decision;
    }
    
    public void decideFromDP0() throws Exception {
        decision=bp.adj4Card(myCardsFace,oneBetThreshold,foldThreshold);
    }
    
    public void decideFromDPs2andUp() throws Exception {
        float oppOneBetThreshold=1f-rateHandsOppRaisedSB;
        decision=bp.adj2Raise
                (numOppRaisedThisHand,potOdds,myCardsFace,oppOneBetThreshold, 
                twoBetThreshold, fourBetThreshold);  
    }    
    
    public void decideFromDP1() throws Exception {
        decision=bp.adj2Limp
                (rateHandsOppFoldedSB,rateHandsOppRaisedSB, myCardsFace,
                limpRaiseThreshold);  
    }

}