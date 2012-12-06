package PokerClient.Application.Holdem;

public class MainForm extends javax.swing.JFrame {
    
    public MainForm() {
        initComponents();
    }
    
    public void processGUIEvent(java.awt.event.ActionEvent evt) {}
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        CardPanel = new javax.swing.JPanel();
        holeCard1 = new javax.swing.JLabel();
        holeCard2 = new javax.swing.JLabel();
        communityCard1 = new javax.swing.JLabel();
        communityCard2 = new javax.swing.JLabel();
        communityCard3 = new javax.swing.JLabel();
        communityCard4 = new javax.swing.JLabel();
        communityCard5 = new javax.swing.JLabel();
        oppCard1 = new javax.swing.JLabel();
        oppCard2 = new javax.swing.JLabel();
        myNameLabel = new javax.swing.JLabel();
        oppNameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MainTextArea = new javax.swing.JTextArea();
        MetaButtonPanel = new javax.swing.JPanel();
        ConnectButton = new javax.swing.JButton();
        DisconnectButton = new javax.swing.JButton();
        usernameSign = new javax.swing.JLabel();
        serverComboBox = new javax.swing.JComboBox();
        usernameSign1 = new javax.swing.JLabel();
        ResetButton = new javax.swing.JButton();
        nameTextField = new javax.swing.JTextField();
        PlayButtonPanel = new javax.swing.JPanel();
        CallButton = new javax.swing.JButton();
        CheckButton = new javax.swing.JButton();
        RaiseButton = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        StartButton = new javax.swing.JButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        GameStateButton = new javax.swing.JButton();
        StatusBar = new javax.swing.JLabel();
        InfoPanel = new javax.swing.JPanel();
        potLabel = new javax.swing.JLabel();
        potSign = new javax.swing.JLabel();
        turnLabel = new javax.swing.JLabel();
        turnSign = new javax.swing.JLabel();
        roundLabel = new javax.swing.JLabel();
        roundSign = new javax.swing.JLabel();
        gameIDLabel = new javax.swing.JLabel();
        gameIDSign = new javax.swing.JLabel();
        balanceLabel = new javax.swing.JLabel();
        balanceSign = new javax.swing.JLabel();
        dealerLabel = new javax.swing.JLabel();
        dealerSign = new javax.swing.JLabel();
        smallBlindLabel = new javax.swing.JLabel();
        smallBlindSign = new javax.swing.JLabel();
        bigBlindLabel = new javax.swing.JLabel();
        bigBlindSign = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        MainPanel.setPreferredSize(new java.awt.Dimension(700, 700));

        myNameLabel.setText("My Cards");

        oppNameLabel.setText("Opponent's Cards");

