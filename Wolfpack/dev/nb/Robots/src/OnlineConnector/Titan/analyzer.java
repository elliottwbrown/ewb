package OnlineConnector.Titan;

import java.util.StringTokenizer;

public class analyzer extends grabber {
    
//  Player Messages
//    1. 'Dealer: x checks'
//    2. 'Dealer: x folds'
//    3. 'Dealer: x bets y'
//    4. 'Dealer: x raises y'
//    5. 'Dealer: x calls y '
//    6. 'Dealer: x goes All-in y'
//    7. 'Dealer: x posts Small Blind y'
//    8. 'Dealer: x posts Big Blind y'
//  Table Messages
//    9. 'Dealer: x is the dealer'
//    10.'Dealer: x has left the table'
//    11.'Dealer: Player x has joined the table'
//  Game Messages
//    12.'Dealer: Starting a new hand (#f)'
//    13.'Dealer: Dealing z'
//       'Fs Fs Fs Fs Fs'          *  shows all cards to date
//    14.'Dealer: Player x wins the main pot y'
//    15.'Dealer: x shows w'
//       'Fs Fs'
//    16.'Dealer: x wins with w'
//       'Fs Fs Fs Fs Fs y'
    
    public static void main(String[] args) throws Exception {
        grabber.calibrate();
        loop();
    }
    
    private static void analyzeNewLine(final String newLine) {
        //System.out.print(":"+newLine);
        StringTokenizer st=new StringTokenizer(newLine);
        String player="",action="",cards="",round="";
        float amt=0;
        
        // what type of message is it ?
        if (
                newLine.contains("bets") ||
                newLine.contains("calls") ||
                newLine.contains("raises") ||
                newLine.contains("goes All-in") ||
                newLine.contains("posts Small Blind") ||
                newLine.contains("posts Big Blind") ||
                newLine.contains("checks") ||
                newLine.contains("folds")) {
            if (
                    newLine.contains("bets") ||
                    newLine.contains("calls") ||
                    newLine.contains("raises") ||
                    newLine.contains("goes All-in") ||
                    newLine.contains("posts Small Blind") ||
                    newLine.contains("posts Big Blind")) {
                st.nextToken(); // burn "dealer:"
                player=st.nextToken();
                if (
                        newLine.contains("bets") ||
                        newLine.contains("calls") ||
                        newLine.contains("raises")) {
                    action=st.nextToken(); // get action
                } else {
                    action=st.nextToken()+st.nextToken();
                    if (!newLine.contains("goes All-in")) action+=st.nextToken();
                }
                amt=Float.parseFloat(st.nextToken());
            } else {
                if (newLine.contains("checks")) {
                    st.nextToken(); // burn "dealer:"
                    player=st.nextToken();
                    action="checks";
                }
                if (newLine.contains("folds")) {
                    st.nextToken(); // burn "dealer:"
                    player=st.nextToken();
                    action="folds";
                }
            }
            System.out.println("Player Message ["+player+","+action+","+amt+"]");
        } else if (
                newLine.contains("is the dealer") ||
                newLine.contains("has left the table") ||
                newLine.contains("has joined the table")) {
            st.nextToken(); // burn "dealer:"
            player=st.nextToken();
            if (newLine.contains("is the dealer")) action="deals";
            if (newLine.contains("has left the table")) action="left";
            if (newLine.contains("has joined the table")) {
                player=st.nextToken();
                action="joined";
            }
            System.out.println("Table Message  ["+player+","+action+"]");
        } else if (
                newLine.contains("Starting a new hand") ||
                newLine.contains("wins the main") ||
                newLine.contains("Dealing") ||
                newLine.contains("shows") ||
                newLine.contains("wins with")) {
            if (newLine.contains("Starting a new hand")) action="newHand";
            if (newLine.contains("wins the main")) action="winsMainPot";
            if (newLine.contains("Dealing")) {
                action="dealing";
                if (!newLine.contains("cards")) {
                    st.nextToken(); // burn "dealer:"
                    st.nextToken(); // burn "Dealing"
                    round=st.nextToken();
                    if (newLine.contains("Flop")) cards=st.nextToken()+" "+st.nextToken()+" "+st.nextToken();
                    if (newLine.contains("Turn")) cards=st.nextToken()+" "+st.nextToken()+" "+st.nextToken()+" "+st.nextToken();
                    if (newLine.contains("River")) cards=st.nextToken()+" "+st.nextToken()+" "+st.nextToken()+" "+st.nextToken()+" "+st.nextToken();
                }
            }
            if (newLine.contains("shows")) action="shows";
            if (newLine.contains("wins with")) action="winsWith";
            System.out.println("Game Message   ["+action+"]"+round+" "+cards);
        } else {
            System.out.println("Unknown Message ["+newLine+"]");
        }
    }
    
    private static void analyzeNewLines(final String newLines) {
        StringTokenizer st=new StringTokenizer(newLines,"\n");
        while(st.hasMoreTokens()) {
            String newLine=st.nextToken();
            if (
                    newLine.contains("Dealing Flop") ||
                    newLine.contains("Dealing Turn") ||
                    newLine.contains("Dealing River") ||
                    newLine.contains("shows") ||
                    newLine.contains("wins with")) newLine+=" "+st.nextToken();
            analyzeNewLine(newLine);
        }
    }
    
    public static void loop() throws Exception {
        for (int i=0;i<60;i++) {
            String newLines=getNewLines();
            if (!newLines.equals("")) analyzeNewLines(newLines);
            r.delay(2000);
        }
    }
    
    
}