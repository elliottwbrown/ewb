package OnlineConnector.prima;

import java.util.StringTokenizer;

public class analyzer extends grabber {
    
    private static int delayBetweenScans=2000;
    
    public static void main(String[] args) throws Exception {
        grabber.calibrate();
        loop();
    }
    
    private static void analyzeNewLine(String newLine) {
        //System.out.print(":"+newLine);
        if (newLine.startsWith(">")) newLine=newLine.substring(1);
        newLine=newLine.trim();
        StringTokenizer st=new StringTokenizer(newLine);
        String player="",action="",cards="",round="";
        float amt=0;
        
        // what type of message is it ?
        try {
            if (
                    newLine.contains("bet") ||
                    newLine.contains("called") ||
                    newLine.contains("raised") ||
                    newLine.contains("went all-in") ||
                    newLine.contains("posted small blind") ||
                    newLine.contains("posted big blind") ||
                    newLine.contains("checked") ||
                    newLine.contains("folded")) {
                if (    newLine.contains("bet") ||
                        newLine.contains("called") ||
                        newLine.contains("raised") ||
                        newLine.contains("went all-in") ||
                        newLine.contains("posted small blind") ||
                        newLine.contains("posted big blind")) {
                    player=st.nextToken();
                    if (    newLine.contains("bet") ||
                            newLine.contains("called") ||
                            newLine.contains("raised")) {
                        action=st.nextToken();
                        st.nextToken();                                         // burn "for"
                    } else if (newLine.contains("went all-in")) {
                        action=st.nextToken()+st.nextToken();
                        st.nextToken();                                         // burn "for"
                    } else if (newLine.contains("posted")) {
                        action=st.nextToken()+st.nextToken()+st.nextToken();
                    }
                    String amtRaw=st.nextToken();
                    amtRaw=amtRaw.replace("(","");
                    amtRaw=amtRaw.replace(")","");
                    amtRaw=amtRaw.replace(",","");
                    amtRaw=amtRaw.replace("$","");
                    amtRaw=amtRaw.trim();
                    amt=Float.parseFloat(amtRaw);
                } else {
                    if (newLine.contains("checked")) {
                        player=st.nextToken();
                        action="checked";
                    }
                    if (newLine.contains("folded")) {
                        player=st.nextToken();
                        action="folded";
                    }
                }
                System.out.println("Player Message  ["+player+","+action+","+amt+"]");
            } else if (
                    newLine.contains("is the dealer") ||
                    newLine.contains("has left the table") ||
                    newLine.contains("has joined the table") ||
                    newLine.contains("has joined the table") ||
                    newLine.contains("shows") ||
                    newLine.contains("mucked") ||
                    newLine.contains("wins")) {
                player=st.nextToken();
                if (newLine.contains("shows")) action="shows";
                if (newLine.contains("mucked")) action="mucked";
                if (newLine.contains("wins")) action="winsWith";
                if (newLine.contains("is the dealer")) action="deals";
                System.out.println("Game Message    ["+action+"]"+round+" "+cards);
            } else if (newLine.contains("Dealing")) {
                action="dealing";
                st.nextToken(); // burn "Dealing"
                st.nextToken(); // burn "the
                round=st.nextToken();
                if (newLine.contains("Hole Cards")) cards="";
                if (newLine.contains("Flop")) cards=st.nextToken();//+" "+st.nextToken();//+" "+st.nextToken();
                if (newLine.contains("turn")) cards=st.nextToken();//+" "+st.nextToken()+" "+st.nextToken();//+" "+st.nextToken();
                if (newLine.contains("river")) cards=st.nextToken();//+" "+st.nextToken()+" "+st.nextToken()+" "+st.nextToken()+" ";//+st.nextToken();
                System.out.println("Game Message    ["+action+"]"+round+" "+cards);
            } else if (newLine.contains("seconds to respond")) {
                System.out.println("Game Message    [time warning]");
            } else if (newLine.contains("starting")) {
                System.out.println("Game Message    [game starting]");
            } else if (newLine.contains("waiting")) {
                System.out.println("Game Message    [waiting]");
            } else if (newLine.contains("sitting out")) {
                System.out.println("Game Message    [sitting out]");
            } else if (newLine.contains("sits out")) {
                System.out.println("Game Message    [sits out]");
            } else if (newLine.contains("will be dealt")) {
                System.out.println("Game Message    [will be dealt]");
            } else if (newLine.contains("posted to play")) {
                System.out.println("Game Message    [posted to play]");
            } else if (newLine.contains("left the table")) {
                System.out.println("Game Message    [left the table]");
            } else if (newLine.contains("Extra chips returned")) {
                System.out.println("Game Message    [Extra chips returned]");
            } else if (newLine.contains("has been disconnected")) {
                System.out.println("Game Message    [has been disconnected]");
            } else if (newLine.contains("Could not start game because not enough people")) {
                System.out.println("Game Message    [Could not start game because not enough people]");
            } else {
                System.out.println("Unknown Message ["+newLine+"]");
            }
        } catch (Exception e) {
            System.out.println("newLine="+newLine);
            e.printStackTrace();
        }
        
    }
    
    private static void analyzeNewLines(final String newLines) {
        StringTokenizer st=new StringTokenizer(newLines,"\n");
        while(st.hasMoreTokens()) {
            String newLine=st.nextToken();
            // these strings come on 2 lines so xconcatenate them ...
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
        for (int i=0;i<200;i++) {
            String newLines=getNewLines();
            //System.out.println(":"+newLines);
            if (!newLines.equals("")) analyzeNewLines(newLines);
            r.delay(delayBetweenScans);
        }
    }
}