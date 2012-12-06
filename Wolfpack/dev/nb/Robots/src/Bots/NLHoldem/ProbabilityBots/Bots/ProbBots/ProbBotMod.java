package Bots.NLHoldem.ProbabilityBots.Bots.ProbBots;

import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer.FlopAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.RiverAnalyzer.RiverAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.TurnAnalyzer.TurnAnalyzer;

public class ProbBotMod extends GenericBot  {
    
    public HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
    
    public void gameOver() throws Exception {
        
    };
    
    public ProbBotMod() {
        System.out.println("instantiating ProbBot");
        try {
            hca.init();
            FlopAnalyzer.init();
            TurnAnalyzer.init();
            RiverAnalyzer.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public int makeDecision() throws Exception {
        calculatePotOdds();
        calculateWinProb();
        getPureOddsDecision();
        return decision;
    }
   
    public void getPureOddsDecision() {
        decision=CHECKFOLD;
        if (winProb>.5) decision=RAISE;
    }
    
    public void calculatePotOdds() {
        potOdds=callAmt/(pot);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        impliedPotOdds=potPlusRaiseOdds*1.8f;
    }
    
    public void calculateWinProb() throws Exception {
        if (roundNo==DEAL) winProb=hca.getWinProbabilty(holeCards,numPlayersInHand);
        else if (roundNo==FLOP) winProb= FlopAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand);
        else if (roundNo==TURN) winProb= TurnAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand);
        else if (roundNo==RIVER) winProb= RiverAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand,hca);
    }
    
}