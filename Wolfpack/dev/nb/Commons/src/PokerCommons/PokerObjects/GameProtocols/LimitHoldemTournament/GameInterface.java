package PokerCommons.PokerObjects.GameProtocols.LimitHoldemTournament;

/**
 * GameInterface
 * all incoming commands are processed here
 * these include game operations a.k.a. turns
 * and met-game operations (join,leave,start)
 *
 */

public class GameInterface extends GameXMLInterface {
    
    // meta-game operations
    public static String join(String PlayerSessionID,String name) {
        String msg="";
        if (!isJoined(PlayerSessionID)) {
            players.put(PlayerSessionID,new Player(PlayerSessionID,name));
            msg="You have joined the game.";
            if (debugLevel>0) System.out.println(">>> Player joined:"+name+","+PlayerSessionID);
        } else {
            msg="You have already joined the game.";
        }
        return msg;
    }
    
    public static String start(String PlayerSessionID) throws Exception {
        String msg="";
        if (handStatus==HAND_NOT_STARTED) {
            if (players.size()>1) {
                System.out.println("*** playGame()");
                startGame();
                if (handStatus==HAND_STARTED) msg+="Started new game at "+gameStart+" with "+players.size()+" players.";
            } else {
                msg="<error>You must have at least 2 players join the game before you start.</error>";
            }
        } else if (handStatus==HAND_STARTED) {
            msg="<error>Game already started.</error>";
        } else if (handStatus==HAND_FINISHED) {
            dealer=getPlayerToTheLeft(dealer);
            startHand();
        }
        return msg;
    }
    
    public static String leave(String PlayerSessionID) {
        System.out.println("removing "+PlayerSessionID);
        players.remove(PlayerSessionID);
        return "<error>You cannot leave a game you havent joined.</error>";
    }
    
    public static String reset(String PlayerSessionID) throws Exception {
        System.out.println("resetting for "+PlayerSessionID);
        resetGame();
        return "reset";
    }    
    
    // game-operations
    public static String turn(int turn,String PlayerSessionID) throws Exception {
        String msg="";
        if (waitingForTurn) {
            if (!currentPlayer.getSessionID().equals(PlayerSessionID))
                msg="<error>It is not your turn.</error>";
            else {
                processTurn(turn);
                if (turn==CALL) msg="<info>You called.</info>";
                if (turn==CHECKFOLD) msg="<info>You checked/folded.</info>";
                if (turn==RAISE) msg="<info>You raised.</info>";
            }
        } else
            msg="<error>It is nobodies turn.</error>";
        return msg;
    }
    
    public static String call(String PlayerSessionID) throws Exception {
        return turn(CALL,PlayerSessionID);
    }
    
    public static String raise(String PlayerSessionID) throws Exception {
        return turn(RAISE,PlayerSessionID);
    }
    
    public static String checkFold(String PlayerSessionID) throws Exception {
        return turn(CHECKFOLD,PlayerSessionID);
    }
    
}