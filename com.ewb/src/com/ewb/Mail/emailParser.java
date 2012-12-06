import com.ewb.FileSystem.FileAccess;
import java.util.StringTokenizer;

public class emailParser {
    
    public static final String
            dataFolder="C:/temp/",
            dataFileName=dataFolder+"emails2.txt";
    
    public static void main(String args[]) throws Exception {
        extractEmailsFromAFile();
    }
    
   // http://www.internettexasholdem.com/phpbb2/memberlist.php?mode=joined&order=ASC&start=50
        
    public static void extractEmailsFromAFile() throws Exception {
        FileAccess fa=new FileAccess();
        fa.openForRead(dataFileName);
        int max=100000;
        for (int i=1;i<=max;i++) {
            String line=fa.readLine();
            StringTokenizer st= new StringTokenizer(line);
            while (st.hasMoreTokens())  {
                String tok=st.nextToken();
                int mailtoAt=tok.indexOf("mailto");
                if (mailtoAt>0) {
                    String addy=tok.substring(mailtoAt+7,tok.indexOf("\"",mailtoAt));
                    System.out.println(addy);
                }
            }
        }
    }
    
}