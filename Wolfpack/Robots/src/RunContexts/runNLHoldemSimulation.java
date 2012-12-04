package RunContexts;

import com.ewb.Utilities.XMLUtilities;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameInterface;
import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.MemoryBots.MemoryBot;
import Bots.NLHoldem.RespectBots.RespectBot1;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;

public class runNLHoldemSimulation implements GameValues {

    public static int debugLevel = 0;
    public static int numHandsToPlay = 1000*1000;
    public static GameInterface game = null;
    public static GenericBot[] bots;

    public static void main(String[] args) throws Exception {
        go();
    }

    public static void go() throws Exception {
        System.out.println("Wolfpack startSimulation Started at " + new java.util.Date());
        game = new GameInterface();
        bots = new GenericBot[2];
        addPlayer(new MemoryBot());
        addPlayer(new RespectBot1());
        startPlaying(bots);
    }

    private static void addPlayer(GenericBot bot) throws Exception {
        if (bots[0] == null) {
            bots[0] = bot;
        } else {
            bots[1] = bot;
        }
    }

    private static void startPlaying(final GenericBot[] bots) throws Exception {
        String response;
        for (int i = 0; i < bots.length; i++) {
            response = game.join(bots[i].toString(), bots[i].botName);
        }
        int c = 0;
        while (c++ < numHandsToPlay) {
            System.out.print(".,");
            playHand(bots);
        }
    }

    private static void playHand(final GenericBot[] bots) throws NumberFormatException, Exception {
        String response;
        response = game.start("");
        if (debugLevel > 1) {
            System.out.println(response);
        }
        int handStatus = HAND_NOT_STARTED;
        while (handStatus != HAND_FINISHED) {
            response = game.getGameState("");
            if (response.contains("<handStatus/>")) {
                handStatus = Integer.parseInt(XMLUtilities.getTag(response, "<handStatus/>"));
            }
            if (handStatus == HAND_STARTED) {
                for (int i = 0; i < bots.length; i++) {
                    response = game.getGameState(bots[i].toString());
                    if (debugLevel > 1) {
                        System.out.println(response);
                    }
                    bots[i].response = response;
                    bots[i].parseGameState();
                    if (response.contains("<turnInfo><player/>" + bots[i])) {
                        int decision = bots[i].makeDecision();
                        if (debugLevel > 0) {
                            System.out.println(bots[i] + " " + actionLabels[decision]);
                        }
                        game.turn(decision, bots[i].toString());
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < bots.length; i++) {
            response = game.getGameState(bots[i].toString());
            if (debugLevel > 0) {
                System.out.println("gameOver");
            }
            bots[i].response = response;
            bots[i].parseGameState();
            bots[i].gameOver();
        }
    }
}