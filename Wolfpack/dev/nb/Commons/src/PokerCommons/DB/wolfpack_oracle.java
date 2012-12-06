package PokerCommons.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.pool.OracleDataSource;

public class wolfpack_oracle {

    public static OracleDataSource ods;
    public static Connection conn;
    public static final int debugLevel = 0;
    public static PreparedStatement[] ps = {null,null,null,null,null};

    public static void main(String args[]) throws Exception {
        System.out.println("start at " + new java.util.Date());
        initDB();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select count(*) from \"SESSION\"");
        rs.next();
        System.out.println(rs.getInt(1));
        conn.close();
        conn = null;
        System.out.println("end at " + new java.util.Date());
    }

    public static void initDB() throws Exception {
        try {
            DriverManager.registerDriver(new OracleDriver());
            ods = new OracleDataSource();
            ods.setDriverType("thin");
            ods.setServerName("theflop.net");            
            ods.setNetworkProtocol("tcp");               
            ods.setDatabaseName("theflop1");
            ods.setPortNumber(1521);
            ods.setUser("wolfpack");
            ods.setPassword("wolfpack42");
            conn = ods.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPreparedStatement(String sql,int n) throws SQLException {
        ps[n] = conn.prepareStatement(sql);
    }

    public static ResultSet executeQuery(String sql) throws Exception {
        Statement st = conn.createStatement();
        if (debugLevel > 1) {
            System.err.println(sql);
        }
        return st.executeQuery(sql);
    }

    synchronized public static void execute(String sql) throws Exception {
        Statement st = null;
        try {
            st = conn.createStatement();
            if (debugLevel > 1) System.out.println("debug:"+sql);
            st.execute(sql);
            conn.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            st.close();
            st = null;
        }
    }

    public static void CloseStatement(ResultSet rs) {
        try {
            Statement stat = rs.getStatement();
            stat.close();
            rs.close();
            stat = null;
            rs = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}