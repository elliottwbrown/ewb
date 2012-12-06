package PokerServer.Server;

import javax.servlet.http.*;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.*;
//import PokerCommons.PokerObjects.GameProtocols.ToyGameOne.*;
//import PokerCommons.Poker.LimitHoldemTournament.*;

public class GameModel extends GenericModel {
    
    public GameModel() throws Exception {
    }
    
    // meta-game operations
    public static void getWelcome(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg("Welcome to WolfPack Server v."+Game.version);
    }
    
    public static void join(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.join(req.getSession().getId(), req.getParameter("name")));
    }
    
    public static void leave(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.leave(req.getSession().getId()));
    }
    
    public static void start(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.start(req.getSession().getId()));
    }
    
    public static void reset(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.reset(req.getSession().getId()));
    }
    
    // game-operations
    public static void call(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.call(req.getSession().getId()));
    }
    
    public static void raise(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.raise(req.getSession().getId()));
    }
    
    public static void checkFold(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.checkFold(req.getSession().getId()));
    }
    
    public static void getGameState(HttpServletRequest req, HttpServletResponse res)  throws Exception {
        returnLocation=GameInterface.formatMsg(GameInterface.getGameState( req.getSession().getId() ));
    }
    
} 