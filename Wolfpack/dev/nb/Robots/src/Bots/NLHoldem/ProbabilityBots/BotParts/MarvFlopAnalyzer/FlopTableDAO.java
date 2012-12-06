package Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer;

import com.ewb.DB.DBAccess;
import com.ewb.FileSystem.FileAccess;
import java.util.StringTokenizer;
import java.util.HashMap;

public class FlopTableDAO {
    
    public static HashMap resultsXML;
    public static final String FileName="C:/temp/out-1-3-5-9.txt";
    public static DBAccess db;

    public static void createDBfromFile() throws Exception {
        init();
        deleteDataFromDB();
        insertDataIntoDB();
    }
    
    public static void init() throws Exception {
        db=new DBAccess("PokerBot.Configuration.OracleOnLocalhost");
        db.InitConnection();
        db.InitPreparedStatement("select prob1,prob3,prob5,prob9 from flop_probabilities where card_index=?");
    }
     
    
    private static void deleteDataFromDB() throws Exception {
        System.out.println(">>> deleting");
        db.runCommand("delete from flop_probabilities");
        db.commit();
    }
    
    private static void insertDataIntoDB() throws Exception {
        System.out.println(">>> inserting");
        FileAccess fa=new FileAccess();
        fa.openForRead(FileName);
        //db.InitPreparedStatement("insert into flop_probabilities values (?,?,?,?,?,?,?,?,?)");
        db.InitPreparedStatement("insert into flop_probabilities values (?,?,?,?,?)");
        int c=0;
        try {
            while (true) {
                StringTokenizer st=new StringTokenizer(fa.readLine());
                c++;
                try {
                    String cardIndex=st.nextToken()+st.nextToken();
                    st.nextToken(); // waste the bar
                    cardIndex+=st.nextToken()+st.nextToken()+st.nextToken();
                    db.ps.setString(1,cardIndex);
                    db.ps.setString(2, st.nextToken());
                    db.ps.setString(3, st.nextToken());
                    db.ps.setString(4, st.nextToken());
                    db.ps.setString(5, st.nextToken());
                    db.ps.execute();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                    db.commit();
                }
                if (c%1000==0) {
                    db.commit();
                    System.out.println("c="+c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.commit();
        }
    }
    
}

