package RunContexts;

import Bots.TG1.GenericBots.GenericBot;
import java.util.ArrayList;
import junit.framework.TestCase;

public class runTG1SimulationTest extends TestCase {
    
    public runTG1SimulationTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPlayHeadsUpAgainstAll() throws Exception {
        System.out.println("playHeadsUpAgainstAll");
        GenericBot Player1 = null;
        ArrayList AllOthers = null;
        TG1Simulator.playHeadsUpAgainstAll(Player1, AllOthers);
        fail("The test case is a prototype.");
    }

    public void testPlayHeadsUpTournament() throws Exception {
        System.out.println("playHeadsUpTournament");
        ArrayList AllPlayers = null;
        TG1Simulator.playHeadsUpTournament(AllPlayers);
        fail("The test case is a prototype.");
    }

    public void testPlayHeadsUpMatch() throws Exception {
        System.out.println("playHeadsUpMatch");
        GenericBot Player1 = null;
        GenericBot Player2 = null;
        TG1Simulator.playHeadsUpMatch(Player1, Player2);
        fail("The test case is a prototype.");
    }
}
