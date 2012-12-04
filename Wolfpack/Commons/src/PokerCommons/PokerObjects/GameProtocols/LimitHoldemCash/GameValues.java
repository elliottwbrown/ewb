package PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash;

public interface GameValues {
    public static final int CHECKFOLD=1,CALL=2,RAISE=3;                                         // action types
    public static final int HAND_NOT_STARTED=1,HAND_STARTED=2,HAND_FINISHED=3;                  // hand status
    public static final int GAME_NOT_STARTED=1,GAME_STARTED=2,GAME_FINISHED=3;                  // game status
    public static final int DEAL=1,FLOP=2,TURN=3,RIVER=4,END_OF_HAND=5;                         // round status
    public static final String[] roundNames={"","Deal","Flop","Turn","River","End of Hand"};    // round labels
    public static final String[] actionLabels={"","Check/Fold","Check/Call","Bet/Raise"};       // action labels
    public static final String gameName="Heads Up Limit Holdem Cash 1.0";
}
