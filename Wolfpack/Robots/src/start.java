import OnlineConnector.Dev.startGrabber;
import RunContexts.runNLHoldemSimulation;
import RunContexts.joinServerAndPlay;
import RunContexts.runTG1Simulations;

public class start {

    public static String 
            doWhat, 
            server = "localhost", 
            logDir = "c:\\temp",
            configFile = "C:/data/dev/svn_rep_bnb/nb/WolfPack/Robots/Robots/src"
            + "/Bots/MemoryBots/Configuration/botVars.cfg" ;
    
    public static void main(String[] args) throws Exception {
        // 1- join an http server game
        // 2- 2 bots head to head in NLHoldem
        // 4- 2 bots head to head in TG1
        doWhat="4"; //      
        if (args.length > 0) doWhat = args[0];
        if (args.length > 1) server = args[1];
        if (args.length > 2) configFile = args[2];
        if (args.length > 3) logDir = args[3];
        go();
    }

    public static void go() throws Exception {
        if (doWhat.equals("1")) {
            joinServerAndPlay.go(new String[]{server, configFile, logDir});
        } else if (doWhat.equals("2")) {
            runNLHoldemSimulation.go();
        } else if (doWhat.equals("3")) {
            startGrabber.go(new String[]{});
        } else if (doWhat.equals("4")) {
            runTG1Simulations.go();
        }
    }
}