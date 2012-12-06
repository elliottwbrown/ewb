package com.ewb.WebCrawler.GUI;

import com.ewb.GUI.TreeUtilities;
import com.ewb.WebCrawler.TOs.CrawlResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class WebCrawlerApplication extends javax.swing.JFrame {

    public static final String startingURL = "http://gimmecasinobonus.com";
    public static final String filename = "c:/temp/model.obj";
    public static final String fileFolder = "c:/temp/crawls";
    public static HashSet urls = new HashSet();
    public static HashMap<String,CrawlResult> crawledResults=null;
    public static int debug = 0;
    public static DefaultMutableTreeNode rootNode = null;
    public static DefaultTreeModel model = null;

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
                //pathNode = new DefaultMutableTreeNode(ct);
                pathNode = new DefaultMutableTreeNode(crawledResult.url);
                model.insertNodeInto(pathNode, parentNode, parentNode.getChildCount());
            }
            model.reload();
            parentNode = pathNode;
        }
    }

    public DefaultTreeModel buildTree( HashMap<String,CrawlResult> crawledResults ) {
        int maxDepth = 0;
        rootNode = new DefaultMutableTreeNode("test");
        model = new DefaultTreeModel(rootNode);
//        Iterator i=crawledResults.keySet().iterator();
//        while (i.hasNext()) {
//            try {
//                CrawlResult crawledResult = crawledResults.get(i.next());
//                String u = crawledResult.toString();
//                URL url = new URL(u);
//                String path = url.getPath();
//                String host = url.getHost();
//                String path2 = path;
//                if (path.length() > 1) {
//                    if (path.substring(path.length() - 1).equals("/")) {
//                        path2 = path.substring(0, path.length() - 1);
//                    }
//                    int depth = path2.replaceAll("[^/]", "").length();
//                    maxDepth = Math.max(depth, maxDepth);
//                }
//                processPath(host, path2, crawledResult, model, rootNode);
//            } catch (MalformedURLException ex) {
//                ex.printStackTrace();
//            }
//        }
        return model;
    }

}
