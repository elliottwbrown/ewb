package Bots.NLHoldem.ProbabilityBots.Bots.ProbBots;

public class ProbBotb extends ProbBot  {
    
    public ProbBotb() {
    }
    
    public void getPureOddsDecision() {
        decision=CALL;
        if (winProb>.5) decision=RAISE;
    }

}

