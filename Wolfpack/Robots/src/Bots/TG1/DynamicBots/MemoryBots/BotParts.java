package Bots.TG1.DynamicBots.MemoryBots;

import PokerCommons.PokerObjects.GameProtocols.TG1.*;

public class BotParts extends Bots.TG1.StaticBots.PositionBots.BotParts {

    /**
     * This method is used when a decision is made in the face of a limp. this can
     * only happen at decision point 2 - for the BB
     * 
     * it uses the rateHandsOppFoldedSB and rateHandsOppRaisedSB to computer his 
     * range of limping cards.
     * 
     * an adjustedWwinProb is then computed by comparing our card to the range
     * and either a call or raise is returned based on a comparison of this the 
     * the threshold given for raising a limp
     */       
    public int adj2Limp(float rateHandsOppFoldedSB,float rateHandsOppRaisedSB,
            int myCard,float limpRaiseThreshold) throws Exception {  
        int decision = GameValues.CALL;
        float 
            adjustedWinProb=0f,  
            oppOneBetThreshold=1f-rateHandsOppRaisedSB,
            oppFoldThreshold=rateHandsOppFoldedSB,
            rawWinProb = ( myCard + 1f) / 13f;  
        if (rawWinProb>=oppOneBetThreshold) adjustedWinProb=1f;
        else if (rawWinProb<=oppFoldThreshold) adjustedWinProb=.0f;
        else adjustedWinProb=(rawWinProb-rateHandsOppFoldedSB)/(rateHandsOppRaisedSB-rateHandsOppFoldedSB);
        if (adjustedWinProb>=limpRaiseThreshold) decision=GameValues.RAISE;
        return decision;        
    }   
}