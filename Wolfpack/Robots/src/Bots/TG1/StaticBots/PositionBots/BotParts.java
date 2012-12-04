package Bots.TG1.StaticBots.PositionBots;

import PokerCommons.PokerObjects.GameProtocols.TG1.*;

public class BotParts {

    /**
     * This method is used when the SB makes his first act. i.e. Decision 1 of each hand.
     * It looks only at the Card dealt and the thresholds for folding or raising
     * The assumption is that bad cards (below threshold) should be folded and that
     * good cards should be raised, and the cards in the middle limped.
     * 
     * since the potOdds are 3-1 here, any card with >=1/3 chance of winning should call
     */
    public int adj4Card(int holeCard,float oneBetThreshold, float foldThreshold) 
            throws Exception {
        int decision = Game.CALL;
        float winProb = (holeCard + 1f) / 13f;
        if (winProb >= oneBetThreshold) decision = Game.RAISE;
        if (winProb < foldThreshold) decision = Game.CHECKFOLD;
        return decision;
    }

    /**
     * This method is used when a decision is made in the face of a raise. this could
     * mean anywhere at decision points 3-10
     * 
     * it takes into account the rate of hands with which the Opp raises (n%), and assumes 
     * this corresponds to the top n% of his cards. 
     * 
     * anything under is folded. 
     * 
     * anything above the range, or the mid point of the range is raised. 
     * 
     * for anything in the lower half of this range, an adjusted win prob is computed
     * and then compared to the potOdds and either folded or called accordingly
     */    
    public int adj2Raise(int numOppRaisedThisHand,float potOdds, int myCard,
            float oppOneBetThreshold, float twoBetThreshold, float fourBetThreshold) {
        float rawWinProb = ( myCard + 1f) / 13f,
            numCardsOppRaises = 12-(12*oppOneBetThreshold),
            adjustedWinProb=0f;
        int decision = GameValues.CHECKFOLD;
        if (rawWinProb<oppOneBetThreshold) adjustedWinProb=0;
        if (rawWinProb>=oppOneBetThreshold) 
            adjustedWinProb=(myCard-(12-numCardsOppRaises)) / numCardsOppRaises;
        if (adjustedWinProb>potOdds) decision=GameValues.CALL;
        if (adjustedWinProb>twoBetThreshold && numOppRaisedThisHand==1) 
            decision=GameValues.RAISE;
        if (adjustedWinProb>fourBetThreshold && numOppRaisedThisHand==2) 
            decision=GameValues.RAISE;
        return decision;
    }  
}