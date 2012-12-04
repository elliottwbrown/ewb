package PokerCommons.PokerObjects.GameProtocols.TG1;

public interface GameValues {
    public static final int CHECKFOLD=1,CALL=2,RAISE=3;                                         // action types
    public static final int HAND_NOT_STARTED=1,HAND_STARTED=2,HAND_FINISHED=3;                  // hand status
    public static final int GAME_NOT_STARTED=1,GAME_STARTED=2,GAME_FINISHED=3;                  // game status
    public static final int DEAL=1,END_OF_HAND=2;                         // round status
    public static final String[] roundNames={"","Deal","End of Hand"};    // round labels
    public static final String[] actionLabels={"","Check/Fold","Check/Call","Bet/Raise"};       // action labels
    public static final String gameName="TG1 1.0";
}
