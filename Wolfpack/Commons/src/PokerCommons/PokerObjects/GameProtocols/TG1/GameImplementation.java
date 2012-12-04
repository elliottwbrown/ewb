package PokerCommons.PokerObjects.GameProtocols.TG1;

import java.util.Iterator;
import java.util.TreeMap;
import ca.ualberta.cs.poker.HandEvaluator;
import PokerCommons.PokerObjects.Players.Player;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.Deck;

public class GameImplementation implements GameValues {
    
    public static int debugLevel=0;
    public static int handID=0,handStatus=HAND_NOT_STARTED,gameStatus=GAME_NOT_STARTED,roundNo=0;
    public static int numRaisesThisRound=0;
    public static HandEvaluator handEval;
    public static int pot;
    public static boolean currentPlayerWent=false;
    public static TreeMap<String,Player> players=new TreeMap();
    public static Player dealer=null,currentPlayer=null,smallBlind=null,bigBlind=null;
    public static java.util.Date gameStart,turnStart,turnEnd;
    public static Deck deck;
    public static String handHistory="",turnHistory="",roundHistory="";
    public static com.ewb.Logging.Logger
            balancesLogger,
            gamesLogger;
    
    public static void dealHoleCards() {
        for (Player p : players.values()) {
            Cards holeCards=new Cards();
            holeCards.add(deck.removeTopCard());
            p.setHoleCards(holeCards);
            if (debugLevel>2) System.out.println(p.getName()+":"+p.getHoleCards().get(0).getCardShortestName());
        }
    }
    
    public static Player getPlayerToTheLeft(Player player) {
        String next=null;
        boolean found=false;
        Player nextPlayer=null;
        int playerNo=0;
        for (String p : players.keySet()) {
            playerNo++;
            if (p.equals(player.getSessionID())) {
                found=true;
                int nextNo=playerNo%players.size()+1;
                int playerNo2=0;
                for (String p2 : players.keySet()) {
                    playerNo2++;
                    if (playerNo2==nextNo) nextPlayer=players.get(p2);
                }
                break;
            }
        }
        return nextPlayer;
    }
    
    public static Player getPlayerInHandToTheLeft(Player player) {
        Player nextPlayer=getPlayerToTheLeft(player);
        if (!nextPlayer.isInCurrentHand()) nextPlayer=getPlayerInHandToTheLeft(nextPlayer);
        return nextPlayer;
    }
    
    public static int countPlayersInGame() {
        int cnt=0;
        for (Player p : players.values()) if (p.isInCurrentHand()) cnt++;
        return cnt;
    }
    
    public static void setRandomDealer() {
        int random=(int) (Math.random()*players.size());
        Iterator<Player> it = players.values().iterator();
        for (int c=0; c<=random; c++) dealer=it.next();
//        if (debugLevel>0) gamesLogger.log(">>> Dealer="+dealer.getSessionID());
    }
    
    public static void betAllPlayers(String SessionID,int amt) {
        for (String p : players.keySet()) {
            if (!p.equals(SessionID)) {
                Player pl=players.get(p);
                pl.setCallAmt(pl.getCallAmt()+amt);
            }
        }
    }
    
    public static int countPlayersInHand() {
        int c=0;
        for (Player p : players.values()) if (p.isInCurrentHand()) c++;
        return c;
    }
    
    public static boolean isPlayerInGame(String SessionID) {
        boolean found=false;
        for (String p : players.keySet()) {
            if (p.equals(SessionID)) {
                found=true;
                break;
            }
        }
        return found;
    }
    
    public static boolean isRemainingCalls() {
        boolean callsRemaining=false;
        for (Player p : players.values()) if (p.isInCurrentHand() && (p.getCallAmt()>0 || !p.isBetThisRound())) callsRemaining=true;
        return callsRemaining;
    }
    
    public static boolean isJoined(String PlayerSessionID) {
        boolean alreadyJoined=false;
        for (Player p : players.values()) {
            if (p.getSessionID().equals(PlayerSessionID))
                alreadyJoined=true;
        }
        return alreadyJoined;
    }
}