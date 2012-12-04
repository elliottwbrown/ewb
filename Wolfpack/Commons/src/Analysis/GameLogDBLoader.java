package Analysis;

import PokerCommons.DB.wolfpack_oracle;
import com.ewb.FileSystem.FileAccess;
import com.ewb.Strings.Utilities;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class GameLogDBLoader extends GameLogDBUtils {

    static int debugLevel=1;
    static int commitInterval=50;

    
    static int STARTING_BALANCE = 1000000;
    static int maxHandsToLoad = 1000000;
    static int SESSION_ID;
    static int HAND_ID;
    static int PLAYER_ONE_ID,PLAYER_TWO_ID;
    static String playerOneName = "";
    static String playerTwoName = "";
    static String GameText = "";
    static String logFileName = "c:/temp/Games2.log";    
    static PreparedStatement ps1,ps2,ps3,ps4,ps5;
    
    public static void main(String args[]) throws Exception {
        go();
    }

    public static void go() throws Exception {
        System.out.println("GameLogDBLoader started" + new java.util.Date());
        System.out.println("creating logFileAccess");
        fs = new FileAccess();
        System.out.println("initializing DB");
        wolfpack_oracle.initDB();
        initPreparedStatements();
        deleteData();
        insertData();
        System.out.println("GameLogDBLoader finished" + new java.util.Date());
    }

    public static void initPreparedStatements() throws SQLException {
        if (debugLevel>1) System.out.println("initPreparedStatements");
        String sql = "insert into HAND (hand_id,session_id,start_ts) values (?,?,?)";
        ps1= wolfpack_oracle.conn.prepareStatement(sql);
        sql = "insert into PLAYER (player_id,player_name,starting_balance,player_type) values (player_seq.nextval,?,?,?)";
        ps2= wolfpack_oracle.conn.prepareStatement(sql);
        sql="insert into ENTRY (entry_id,hand_result,hole_card,starting_balance,position,player_id,hand_id)  values (entry_seq.nextval,?,?,?,?,?,?)";
        ps3= wolfpack_oracle.conn.prepareStatement(sql);
        sql="insert into TURN (TURN_ID,TURN_TYPE,AMOUNT,PLAYER_ID,HAND_ID) values (TURN_SEQ.nextval,?,?,?,?)";
        ps4= wolfpack_oracle.conn.prepareStatement(sql);
    }

    public static void insertData() throws Exception {
        if (debugLevel>1) System.out.println("insertData");
        fs.openForRead(logFileName);
        boolean firstRun = true;
        insertSession();
        int c=0;
        skipSourceCode();
        while (++c<maxHandsToLoad) {
            if (debugLevel>1) System.out.println("inserting hand "+c);
            try {
                GameText = readHand();
                try {
                    insertHand();
                    if (firstRun) {
                        insertPlayers();
                        firstRun = false;
                    }
                    insertEntries();
                    insertTurns();
                } catch (Exception e2) {
                    if(e2.getLocalizedMessage().contains(" unique constraint (WOLFPACK.HAND_PK) violated")); 
                    System.out.println("Hand already loaded - skipping.");
                }
            } catch (Exception e) {
                if (e.getLocalizedMessage().contains("end of file")) e.printStackTrace();
                else e.printStackTrace();
                break;
            }
            if (c%commitInterval==0) {
                wolfpack_oracle.conn.commit();
                System.out.println("Committing ..."+c);
            }
        }
        wolfpack_oracle.conn.commit();
    }

    public static void insertEntry(String playerName, int PLAYER_ID) throws Exception {
        if (debugLevel>1) System.out.println("insertEntry");
        int HAND_RESULT = GameText.contains(playerName + " wins") ? 1 : 0;
        if (HAND_RESULT==1)
            HAND_RESULT = Integer.parseInt(Utilities.getTokenAfter(GameText, playerName + " wins"));
        String HOLE_CARDs = Utilities.getTokenAfter(GameText, playerName + " had");
        int HOLE_CARD = convertCardToInt(HOLE_CARDs);
        String smallBlindName = (Utilities.getNthToken(GameText, 12));
        int POSITION = smallBlindName.equals(playerName) ? 1 : 2;
        STARTING_BALANCE = Integer.parseInt(Utilities.getStringFromXtoEOL(GameText,playerName+","));
        ps3.setInt(1,HAND_RESULT);
        ps3.setInt(2,HOLE_CARD);
        ps3.setInt(3,STARTING_BALANCE);
        ps3.setInt(4,POSITION);
        ps3.setInt(5,PLAYER_ID);
        ps3.setInt(6,HAND_ID);
        ps3.execute();
        
    }

    public static void insertEntries() throws Exception {
        insertEntry(playerOneName, PLAYER_ONE_ID);
        insertEntry(playerTwoName, PLAYER_TWO_ID);
    }

    public static int getPlayerID() throws Exception {
        String sql = "select player_seq.currval from dual";
        ResultSet rs=wolfpack_oracle.executeQuery(sql);
        rs.next();
        return Integer.parseInt(rs.getString(1));        
    }
    
    public static void insertPlayers() throws Exception {
        if (debugLevel>1) System.out.println("insertPlayers");
        playerOneName = (Utilities.getNthToken(GameText, 12));
        int PLAYER_TYPE = playerOneName.contains("Bot") ? 1 : 2;
        ps2.setString(1,playerOneName);
        ps2.setInt(2,STARTING_BALANCE);
        ps2.setInt(3,PLAYER_TYPE);
        ps2.execute();
        PLAYER_ONE_ID=getPlayerID();
        playerTwoName = (Utilities.getNthToken(GameText, 16));
        PLAYER_TYPE = playerTwoName.contains("Bot") ? 1 : 2;
        ps2.setString(1,playerTwoName);
        ps2.setInt(2,STARTING_BALANCE);
        ps2.setInt(3,PLAYER_TYPE);
        ps2.execute();
        PLAYER_TWO_ID=getPlayerID();
    }

    public static void insertHand() throws Exception {
        if (debugLevel>1) System.out.println("insertHand started");
        HAND_ID = Integer.parseInt(Utilities.getNthToken(GameText, 3));
        ps1.setInt(1, HAND_ID);
        ps1.setInt(2, SESSION_ID);
        ps1.setString(3, null);
        ps1.execute();
        if (debugLevel>1) System.out.println("insertHand completed");
    }

    public static void insertSession() throws Exception {
        String sql = "insert into GAME_SESSION (session_id,start_ts,rule_id) values (session_seq.nextval,sysdate,1)";
        wolfpack_oracle.execute(sql);
        sql = "select session_seq.currval from dual";
        ResultSet rs=wolfpack_oracle.executeQuery(sql);
        rs.next();
        SESSION_ID=Integer.parseInt(rs.getString(1));
        rs=null;
    }    

    public static void insertTurns() throws Exception {
        StringTokenizer st = new StringTokenizer(GameText, "\r\n");
        int c = 0;
        while (st.hasMoreTokens()) {
            String line=st.nextToken();
            if (line.contains("called")||line.contains("folded")||line.contains("bet")||line.contains("raised")) {
                int PLAYER_ID=Utilities.getNthToken(line, 1).equals(playerOneName)?PLAYER_ONE_ID:PLAYER_TWO_ID;
                String TURN_TYPEs=Utilities.getNthToken(line, 2);
                int TURN_TYPE;
                if (TURN_TYPEs.equals("folded.")) TURN_TYPE=1;
                else if (TURN_TYPEs.equals("called")) TURN_TYPE=2;
                else TURN_TYPE=3;
                String AMOUNTs=TURN_TYPE==1?"0":Utilities.getNthToken(line, 3);
                int AMOUNT = Integer.parseInt(AMOUNTs);
                ps4.setInt(1,TURN_TYPE);
                ps4.setInt(2,AMOUNT);
                ps4.setInt(3,PLAYER_ID);
                ps4.setInt(4,HAND_ID);
                ps4.execute();
            }
        }
    }
    
    public static void deleteData() {
        if (debugLevel>1) System.out.println("deleteData");
        try {
            wolfpack_oracle.execute("delete from ENTRY");
            wolfpack_oracle.execute("delete from TURN");
            wolfpack_oracle.execute("delete from HAND");
            wolfpack_oracle.execute("delete from PLAYER");
            wolfpack_oracle.execute("delete from GAME_SESSION");
            wolfpack_oracle.conn.commit();
        } catch (Exception e) {
        }
    }        
}
