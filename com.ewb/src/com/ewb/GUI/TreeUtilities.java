
package com.ewb.GUI;

import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class TreeUtilities {

    public static DefaultMutableTreeNode searchNode(String nodeStr,DefaultMutableTreeNode rootNode) {
        DefaultMutableTreeNode node = null;
        Enumeration e = rootNode.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
          node = (DefaultMutableTreeNode) e.nextElement();
          if (nodeStr.equals(node.getUserObject().toString())) {
            return node;
          }
        }
        return null;
    }

    public static void addChildCount(JTree jTree1) {
        for (int i = 0; i < jTree1.getRowCount(); i++) {
            jTree1.expandRow(i);
            MutableTreeNode node = (MutableTreeNode) jTree1.getPathForRow(i).getLastPathComponent();
            if (node.getChildCount() > 0)
                node.setUserObject(node.toString() + " (" + node.getChildCount() + ")");
        }
    }

    public static int getDescendantCount(MutableTreeNode node) {
        int sum=0;
        if (!node.isLeaf()) {
            Enumeration e=node.children();
            while (e.hasMoreElements()) {
                sum+=getDescendantCount((MutableTreeNode)e.nextElement());
            };
        } else {
            sum=1;
        }
        return sum;
    }

    public static void addDescendantCount(JTree jTree1) {
        System.out.println("addDescendantCount");
        for (int i = 0; i < jTree1.getRowCount(); i++) {
            jTree1.expandRow(i);
            MutableTreeNode node = (MutableTreeNode) jTree1.getPathForRow(i).getLastPathComponent();
            int num=getDescendantCount(node);
            if (num>1) num+=1;
            node.setUserObject(node.toString() + " (" + num + ")");
        }
    }

}
