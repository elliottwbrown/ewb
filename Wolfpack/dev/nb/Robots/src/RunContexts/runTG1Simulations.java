package RunContexts;

import java.util.ArrayList;
import java.util.Arrays;
import Bots.TG1.GenericBots.GenericBot;
import Bots.TG1.DynamicBots.MemoryBots.*;
import Bots.TG1.StaticBots.PositionBots.*;
import Bots.TG1.RangeBots.GearChangeBots.*;
import Bots.TG1.StaticBots.BasicBots.RaiseAllBot;

public class runTG1Simulations extends TG1Simulator {
 
    static final GenericBot Champion = new GearChangeBot1(); 
    //static final GenericBot Opp1 = Champion;
    //static final GenericBot Opp2 = Champion;
    //static final GenericBot Opp2 = new AntiGearChangeBot1();
    static final GenericBot Opp1 = new FourPointBuck39R0();
    static final GenericBot Opp2 = new RaiseAllBot();
    //static final GenericBot Opp2 = new RaiseAllBot();

    public static void main(String[] args) throws Exception {
        go();
    }
    
    public static void go() throws Exception {
        System.out.println("Wolfpack startSimulation started at " + new java.util.Date());
        
        debugLevel          = 2;       // game ID is logged
        numHandsPerRun      = 10*1000;
        numRunsToPlay       = 10;
        graphResults        = false;     
        
        testHeadsUpMatch();
        // or ...
        //testMultipleHeadsUpMatches();
        //testHeadsUpAgainstAll();
        //testHeadsUpTournament();
        
        // to upload from Game.log file to WolfpackDB (online app)
        //GameLogDBLoader.go();
        
        // to draw a chart from a Game.log. 
        // (used for graphig runs too large to graph in realtime say >10k)
        //ChartResults.go();
        
        System.out.println("Wolfpack startSimulation finished at " + new java.util.Date());
    }
    
    public static void testMultipleHeadsUpMatches() throws Exception {
        testHeadsUpMatch(numRunsToPlay);  
    }
    
    public static void testHeadsUpMatch() throws Exception {
        testHeadsUpMatch(1);        
    }

    public static void testHeadsUpMatch(int n) throws Exception {
        playMultipleHeadsUpMatches(Opp1,Opp2,n);
        System.out.println("numHandsOppRaised "+Opp1.numHandsOppRaisedSB);
        System.out.println("numHands "+Opp1.numHands);
        System.out.println("numOppFolded "+Opp1.numOppFolded);
        System.out.println("numBotFoldedSB "+Opp1.numBotFoldedSB);
        System.out.println("Opp1 cardsIWasDealt"+Arrays.toString(Opp1.cardsIWasDealt));
        System.out.println("Opp2 cardsIWasDealt"+Arrays.toString(Opp2.cardsIWasDealt));
        System.out.println("Opp1 decisionPointsIReached"+Arrays.toString(Opp1.decisionPointsIReached));
        System.out.println("Opp2 decisionPointsIReached"+Arrays.toString(Opp2.decisionPointsIReached));
//                System.out.print(bots[0].rateHandsOppRaisedSB+"\t");
//                System.out.print(bots[0].numHandsOppRaisedSB+"\t");
//                System.out.print(bots[1].rateHandsOppRaisedSB+"\t");
//                System.out.print(bots[1].numHandsOppRaisedSB+"\t");
//                if (bots[1].numHandsOppRaisedSB>0) {
//                    System.out.println("");
//                    System.out.println(bots[1].response);
//                    throw new Exception("what");
//                }
//                System.out.println("");        
    }

    public static void testHeadsUpTournament() throws Exception {
        ArrayList allBots=new ArrayList();
        //allBots.add(new RaiseAllBot());
        //allBots.add(new OrigamiBot39R76());
        //allBots.add(new Adj2LimpBot39R76());
        //allBots.add(new Adj2LimpBot39R50());
        //allBots.add(new FivePointBuck());
        allBots.add(new FourPointBuck39R50());
        //allBots.add(new threePointBuck39R76());
        //allBots.add(new threePointBuck39R50());
        allBots.add(new PositionBot1());
        allBots.add(new GearChangeBot1());
        allBots.add(new GearChangeBot2());
        playHeadsUpTournament(allBots);
    }
    
    public static void testHeadsUpAgainstAll() throws Exception {
        ArrayList AllOthers=new ArrayList();
        //AllOthers.add(new RaiseAllBot());
        //AllOthers.add(new ProbaBot76());
        //AllOthers.add(new OrigamiBot39R76());
        AllOthers.add(new PositionBot1());
        AllOthers.add(new Adj2LimpBot39R76());
        AllOthers.add(new Adj2LimpBot39R50());
        AllOthers.add(new threePointBuck39R50());
        AllOthers.add(new threePointBuck39R76());
        playHeadsUpAgainstAll(Champion,AllOthers);
    }

}