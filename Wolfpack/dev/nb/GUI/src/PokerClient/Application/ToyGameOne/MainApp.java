package PokerClient.Application.ToyGameOne;

import java.util.PropertyResourceBundle;
import javax.swing.ImageIcon;

import com.ewb.Utilities.XMLUtilities;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.Client.PokerClient;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;
import PokerClient.Application.*;

public class MainApp extends MainForm implements GameValues {
    
    public static void main(String args[]) {
        ma= new MainApp();
        ma.startGUI();
    }
    
    public MainApp() {
        super();
        configuration=(PropertyResourceBundle)PropertyResourceBundle.getBundle("PokerClient.Configuration.GUIProperties");
        cardsLocation=configuration.getString("cardsLocation");
        resetCards();
        resetLabels();
        updateThread=new UpdateThread(this);
        updateThread.setKeepUpdating(false);
        updateThread.start();
    }
    
    public void startGUI() {
        java.awt.EventQueue.invokeLater(
                new Runnable() { public void run() {
                    ma.setVisible(true);}}
        );
        GameStateButton.setVisible(false);
    }
    
    private void resetCards() {
        holeCard1.setIcon(new ImageIcon(cardsLocation+"FD.gif"));
        oppCard1.setIcon(new ImageIcon(cardsLocation+"FD.gif"));
    }
    
    private void resetLabels() {
        roundLabel.setText("");
        //usernameLabel.setText("");
        gameIDLabel.setText("");
        MainTextArea.setText("");
        potLabel.setText("$0");
        turnLabel.setText("");
        dealerLabel.setText("");
        smallBlindLabel.setText("");
        bigBlindLabel.setText("");
        balanceLabel.setText("$1000");
        CheckButton.setEnabled(false);
        CallButton.setEnabled(false);
        RaiseButton.setEnabled(false);
        RaiseButton.setEnabled(false);
    }
    
    public void processGUIEvent(java.awt.event.ActionEvent evt) {
        MainTextArea.setText("");
        String eventName=evt.getActionCommand();
        System.out.println("evt:"+eventName);
        if (eventName=="Connect") {
            ConnectButtonPressed();
        } else if (eventName=="Disconnect") {
            DisconnectButtonPressed();
        } else if (eventName=="Start") {
            StartButtonPressed();
        } else if (eventName==CallButton.getText()) {
            CallButtonPressed();
        } else if (eventName==CheckButton.getText()) {
            CheckFoldButtonPressed();
        } else if (eventName==RaiseButton.getText()) {
            RaiseButtonPressed();
        } else if (eventName==ResetButton.getText()) {
            StatusBar.setText("resetting");
            ResetButtonPressed();
        } else if (eventName=="getGameState") {
            getGameStateButtonPressed();
        } else {
            System.out.println("*** eventName not found");
            System.out.println("eventName '"+eventName+"'");
        }
    }
    
