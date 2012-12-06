package com.ewb.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.driver.OracleDriver;

public class DBAccessOracle {
    public static Connection con = null;
    public static Statement stmt = null;
    public static String url = "",username="", password="";

    public static void main (String args[]) throws Exception {
         url = "jdbc:oracle:thin:@www.theflop.net:1521:XE";
         username="flop";
         password="flop8192";
         initDB();
            ResultSet rs = stmt.executeQuery("select bonus_name,expiry_date from bonuses where round(expiry_date)-round(sysdate) between 1 and 9");
            while (rs.next()) {
                System.out.println("3 day warning: " + rs.getString(1) + "\n");
            }         
    }
  

    public static ResultSet executeQuery(String sql) throws Exception {
        return stmt.executeQuery(sql);
    }

    public static void execute(String sql) throws Exception {
        stmt.execute(sql);
        con.commit();
    }
    
    public static void initDB() throws Exception {
        DriverManager.registerDriver(new OracleDriver());
        Class.forName("oracle.jdbc.OracleDriver");
        con =  DriverManager.getConnection(url, username, password);
        stmt = con.createStatement();
    }
    
    public static void closeDB() throws Exception {
        try {
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    

}
