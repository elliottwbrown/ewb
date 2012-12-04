package Bots.NLHoldem.RespectBots;

public class RespectBot2 extends RespectBotEngine {

    public RespectBot2() {
        init();
    }

    @Override
    public int makeDecision() throws Exception {
        if (lastGameID != gameID) startNewHand();
        calculatePotOdds();
        calculateWinProb();
        adjustWinProbForRespect();
        getPureOddsDecision();
        considerBluffing();
        logInGameData();
        if (winProb>1 || winProb<0) throw new Exception("winProb problem:"+winProb);
        return decision;
    }
}