        org.jdesktop.layout.GroupLayout CardPanelLayout = new org.jdesktop.layout.GroupLayout(CardPanel);
        CardPanel.setLayout(CardPanelLayout);
        CardPanelLayout.setHorizontalGroup(
            CardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(CardPanelLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .add(CardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(CardPanelLayout.createSequentialGroup()
                        .add(holeCard1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(holeCard2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(myNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(34, 34, 34)
                .add(communityCard1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(communityCard2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(communityCard3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(communityCard4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(communityCard5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(58, 58, 58)
                .add(CardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(CardPanelLayout.createSequentialGroup()
                        .add(oppCard2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(oppCard1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(oppNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        CardPanelLayout.setVerticalGroup(
            CardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(CardPanelLayout.createSequentialGroup()
                .add(CardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(holeCard2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(communityCard1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(communityCard2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(communityCard3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(communityCard4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(communityCard5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(oppCard2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(oppCard1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(holeCard1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(CardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(myNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(oppNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        MainTextArea.setBackground(new java.awt.Color(204, 204, 204));
        MainTextArea.setColumns(20);
        MainTextArea.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        MainTextArea.setLineWrap(true);
        MainTextArea.setRows(5);
        MainTextArea.setMinimumSize(null);
        MainTextArea.setPreferredSize(new java.awt.Dimension(680, 74));
        jScrollPane1.setViewportView(MainTextArea);

        ConnectButton.setText("Connect");
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectButtonActionPerformed(evt);
            }
        });

        DisconnectButton.setText("Disconnect");
        DisconnectButton.setEnabled(false);
        DisconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisconnectButtonActionPerformed(evt);
            }
        });

        usernameSign.setText("Name:");

        serverComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "localhost", "szilasi", "advent", "compaq", "84.232.53.154" }));

        usernameSign1.setText("Server:");

        ResetButton.setText("Reset");
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });

        nameTextField.setText("elliott");

        org.jdesktop.layout.GroupLayout MetaButtonPanelLayout = new org.jdesktop.layout.GroupLayout(MetaButtonPanel);
        MetaButtonPanel.setLayout(MetaButtonPanelLayout);
        MetaButtonPanelLayout.setHorizontalGroup(
            MetaButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MetaButtonPanelLayout.createSequentialGroup()
                .add(usernameSign1)
                .add(5, 5, 5)
                .add(serverComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ConnectButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(DisconnectButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ResetButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(usernameSign)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(nameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .add(67, 67, 67))
        );
        MetaButtonPanelLayout.setVerticalGroup(
            MetaButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MetaButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(usernameSign1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(serverComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(ConnectButton)
                .add(DisconnectButton)
                .add(ResetButton)
                .add(usernameSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        CallButton.setText("Check/Call");
        CallButton.setEnabled(false);
        CallButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CallButtonActionPerformed(evt);
            }
        });

        CheckButton.setText("Check/Fold");
        CheckButton.setEnabled(false);
        CheckButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckButtonActionPerformed(evt);
            }
        });

        RaiseButton.setText("Bet/Raise");
        RaiseButton.setEnabled(false);
        RaiseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RaiseButtonActionPerformed(evt);
            }
        });

        jRadioButton1.setText("AutoCall");

        StartButton.setText("Post Blind");
        StartButton.setEnabled(false);
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Auto Post");

        GameStateButton.setText("getGameState");
        GameStateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GameStateButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout PlayButtonPanelLayout = new org.jdesktop.layout.GroupLayout(PlayButtonPanel);
        PlayButtonPanel.setLayout(PlayButtonPanelLayout);
        PlayButtonPanelLayout.setHorizontalGroup(
            PlayButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PlayButtonPanelLayout.createSequentialGroup()
                .add(CheckButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(CallButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(RaiseButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(StartButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButton2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(GameStateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        PlayButtonPanelLayout.setVerticalGroup(
            PlayButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PlayButtonPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(CheckButton)
                .add(CallButton)
                .add(RaiseButton)
                .add(jRadioButton1)
                .add(StartButton)
                .add(jRadioButton2)
                .add(GameStateButton))
        );

        StatusBar.setAutoscrolls(true);
        StatusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusBar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        StatusBar.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        potLabel.setText("999");

        potSign.setText("Pot:");

        turnLabel.setDisplayedMnemonic('\u03e7');
        turnLabel.setText("999");

        turnSign.setText("To Play:");

        roundLabel.setText("999");

        roundSign.setText("Round:");

        gameIDLabel.setText("999");

        gameIDSign.setText("Game ID:");

        balanceLabel.setText("999");

        balanceSign.setText("Balance:");

        dealerLabel.setText("0123456789");

        dealerSign.setText("Dealer:");

        smallBlindLabel.setText("0123456789");

        smallBlindSign.setText("Small Blind:");

        bigBlindLabel.setText("0123456789");

        bigBlindSign.setText("Big Blind:");

        org.jdesktop.layout.GroupLayout InfoPanelLayout = new org.jdesktop.layout.GroupLayout(InfoPanel);
        InfoPanel.setLayout(InfoPanelLayout);
        InfoPanelLayout.setHorizontalGroup(
            InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, InfoPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, InfoPanelLayout.createSequentialGroup()
                        .add(turnSign)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(turnLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(InfoPanelLayout.createSequentialGroup()
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(smallBlindSign)
                            .add(bigBlindSign))
                        .add(16, 16, 16)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(bigBlindLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(smallBlindLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(InfoPanelLayout.createSequentialGroup()
                        .add(dealerSign)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(dealerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(roundSign)
                    .add(gameIDSign)
                    .add(potSign)
                    .add(balanceSign))
                .add(11, 11, 11)
                .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(roundLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(gameIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(potLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(balanceLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(70, 70, 70))
        );
        InfoPanelLayout.setVerticalGroup(
            InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(InfoPanelLayout.createSequentialGroup()
                .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(InfoPanelLayout.createSequentialGroup()
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(gameIDSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(gameIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(potSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(potLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
                        .add(6, 6, 6)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(balanceSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(balanceLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(6, 6, 6)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(roundLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(roundSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(InfoPanelLayout.createSequentialGroup()
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(dealerSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(dealerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(6, 6, 6)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(smallBlindLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(smallBlindSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(6, 6, 6)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(bigBlindLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(bigBlindSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(InfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(turnSign, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(turnLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout MainPanelLayout = new org.jdesktop.layout.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MainPanelLayout.createSequentialGroup()
                .add(MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(MainPanelLayout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(CardPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(MetaButtonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 63, Short.MAX_VALUE))
                    .add(MainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(StatusBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                            .add(MainPanelLayout.createSequentialGroup()
                                .add(MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, PlayButtonPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, InfoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(67, 67, 67))))
                    .add(MainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)))
                .addContainerGap())
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MainPanelLayout.createSequentialGroup()
                .add(11, 11, 11)
                .add(MetaButtonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .add(11, 11, 11)
                .add(CardPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(InfoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(PlayButtonPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(StatusBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(MainPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 634, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetButtonActionPerformed
        processGUIEvent(evt);
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void GameStateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GameStateButtonActionPerformed
        processGUIEvent(evt);
    }//GEN-LAST:event_GameStateButtonActionPerformed
    private void RaiseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RaiseButtonActionPerformed
        processGUIEvent(evt);
    }//GEN-LAST:event_RaiseButtonActionPerformed
    private void CheckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckButtonActionPerformed
        processGUIEvent(evt);
    }//GEN-LAST:event_CheckButtonActionPerformed
    private void CallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CallButtonActionPerformed
        processGUIEvent(evt);
    }//GEN-LAST:event_CallButtonActionPerformed
    private void DisconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisconnectButtonActionPerformed
        processGUIEvent(evt);
    }//GEN-LAST:event_DisconnectButtonActionPerformed
    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        processGUIEvent(evt);
    }                                                                                 
    private void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {                                          
        processGUIEvent(evt);//GEN-FIRST:event_ConnectButtonActionPerformed
    }//GEN-LAST:event_ConnectButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton CallButton;
    private javax.swing.JPanel CardPanel;
    public javax.swing.JButton CheckButton;
    public javax.swing.JButton ConnectButton;
    public javax.swing.JButton DisconnectButton;
    public javax.swing.JButton GameStateButton;
    private javax.swing.JPanel InfoPanel;
    public javax.swing.JPanel MainPanel;
    public javax.swing.JTextArea MainTextArea;
    private javax.swing.JPanel MetaButtonPanel;
    private javax.swing.JPanel PlayButtonPanel;
    public static javax.swing.JButton RaiseButton;
    public javax.swing.JButton ResetButton;
    public javax.swing.JButton StartButton;
    public javax.swing.JLabel StatusBar;
    public javax.swing.JLabel balanceLabel;
    private javax.swing.JLabel balanceSign;
    public javax.swing.JLabel bigBlindLabel;
    private javax.swing.JLabel bigBlindSign;
    public javax.swing.JLabel communityCard1;
    public javax.swing.JLabel communityCard2;
    public javax.swing.JLabel communityCard3;
    public javax.swing.JLabel communityCard4;
    public javax.swing.JLabel communityCard5;
    public javax.swing.JLabel dealerLabel;
    private javax.swing.JLabel dealerSign;
    public javax.swing.JLabel gameIDLabel;
    private javax.swing.JLabel gameIDSign;
    public javax.swing.JLabel holeCard1;
    public javax.swing.JLabel holeCard2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel myNameLabel;
    public javax.swing.JTextField nameTextField;
    public javax.swing.JLabel oppCard1;
    public javax.swing.JLabel oppCard2;
    public javax.swing.JLabel oppNameLabel;
    public javax.swing.JLabel potLabel;
    private javax.swing.JLabel potSign;
    public javax.swing.JLabel roundLabel;
    private javax.swing.JLabel roundSign;
    public javax.swing.JComboBox serverComboBox;
    public javax.swing.JLabel smallBlindLabel;
    private javax.swing.JLabel smallBlindSign;
    public javax.swing.JLabel turnLabel;
    private javax.swing.JLabel turnSign;
    private javax.swing.JLabel usernameSign;
    private javax.swing.JLabel usernameSign1;
    // End of variables declaration//GEN-END:variables
}