package PokerCommons.PokerObjects.GameProtocols.LimitHoldemTournament;

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
    
    public static String PlayerSessionID="";
    
    public static String formatMsg(String message)  throws Exception {
        return ""+
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<message>"+
                "<messageType/>GameStateUpdate"+
                "<messageTime/>"+new java.util.Date()+
                "<messageBody>"+
                message+
                "</messageBody>"+
                "</message>";
    }
    
    public static String getGameState(String PlayerSessionIDIn) {
        PlayerSessionID=PlayerSessionIDIn;
        return ""+
                "<gameState>"+
                getGameInfo()+
                (handStatus>HAND_NOT_STARTED?getHandInfo():"")+
                (handStatus>HAND_NOT_STARTED?getRoundInfo():"")+
                (handStatus==HAND_STARTED?getTurnInfo():"")+
                "</gameState>";
    }
    
    private static String getGameInfo() {
        String msg="<gameInfo>"+
                "<gameID/>"+gameID+
                "<smallBlindAmt/>"+smallBlindAmt;
        if (handStatus==HAND_STARTED) msg+="<gameStart/>"+gameStart;
        if (players.size()>0) msg+=getPlayers();
        msg+="</gameInfo>";
        return msg;
    }
    
    private static String getHandInfo() {
        String msg="<handInfo>"+
                "<handStatus/>"+handStatus+
                "<dealer/>"+dealer.getSessionID()+
                "<smallBlind/>"+smallBlind.getSessionID()+
                "<bigBlind/>"+bigBlind.getSessionID()+
                "<handHistory/>"+handHistory+
                "<roundHistory>"+roundHistory+"</roundHistory>";
        msg+="</handInfo>";
        return msg;
    }
    
    private static String getRoundInfo() {
        String msg=""+
                "<roundInfo>"+
                "<roundNo/>"+roundNo;
        if (handStatus==HAND_STARTED) msg+=getCardInfo();
        msg+="<turnHistory>"+turnHistory+"</turnHistory>"+
                "</roundInfo>";
        return msg;
    }
    
    private static String getTurnInfo() {
        return ""+
                "<turnInfo>"+
                "<player/>"+currentPlayer.getSessionID()+
                "<startOfTurn/>"+turnStart+
                "<endOfTurn/>"+turnEnd+
                "<pot/>"+pot+
                "<callAmount/>"+currentPlayer.getCallAmt()+
                "<raiseAmount/>"+raiseAmt[roundNo]+
                (handStatus>HAND_NOT_STARTED?getPlayersInHand():"")+
                "</turnInfo>";
    }
    
    private static String getCardInfo() {
        String msg="<cardInfo>";
        if (isPlayerInGame(PlayerSessionID)) {
            msg+="<card1/>"+players.get(PlayerSessionID).getHoleCards().get(0).getCardShortestName()+
                    "<card2/>"+players.get(PlayerSessionID).getHoleCards().get(1).getCardShortestName();
        }
        if (roundNo>=FLOP) {
            msg+= "<card3/>"+communityCards.get(0).getCardShortestName()+
                    "<card4/>"+communityCards.get(1).getCardShortestName()+
                    "<card5/>"+communityCards.get(2).getCardShortestName();
        }
        if (roundNo>=TURN) {
            msg+= "<card6/>"+communityCards.get(3).getCardShortestName();
        }
        if (roundNo>=RIVER) {
            msg+= "<card7/>"+communityCards.get(4).getCardShortestName();
        }
        msg+="</cardInfo>";
        return msg;
    }
    
    private static String getPlayers() {
        String msg="";
        msg+="<playersInGame>";
        for(Player p : players.values()) 
            msg+="<player/><sessionID/>"+p.getSessionID()+
                "<name/>"+p.getName()+
                "<balance/>"+p.getBalance();
        msg+="</playersInGame>";
        return msg;
    }
    
    private static String getPlayersInHand() {
        String msg="";
        msg+="<playersInHand>";
        msg+="<numPlayersInHand/>"+countPlayersInHand();
        Iterator<Player> it=players.values().iterator();
        for(Player p : players.values()) if (p.isInCurrentHand())
            msg+="<player><sessionID/>"+p.getSessionID()+"</player>";
        msg+="</playersInHand>";
        return msg;
    }
    
}
