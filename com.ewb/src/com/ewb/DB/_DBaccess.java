package com.ewb.DB;

/**
 * DBaccess.java
 *
 * Author:			Elliott Brown
 */

import java.io.*;
import java.util.*;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;

public class _DBaccess {
    
    static PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle)PropertyResourceBundle.getBundle("bison.Properties.ApplicationProperties");
    static PropertyResourceBundle dbaccessPty = (PropertyResourceBundle)PropertyResourceBundle.getBundle(ApplicationProperties.getString("DBaccess"));
    static String url = dbaccessPty.getString("url");
    static String username = dbaccessPty.getString("user");
    static String password = dbaccessPty.getString("password");
    static Connection connTx = null;
    
    public _DBaccess()
    throws Exception {
        connTx = DriverManager.getConnection(url,username,password);
        connTx.setAutoCommit(false);
    }
    
    public static Vector runQuery(String SQLString)
    throws Exception {
        
        Vector results = new Vector();
        
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); // for oracle
        Connection conn = DriverManager.getConnection(url,username,password);
        Statement stmt = conn.createStatement();
        System.out.println("SQL Query >>> "+SQLString);
        
        ResultSet rs = stmt.executeQuery(SQLString);
        ResultSetMetaData meta = rs.getMetaData();
        int count=0;
        while (rs.next()) {
            Hashtable row = new Hashtable();
            for (int c=0;c<meta.getColumnCount();c++) {;
            row.put(meta.getColumnName(c+1),(rs.getString(c+1)==null)?"":rs.getString(c+1).trim());
            }
            count++;
            results.addElement(row);
            row=null;
        }
        
        //System.out.println("Returned: "+count+ " rows.");
        rs.close();
        rs=null;
        stmt.close();
        stmt=null;
        conn.close();
        conn=null;
        
        return results;
    } // end method
    
    public static void runCommand(String SQLString)
    throws Exception {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); // for oracle
        Connection conn = DriverManager.getConnection(url,username,password);
        Statement stmt = conn.createStatement();
        //System.out.println("running SQL command:"+SQLString); //for debugging
        stmt.execute(SQLString);
        stmt.close();
        stmt=null;
        conn.close();
        conn=null;
    } // end method
    
    public static Vector runQueryTx(String SQLString)
    throws Exception {
        
        Vector results = new Vector();
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); // for oracle
        Statement stmt = connTx.createStatement();
        
        System.out.println("SQL Query Tx >>> "+SQLString);
        
        ResultSet rs = stmt.executeQuery(SQLString);
        ResultSetMetaData meta = rs.getMetaData();
        int count=0;
        while (rs.next()) {
            Hashtable row = new Hashtable();
            for (int c=0;c<meta.getColumnCount();c++) {;
            row.put(meta.getColumnName(c+1),(rs.getString(c+1)==null)?"":rs.getString(c+1).trim());
            }
            count++;
            results.addElement(row);
            row=null;
        }
        
        System.out.println("Returned: "+count+ " rows.");
        rs.close();
        rs=null;
        stmt.close();
        stmt=null;
        
        return results;
    } // end method
    
    public static void runCommandTx(String SQLString)
    throws Exception {
        Statement stmt = connTx.createStatement();
        
        try {
            //System.out.println("running SQL command in Tx:"+SQLString); //for debugging
            stmt.execute(SQLString);
            System.out.println("Tx command succeeded");
        } catch (Exception e) {
            System.out.println("Tx command failed");
            throw new Exception("execution failed : \n" + e.getMessage());
        } finally {
            stmt.close();
            stmt=null;
        }
    } // end method
    
    public static void runCommandsTx(String[] SQLString)
    throws Exception {
        Statement stmt = connTx.createStatement();
        
        try {
            for (int c=0 ; c<SQLString.length ; c++) {
                //System.out.println("running SQL command:"+SQLString); //for debugging
                stmt.execute(SQLString[c]);
                stmt.close();
                stmt=null;
            }
            connTx.commit();
            System.out.println("Tx commands succeeded");
        } catch (Exception e) {
            System.out.println("Tx commands failed");
            e.printStackTrace();
            connTx.rollback();
        } finally {
            connTx.close();
            connTx=null;
        }
    } // end method
    
    public static void commitTx()
    throws Exception {
        try {
            connTx.commit();
            System.out.println("transaction committed");
        } catch (Exception e) {
            System.out.println("transaction commit failed - rolling back");
            e.printStackTrace();
            connTx.rollback();
        } finally {
            connTx.close();
            connTx=null;
        }
    } // end method
    
    public static void rollbackTx()
    throws Exception {
        System.out.println("transaction failed - rolling back");
        connTx.rollback();
        connTx.close();
        connTx=null;
    } // end method
    
} // end classpublic class DBaccess