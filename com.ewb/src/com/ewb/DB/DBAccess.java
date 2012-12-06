package com.ewb.DB;

import java.sql.*;
import java.util.*;

public class DBAccess {

    private static int debugLevel = 1;
    private static boolean handleExceptions = false;
    public static PreparedStatement ps = null;

    public DBAccess(String db) throws Exception {
        dbaccessPty = (PropertyResourceBundle) PropertyResourceBundle.getBundle(db);
    }

    public DBAccess() throws Exception {
        ApplicationProperties = (PropertyResourceBundle) PropertyResourceBundle.getBundle("ewb.ApplicationProperties");
        dbaccessPty = (PropertyResourceBundle) PropertyResourceBundle.getBundle(ApplicationProperties.getString("DefaultDBAccess"));
    }

    public static void setApplicationProperties(PropertyResourceBundle ap) throws Exception {
        ApplicationProperties=ap;
        dbaccessPty = (PropertyResourceBundle) PropertyResourceBundle.getBundle(ApplicationProperties.getString("DefaultDBAccess"));
    }

    public static void InitConnection() throws Exception {
        System.out.println("> Connecting to DB ...");
        url = dbaccessPty.getString("url");
        username = dbaccessPty.getString("user");
        password = dbaccessPty.getString("password");
        driver = dbaccessPty.getString("driver");
        dsn = dbaccessPty.getString("dsn");
        if (debugLevel > 0) {
            System.out.println("> Connecting to DB ...");
            System.out.println(">> url=" + url);
            System.out.println(">> dsn=" + dsn);
            System.out.println(">> driver=" + driver);
            System.out.println(">> user=" + username);
            System.out.println(">> password=" + password);
        }
        try {
            Class.forName(driver);
            //String fullUrl=url+dsn+"?user="+username+"&password="+password;
            //conn = DriverManager.getConnection(fullUrl);
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("> Connected.");
        } catch (Exception e) {
            System.out.println("> Not Connected.");
            e.printStackTrace();
            throw e;
        }
    }

    public static void ReleaseConnection() throws Exception {
        conn.close();
        conn = null;
    }

    public static void InitPreparedStatement(String psString) throws Exception {
        ps = conn.prepareStatement(psString);
    }

    public static Connection getConnection() throws Exception {
        return conn;
    }

    public static ResultSet runQuery(String command) throws Exception {
        //command=escape(command);
        if (debugLevel > 1) {
            System.out.println("> Executing Query:" + command);
        }
        ResultSet rs = null;
        Statement readStmt = null;
        try {
            readStmt = conn.createStatement();
            rs = readStmt.executeQuery(command);
        } catch (SQLException e) {
            processSQLException(command, e);
        }

        return rs;
    }

    public static void runCommand(String command) throws Exception {
        //command=escape(command);
        if (debugLevel > 1) {
            System.out.println("> Executing Command:" + command);
        }
        boolean result = true;
        Statement writeStmt = null;
        try {
            writeStmt = conn.createStatement();
            result = writeStmt.execute(command);
        } catch (SQLException e) {
            processSQLException(command, e);
        } finally {
            writeStmt.close();
            writeStmt = null;
        }
    }

    public static String getVar(String command) throws Exception {
        if (debugLevel > 1) {
            System.out.println("> Executing Query:" + command);
        }
        ResultSet rs = null;
        String obj = null;
        Statement readStmt = null;
        try {
            readStmt = conn.createStatement();
            rs = readStmt.executeQuery(command);
            rs.next();
            obj = rs.getString(1);
        } catch (SQLException e) {
            System.out.println("Error trapped in getVar");
            processSQLException(command, e);
        } finally {
            rs.close();
            rs = null;
            readStmt.close();
            readStmt = null;
        }
        return obj;
    }

    private static void processSQLException(final String command, final SQLException e) throws Exception {
        if (handleExceptions) {
            StackTraceElement[] ste = e.getStackTrace();
            for (int i = 0; i < ste.length; i++) {
                if (ste[i].getClassName().equals("com.ewb.DB.DBAccess")) {
                    System.err.println("DBAccess exception trapped in " + ste[i].getMethodName());
                }
            }
            System.out.println("Command: " + command);
            int errorCode = e.getErrorCode();
            if (errorCode == 17011) {
                System.err.println("Caused: Exhausted ResultSet");
            } else if (errorCode == 1000) {
                System.err.println("Caused: ORA-01000: maximum open cursors exceeded");
                throw e;
            } else {
                e.printStackTrace();
                System.err.println("Caused SQL error: " + e.getErrorCode());
                throw e;
            }
        } else {
            throw e;
        }
    }

    public static Vector ResultSet2Vector(ResultSet rs) throws Exception {
        Vector v = new Vector();
        ResultSetMetaData md = rs.getMetaData();

        while (rs.next()) {
            HashMap hm = new HashMap();
            for (int col = 1; col <= md.getColumnCount(); col++) {
                hm.put(md.getColumnName(col), rs.getObject(col));
            }
            v.add(hm);
            hm = null;
        }

        rs.close();
        return v;
    }

    public static void commit() throws Exception {
        if (debugLevel > 0) {
            System.out.println("> Executing Commit.");
        }
        conn.commit();
    }

    public static String escape(String s) {
        String retvalue = s.trim();
        if (s.indexOf("'") != -1) {
            StringBuffer hold = new StringBuffer();
            char c;
            for (int i = 0; i < s.length(); i++) {
                if ((c = s.charAt(i)) == '\'') {
                    hold.append("''");
                } else {
                    hold.append(c);
                }
            }
            retvalue = hold.toString();
        }

        return retvalue;

    }
    private static Connection conn;
    private static PropertyResourceBundle ApplicationProperties;
    private static PropertyResourceBundle dbaccessPty;
    private static String driver,  url,  username,  password,  dsn;
}
