package Analysis;

import com.ewb.FileSystem.FileAccess;

public class  GameLogDBUtils {

    static FileAccess fs;
    
    public static int convertCardToInt(String HOLE_CARD) {
        HOLE_CARD=HOLE_CARD.substring(0,1);
        if (HOLE_CARD.equals("T")) HOLE_CARD="10";
        if (HOLE_CARD.equals("J")) HOLE_CARD="11";
        if (HOLE_CARD.equals("Q")) HOLE_CARD="12";
        if (HOLE_CARD.equals("K")) HOLE_CARD="13";
        if (HOLE_CARD.equals("A")) HOLE_CARD="14";
        return Integer.parseInt(HOLE_CARD);
    } 
    
    public static String readHand() throws Exception {
        String GameText = "";
        while (true) {
            String line = fs.readLine();
            GameText += line;
            if (line.contains("End of Hand")) break;
        }
        return GameText;
    }    

    public static void skipSourceCode() throws Exception {
        while (true) 
            if (fs.readLine().contains("Hand Histories Begin")) break;
    }    
    
}