    /***********************************************************************************
     * Button Event Handlers
     */
    public void ConnectButtonPressed() {
        if (nameTextField.getText().length()>4) {
            try {
                client= new PokerClient( (String) serverComboBox.getModel().getSelectedItem(), 
                        (String) configuration.getString("hostPort"));
                MainTextArea.setText(XMLUtilities.getMessageBody(client.getWelcomeAndCookie()));
                StatusBar.setText("Connected");
                disableMetaButtons();
                DisconnectButton.setEnabled(true);
                ResetButton.setEnabled(true);
                JoinButtonPressed();
            } catch (java.net.ConnectException e) {
                StatusBar.setText("Not Connected");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StatusBar.setText("Please enter a username longer than 4 characters.");
        }
    }
    
    public void DisconnectButtonPressed()  {
        try {
            resetCards();
            resetLabels();
            client.sendOperation("leave");
            StatusBar.setText("disconnected");
            disableMetaButtons();
            disablePlayButtons();
            ConnectButton.setEnabled(true);
            MainPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ResetButtonPressed()  {
        try {
            StatusBar.setText("resetting");
            resetCards();
            resetLabels();
            client.sendOperation("reset");
            StatusBar.setText("reset");
            disableMetaButtons2();
            disablePlayButtons();
            StartButton.setEnabled(true);
            ConnectButton.setEnabled(true);
            updateThread.setKeepUpdating(false);
            MainPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void disableMetaButtons2() {
        DisconnectButton.setEnabled(false);
        ConnectButton.setEnabled(false);
        StartButton.setEnabled(true);
        ResetButton.setEnabled(false);
    }
    
    private void disableMetaButtons() {
        DisconnectButton.setEnabled(false);
        ConnectButton.setEnabled(false);
        StartButton.setEnabled(false);
        ResetButton.setEnabled(false);
    }
    
    public void disablePlayButtons() {
        CallButton.setEnabled(false);
        RaiseButton.setEnabled(false);
        CheckButton.setEnabled(false);
    }
    
    public void enablePlayButtons() {
        if (callAmount==0) {
            CheckButton.setText("Check");
            CallButton.setText("Check");
            RaiseButton.setText("Bet "+raiseAmount);
        } else {
            CheckButton.setText("Fold");
            CallButton.setText("Call "+callAmount);
            RaiseButton.setText("Bet "+raiseAmount);
        }
        CheckButton.setEnabled(true);
        CallButton.setEnabled(true);
        RaiseButton.setEnabled(true);
    }
    
    public void JoinButtonPressed()  {
        try {
            System.out.println("client "+client);
            String response=client.sendOperation("join","name="+nameTextField.getText());
            System.out.println("response "+response);
            String msg= XMLUtilities.getMessageBody(response);
            System.out.println("msg "+msg);
            MainTextArea.setText(msg);
            ConnectButton.setEnabled(false);
            StartButton.setEnabled(true);
            StatusBar.setText("joined as "+nameTextField.getText());
            nameTextField.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void StartButtonPressed() {
        try {
            String response=client.sendOperation("start");
            if (response.indexOf("<error>You must have at least 2 players join the game before you start.</error>")>0) {
                StatusBar.setText("not started");
            } else {
                StartButton.setEnabled(false);
                GameStateButton.setEnabled(true);
                updateThread.setKeepUpdating(true);
                getGameStateButtonPressed();
                StatusBar.setText("started");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void CallButtonPressed() {
        System.out.println("Calling");
        try {
            String response=client.sendOperation("call");
            if (response.indexOf("<error>")>0) {
                StatusBar.setText("not called");
            } else {
                disablePlayButtons();
                StatusBar.setText("called");
            }
            getGameStateButtonPressed();
            MainPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void RaiseButtonPressed() {
        System.out.println("Raising");
        try {
            String response=client.sendOperation("raise");
            if (response.indexOf("<error>")>0) {
                StatusBar.setText("not raised");
            } else {
                disablePlayButtons();
                StatusBar.setText("raised");
            }
            getGameStateButtonPressed();
            MainPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void CheckFoldButtonPressed() {
        System.out.println("Checking/Folding");
        try {
            String response=client.sendOperation("checkfold");
            if (response.indexOf("<error>")>0) {
                StatusBar.setText("error");
            } else {
                disablePlayButtons();
                StatusBar.setText("checked/folded");
            }
            getGameStateButtonPressed();
            MainPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getGameStateButtonPressed() {
        getGameState();
        parseGameState();
        drawGameState();
    }
    
    synchronized public void getGameState()  {
        boolean readSuccesfully=false;
        while (!readSuccesfully) {
            try {
                System.out.println(">>> gettingGameState");
                lastResponse=response;
                response=client.sendOperation("getGameState");
                System.out.println(">>> done. Parsing GameState");
                readSuccesfully=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        System.out.println(">>> gettingGameState finished");
    }
    
    public void parseGameState() {
        if (lastResponse!=response) {
            try {
                myName=nameTextField.getText();
                oppName=XMLUtilities.getTag(response,"<name/>");
                if (oppName.equals(myName)) oppName=XMLUtilities.getSecondTag(response,"<name/>",2);
                handStatus=Integer.parseInt(XMLUtilities.getTag(response,"<handStatus/>"));
                balance=Float.parseFloat(XMLUtilities.getTag(response,"<sessionID/>"+client.getSessionID()+"<name/>"+myName+"<balance/>"));
                if (handStatus>HAND_NOT_STARTED) {
                    handHistory=XMLUtilities.getTag(response,"<handHistory/>");
                    if (response.contains("<pot/>")) pot=Float.parseFloat(XMLUtilities.getTag(response,"<pot/>"));
                    else pot=0;
                    if (handStatus==HAND_STARTED) {
                        roundNo=Integer.parseInt(XMLUtilities.getTag(response,"<roundNo/>"));
                        gameID=Integer.parseInt(XMLUtilities.getTag(response,"<gameID/>"));
                        dealer=XMLUtilities.getTag(response,"<dealer/>");
                        smallBlind=XMLUtilities.getTag(response,"<smallBlind/>");
                        bigBlind=XMLUtilities.getTag(response,"<bigBlind/>");
                        currentPlayer=XMLUtilities.getTag(response,"<turnInfo><player/>");
                        if (dealer.equals(client.getSessionID())) dealer=myName;
                        else dealer=oppName;
                        if (smallBlind.equals(client.getSessionID())) smallBlind=myName;
                        else smallBlind=oppName;
                        if (bigBlind.equals(client.getSessionID())) bigBlind=myName;
                        else bigBlind=oppName;
                        if (currentPlayer.equals(client.getSessionID())) currentPlayer=myName;
                        else currentPlayer=oppName;
                        if (response.contains("<cardInfo>")) {
                            holeCards=new Cards();
                            opponentsCards=new Cards();
                            if (roundNo>=DEAL) {
                                holeCards.add(new Card(XMLUtilities.getTag(response,"<card1/>")));
                            }
                            if (roundNo>=END_OF_HAND) {
                                opponentsCards.add(new Card(XMLUtilities.getTag(response,"<card8/>")));
                            }                            
                            
                            callAmount = 0;
                            if (response.contains("<callAmount/>")) callAmount=Math.min(Float.parseFloat(XMLUtilities.getTag(response,"<callAmount/>")),balance);
                            
                            raiseAmount = 0;
                            if (response.contains("<raiseAmount/>")) raiseAmount=Math.min(Float.parseFloat(XMLUtilities.getTag(response,"<raiseAmount/>")),balance);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void drawGameState() {
        if (lastResponse!=response) {
            try {
                balanceLabel.setText("$"+balance);
                potLabel.setText("$"+pot);
                if (handStatus>HAND_NOT_STARTED) {
                    MainTextArea.setText(handHistory);
                    if (handStatus==HAND_STARTED) {
                        if (roundNo==DEAL) resetCards();
                        roundLabel.setText(roundNames[roundNo]);
                        gameIDLabel.setText(""+gameID);
                        StartButton.setEnabled(false);
                        dealerLabel.setText(dealer);
                        smallBlindLabel.setText(smallBlind);
                        bigBlindLabel.setText(bigBlind);
                        turnLabel.setText(currentPlayer);
                        if (response.contains("<turnInfo><player/>"+client.getSessionID())) enablePlayButtons();
                        else disablePlayButtons();
                        if (response.contains("<cardInfo>")) {
                            holeCard1.setIcon(new ImageIcon(cardsLocation+holeCards.get(0).getCardShortestName()+".gif"));
                        }
                    }
                    if (handStatus==HAND_FINISHED) {
                        oppCard1.setIcon(new ImageIcon(cardsLocation+opponentsCards.get(0).getCardShortestName()+".gif"));
                        updateThread.setKeepUpdating(false);
                        StartButton.setEnabled(true);
                    }
                }
                MainPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static final String[] roundNames={"","Deal","Flop","Turn","River","End of Hand"};
    public static String myName,oppName;
    public PokerClient client;
    public volatile String response="",lastResponse,nse="",dealer,smallBlind,bigBlind,currentPlayer,handHistory,cardsLocation="";
    public UpdateThread updateThread=null;
    private PropertyResourceBundle configuration;
    public static MainApp ma;
    public static int handStatus,roundNo,gameID;
    public static float pot,balance,callAmount,raiseAmount;
    public static Cards holeCards,communityCards,opponentsCards;
    
    
}
