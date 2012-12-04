package Bots.TG1.StaticBots.PositionBots;

import Bots.TG1.GenericBots.GenericBot;

public class PositionBot1 extends GenericBot {

    public float 
            foldThreshold=.39f,
            oneBetThreshold=.76f,
            reRaiseThreshold=.5f;
    
    @Override
    public int makeDecision() throws Exception {
        float rawWinProb = (myCardsFace + 1f) / 13f;
        if (actFirst() && !beenRaisedTo) {                                      // first turn
            decision=CALL;
            if (rawWinProb>oneBetThreshold) decision=RAISE;
            if (rawWinProb<foldThreshold) decision=CHECKFOLD;
        } else {
            if (!actFirst() && !beenRaisedTo) {                                 // he limped
                decision = CALL;
                if (rawWinProb<foldThreshold) decision=CHECKFOLD;
                else if (rawWinProb>=oneBetThreshold) decision=RAISE;
                else {
                    float limpedWinProb=((myCardsFace)-8f)/4f;
                    if (limpedWinProb>.5) decision=RAISE;
                }
            } else {                                                            // he raised
                decision = CHECKFOLD;
                potOdds = callAmt / pot;
                float raisedWinProb=0f;
                if (rawWinProb<oneBetThreshold) raisedWinProb=0;
                if (rawWinProb>=oneBetThreshold) raisedWinProb=(myCardsFace-8f) / 4f;
                if (raisedWinProb>potOdds) decision=CALL;
                if (raisedWinProb>reRaiseThreshold && numOppMovedThisHand==1) decision=RAISE;
            }
        }
        return decision;
    }
}