package PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash;

import PokerCommons.PokerObjects.Players.Player;
import java.util.Iterator;

/**
 * GameXMLInterface
 * all outgoing responses are formatted here as XML
 * these include responses to game operations a.k.a. turns
 * and meta-game operations (join,leave,start)
 * as wells as game state updates
 *
 */
public class GameXMLInterface extends Game {

    public static String PlayerSessionID = "",oldMsg="";

    public static String formatMsg(String message) throws Exception {
        return ""
                + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<message>"
                + "<messageType>GameStateUpdate</messageType>"
                + "<messageTime>" + new java.util.Date() + "</messageTime>"
                + "<messageBody>"
                + message
                + "</messageBody>"
                + "</message>";
    }

    public static String getGameState(String PlayerSessionIDIn) {
        PlayerSessionID = PlayerSessionIDIn;
        String msg=""
                + "<gameState>"
                + getGameInfo()
                + (handStatus > HAND_NOT_STARTED ? getHandInfo() : "")
                + (handStatus > HAND_NOT_STARTED ? getRoundInfo() : "")
                + (handStatus == HAND_STARTED ? getTurnInfo() : "")
                + "</gameState>";
        if (!msg.equals(oldMsg)) {
            if (debugLevel>2) System.out.println("getGameState:roundNo"+roundNo);
            //System.out.println(msg);
            oldMsg=msg;
        }
        return msg;
    }

    private static String getGameInfo() {
        String msg = "<gameInfo>"
                + "<gameID>" + gameID + "</gameID>"
                + "<smallBlindAmt>" + smallBlindAmt + "</smallBlindAmt>";
        if (handStatus == HAND_STARTED) msg += "<gameStart>" + gameStart + "</gameStart>";
        if (players.size() > 0) msg += getPlayers();
        msg += "</gameInfo>";
        return msg;
    }

    private static String getHandInfo() {
        String msg = "<handInfo>"
                + "<handStatus>" + handStatus + "</handStatus>"
                + "<dealer>" + dealer.getSessionID() + "</dealer>"
                + "<smallBlind>" + smallBlind.getSessionID() + "</smallBlind>"
                + "<bigBlind>" + bigBlind.getSessionID() + "</bigBlind>"
                + "<handHistory>" + handHistory + "</handHistory>"
                + "<roundHistory>" + roundHistory + "</roundHistory>";
        msg += "</handInfo>";
        return msg;
    }

    private static String getRoundInfo() {
        String msg = ""
                + "<roundInfo>"
                + "<roundNo>" + roundNo + "</roundNo>";
        if (handStatus==HAND_STARTED) msg+=getCardInfo();
        msg += "<turnHistory>" + turnHistory + "</turnHistory>"
                + "</roundInfo>";
        return msg;
    }

    private static String getTurnInfo() {
        return ""
                + "<turnInfo>"
                + "<player>" + currentPlayer.getSessionID() + "</player>"
                + "<startOfTurn>" + turnStart + "</startOfTurn>"
                + "<endOfTurn>" + turnEnd + "</endOfTurn>"
                + "<pot>" + pot + "</pot>"
                + "<callAmount>" + currentPlayer.getCallAmt() + "</callAmount>"
                + "<raiseAmount>" + raiseAmt[roundNo] + "</raiseAmount>"
                + (handStatus > HAND_NOT_STARTED ? getPlayersInHand() : "") 
                + "</turnInfo>";
    }

    private static String getCardInfo() {
        String msg = "<cardInfo>";
        String OpponentPlayerSessionID = "";
        for (Player p : players.values()) {
            if (!p.getSessionID().equals(PlayerSessionID)) {
                OpponentPlayerSessionID = p.getSessionID();
            }
        }

        if (isPlayerInGame(PlayerSessionID)) {
            msg += "<card1>" + players.get(PlayerSessionID).getHoleCards().get(0).getCardShortestName()+"</card1>"
                    + "<card2>" + players.get(PlayerSessionID).getHoleCards().get(1).getCardShortestName()+"</card2>";
        }
        if (roundNo >= FLOP) {
            msg += "<card3>" + communityCards.get(0).getCardShortestName()+"</card3>"
                    + "<card4>" + communityCards.get(1).getCardShortestName()+"</card4>"
                    + "<card5>" + communityCards.get(2).getCardShortestName()+"</card5>";
        }
        if (roundNo >= TURN) {
            msg += "<card6>" + communityCards.get(3).getCardShortestName()+"</card6>";
        }
        if (roundNo >= RIVER) {
            msg += "<card7>" + communityCards.get(4).getCardShortestName()+"</card7>";
        }
        if (roundNo > RIVER) {
            try {
                //if (currentPlayer.isInCurrentHand()) {;
                    msg += "<card8>" + players.get(OpponentPlayerSessionID).getHoleCards().get(0).getCardShortestName()+"</card8>";
                    msg += "<card9>" + players.get(OpponentPlayerSessionID).getHoleCards().get(1).getCardShortestName()+"</card9>";
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        msg += "</cardInfo>";
        return msg;
    }

    private static String getPlayers() {
        String msg = "";
        msg += "<playersInGame>";
        for (Player p : players.values()) {
            msg += "<player>"
                    + "<sessionID>" + p.getSessionID() + "</sessionID>"
                    + "<name>" + p.getName() + "</name>"
                    + "<balance>" + p.getBalance() + "</balance>"
                    + "</player>";
        }
        msg += "</playersInGame>";
        return msg;
    }

    private static String getPlayersInHand() {
        String msg = "";
        msg += "<playersInHand>";
        msg += "<numPlayersInHand>" + countPlayersInHand() + "</numPlayersInHand>";
        Iterator<Player> it = players.values().iterator();
        for (Player p : players.values()) {
            if (p.isInCurrentHand()) {
                msg += "<player><sessionID>" + p.getSessionID() + "</sessionID></player>";
            }
        }
        msg += "</playersInHand>";
        return msg;
    }
}