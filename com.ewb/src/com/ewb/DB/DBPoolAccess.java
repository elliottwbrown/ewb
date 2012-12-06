package com.ewb.DB;

import com.bitmechanic.sql.ConnectionPool;
import com.bitmechanic.sql.ConnectionPoolManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.Vector;

/**
 * ConnPool.java
 *
 * Author:			Elliott Brown
 * Project:
 * Created:			February 19, 2003, 9:02 AM
 * Modified:                    February 19, 2003, 9:02 AM
 *
 * (c) Elliott Brown
 *
 **********************************************************************************************
 * This file handles implements a DB connection pool
 *
 **********************************************************************************************
 */
public class DBPoolAccess {

    private ConnectionPoolManager pool;
    private String alias = "jdbcalias2";
    private String driver = "oracle.jdbc.OracleDriver";
    private String maxConnections = "100";
    private String idleTimeout = "10000";
    private String checkoutTimeout = "10000";

    // default
    private PropertyResourceBundle dbaccessPty;
    private String url = "";
    private String username = "";
    private String password = "";
    private String debug = "on";

    public DBPoolAccess(String props) throws Exception {
        dbaccessPty = (PropertyResourceBundle) PropertyResourceBundle.getBundle(props);
        url = dbaccessPty.getString("url");
        username = dbaccessPty.getString("user");
        password = dbaccessPty.getString("password");
        debug = dbaccessPty.getString("debug");

        System.out.println("Instantiating ConnPool.");
        pool = new ConnectionPoolManager(120);
        pool.addAlias(alias, driver, url, username, password,
                Integer.parseInt(maxConnections), Integer.parseInt(idleTimeout),
                Integer.parseInt(checkoutTimeout));
    }

    public Vector runQuery(String command) throws Exception {
        java.util.Date start = null;

        if (debug.equals("on")) {
            start = new java.util.Date();
            System.out.println("start SQL Query at " + start);
            System.out.println();
            System.out.println("" + command);
        }

        Vector results = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData meta = null;

        try {
            // Run a query
            results = new Vector();
            conn = DriverManager.getConnection(ConnectionPoolManager.URL_PREFIX + alias, null, null);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(command);
            meta = rs.getMetaData();

            int count = 0;

            while (rs.next()) {
                Hashtable row = new Hashtable();
                for (int c = 0; c < meta.getColumnCount(); c++) {
                    row.put(meta.getColumnName(c + 1), (rs.getString(c + 1) == null) ? "" : rs.getString(c + 1).trim());
                }
                count++;
                results.addElement(row);
                row = null;
            }
        } catch (Exception e) {
            System.out.println("exception in ConnPool.executeCommand");
            e.printStackTrace();
            throw e;
        } finally {
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
            conn.close();
            conn = null;

            if (debug.equals("on")) {
                java.util.Date end = new java.util.Date();
                System.out.println("finish SQL Query at " + end);
                long duration = (end.getTime() - start.getTime());
                System.out.println("    took: " + duration + " ms");
                System.out.println();
            }
        }
        return results;
    }

    public void runCommand(String command) throws Exception {
        if (debug.equals("on")) {
            System.out.println("SQL Command >>> " + command);
        }

        Vector results = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            // Run a query
            results = new Vector();
            conn = DriverManager.getConnection(ConnectionPoolManager.URL_PREFIX + alias, null, null);
            stmt = conn.createStatement();
            stmt.execute(command);
        } catch (Exception e) {
            System.out.println("exception in ConnPool.executeCommand");
            e.printStackTrace();
            throw e;
        } finally {
            stmt.close();
            stmt = null;
            conn.close();
            conn = null;
        }
    }

    public String printStatistics() throws SQLException {

        ConnectionPool p = pool.getPool(alias);

        String msg = "" +
                "<br> Pool Statistics: " +
                "<br>   Current size: " + p.size() + " of " + p.getMaxConn() +
                "<br>   Connection requests: " + p.getNumRequests() +
                "<br>   Number of waits: " + p.getNumWaits() +
                "<br>   Number of timeouts: " + p.getNumCheckoutTimeouts() +
                "<br> " +
                "<br><a href='javascript:history.go(-1);'>Go Back</a>";

        return msg;
    }
}
