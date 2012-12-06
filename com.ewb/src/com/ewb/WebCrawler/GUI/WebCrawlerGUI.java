package com.ewb.WebCrawler.GUI;

import com.ewb.GUI.TreeUtilities;
import com.ewb.Serialization.ObjectSerializer;
import com.ewb.WebCrawler.TOs.CrawlResult;
import com.ewb.WebCrawler.WebCrawler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class WebCrawlerGUI extends javax.swing.JFrame {

    public static final String startingURL = "http://gimmecasinobonus.com";
    public static final String filename = "model.obj";
    public static final String fileFolder = "c:/data/dev/svn_rep_b/theflop/WebCrawler/";
    public static HashSet urls = new HashSet();
    public static HashMap<String,CrawlResult> crawledResults=null;
    public static int debug = 0;
    public static DefaultMutableTreeNode rootNode = null;
    public static DefaultTreeModel model = null;

    public WebCrawlerGUI() {
        initComponents();
        int startRow = 0;
        jTextArea1.setLineWrap(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WebCrawlerGUI cg = new WebCrawlerGUI();
                cg.setVisible(true);
            }
        });
    }

    public static void processPath(String host, String path, CrawlResult crawledResult, DefaultTreeModel model, DefaultMutableTreeNode rootNode) {
        DefaultMutableTreeNode hostNode = TreeUtilities.searchNode(host, rootNode);
        if (hostNode == null) {
            hostNode = new DefaultMutableTreeNode(host);
            model.insertNodeInto(hostNode, rootNode, rootNode.getChildCount());
        }
        StringTokenizer st = new StringTokenizer(path, "/");
        String ct = "";
        DefaultMutableTreeNode parentNode = hostNode;
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            ct += "/" + t;
            DefaultMutableTreeNode pathNode = TreeUtilities.searchNode(ct, hostNode);
            if (pathNode == null) {
                //pathNode = new DefaultMutableTreeNode(t);
                pathNode = new DefaultMutableTreeNode(ct);
                //pathNode = new DefaultMutableTreeNode(crawledResult.url);
                model.insertNodeInto(pathNode, parentNode, parentNode.getChildCount());
            }
            model.reload();
            parentNode = pathNode;
        }
    }

    public DefaultTreeModel buildTree( HashMap<String,CrawlResult> crawledResults ) {
        int maxDepth = 0;
        rootNode = new DefaultMutableTreeNode("Root");
        model = new DefaultTreeModel(rootNode);
        Iterator i=crawledResults.keySet().iterator();
        while (i.hasNext()) {
            try {
                CrawlResult crawledResult = crawledResults.get(i.next());
                String u = crawledResult.toString();
                URL url = new URL(u);
                String path = url.getPath();
                String host = url.getHost();
                String path2 = path;
                if (path.length() > 1) {
                    if (path.substring(path.length() - 1).equals("/")) {
                        path2 = path.substring(0, path.length() - 1);
                    }
                    int depth = path2.replaceAll("[^/]", "").length();
                    maxDepth = Math.max(depth, maxDepth);
                }
                processPath(host, path2, crawledResult, model, rootNode);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return model;
    }

    public void renderTree() {
        System.out.println("renderTree");
        jTree1.setModel(model);
        TreeUtilities.addDescendantCount(jTree1);
        //model.reload();
    }

    public TreeModel getJTreeModel() {
        return jTree1.getModel();
    }

    public static void setJTreeModel(DefaultTreeModel jt) {
        model = jt;
        jt = null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        crawlButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        loadButton = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        crawlButton.setText("Crawl");
        crawlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crawlButtonActionPerformed(evt);
            }
        });

        jTextField1.setText("http://poker.theflop.net"); // NOI18N

        jLabel1.setText("Enter a URL and press the crawl button to start.");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "50", "100", "500", "1000", "5000", "10000" }));

        jSplitPane1.setDividerLocation(400);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextArea1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setViewportView(jPanel2);

        jSplitPane1.setRightComponent(jScrollPane2);

        loadButton.setText("Load");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crawlButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadButton))
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(crawlButton)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(loadButton))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void crawlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crawlButtonActionPerformed
        System.out.println("crawlActionPerformed");
        try {
            String startingURL = jTextField1.getText();
            int numCrawls = Integer.parseInt(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()) + "");

            //crawledResults = WebCrawler.StartCrawl(startingURL, numCrawls);
            System.out.println(crawledResults.toString());
            model = buildTree(crawledResults);

            String fileName = filename;
            try {
                URL url = new URL(jTextField1.getText());
                fileName = fileFolder + "/" + url.getHost() + ".obj";
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            ObjectSerializer.writeObject2File(model, fileName);
            renderTree();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        crawlButton.setEnabled(true);
        jLabel1.setText("Crawl finished.");
    }//GEN-LAST:event_crawlButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        System.out.println("loadActionPerformed");
        renderTree();
        String fileName = filename;
        try {
            URL url = new URL(jTextField1.getText());
            fileName = fileFolder + "/" + url.getHost() + ".obj";
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        model = (DefaultTreeModel) ObjectSerializer.readObjectFromFile(fileName);
        renderTree();
        crawlButton.setEnabled(true);
        jLabel1.setText("Model loaded.");
    }//GEN-LAST:event_loadButtonActionPerformed

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        String msg = "<html>";
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (dmtn != null) {
            Object obj = dmtn.getUserObject();
            System.out.println(obj.toString());
            CrawlResult cr = crawledResults.get(obj.toString());
            msg += "\nURL\t\t" + "<a href=\"" + cr.url + "\">" + cr.url + "</a>";
            msg += "\nLoad Time\t\t" + cr.elapsed + " (ms)";
            msg += "\nPage Size\t\t" + cr.page.length() + " bytes";
            msg += "\nStatus\t\t" + cr.result;
            msg += "\nGoogle PR\t\t" + cr.googlePR;
            msg += "\nPage:\n" + cr.page;
            msg += "</html>";
        }
        jTextArea1.setText(msg);
        jTextArea1.setCaretPosition(0);

        this.repaint();
    }//GEN-LAST:event_jTree1MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton crawlButton;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton loadButton;
    // End of variables declaration//GEN-END:variables
}
