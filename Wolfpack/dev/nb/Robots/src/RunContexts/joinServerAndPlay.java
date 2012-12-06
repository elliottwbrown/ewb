package RunContexts;

import Bots.NLHoldem.MemoryBots.MemoryBot;

public class joinServerAndPlay {
    
    public static void main(String[] args) throws Exception {
        go(args);
    }

    public static void go(String[] args) throws Exception {
        System.out.println("Wolfpack startTournament Started");
        new Thread(new MemoryBot()).start();
        System.out.println("Wolfpack startTournament Finished");
    }
}