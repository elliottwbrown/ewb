package com.ewb.Utilities;

import com.ewb.DB.SQLtoFile;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class SQLtoXMLISO88592 extends SQLtoFile {

    public static String getDBTable(Statement st,String sql) throws IOException, Exception {
        String text = "<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>\n";        
        text = "<table width=\"100%\">\n";
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData rsm = rs.getMetaData();
        int columnCount = rsm.getColumnCount();
        text += "<tr>\n";
        for (int c = 1; c <= columnCount; c++) {
            String columnName = rsm.getColumnName(c);
            text += "<th>" + columnName + "</th>\n";
        }
        text += "</tr>\n";
        while (rs.next()) {
            text += "<tr>\n";
            for (int c = 1; c <= columnCount; c++) {
                String columnName = rsm.getColumnName(c);
                String columnValue = rs.getString(columnName);
                if (columnValue == null) {
                    columnValue = "";
                }
                text += "<td>" + columnValue + "</td>\n";
            }
            text += "</tr>\n";
        }
        text += "</table>";
        return escapeFunctions.stringToHTMLString(text);
    }
    
    public static String addRecords(Statement st, String objectName, String tableName, String whereClause) throws Exception {
        String text = "<" + objectName + "Records>\n";
        String sql = "select * from " + tableName;
        if (!whereClause.isEmpty()) {
            sql += " where" + whereClause;
        }
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            text += "<" + objectName + "Record>\n";
            ResultSetMetaData rsm = rs.getMetaData();
            int columnCount = rsm.getColumnCount();
            for (int c = 1; c <= columnCount; c++) {
                String columnName = rsm.getColumnName(c);
                text += "<" + columnName + ">";
                String columnValue = rs.getString(columnName);
                if (columnValue == null) {
                    columnValue = "";
                }
                text += columnValue;
                text += "</" + columnName + ">\n";
            }
            text += "</" + objectName + "Record>\n";
        }
        text += "</" + objectName + "Records>\n";
        return escapeFunctions.stringToHTMLString(text);
    }

    public static String addRecord(Statement st, String pkName, int rec_id, String objectName, String table, String whereClause) throws Exception {
        String text = "<" + objectName + "Record>\n";
        String sql = "select * from " + table + " where " + pkName + " = " + rec_id;
        if (!whereClause.isEmpty()) {
            sql += " and " + whereClause;
        }
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        ResultSetMetaData rsm = rs.getMetaData();
        int columnCount = rsm.getColumnCount();
        for (int c = 1; c <= columnCount; c++) {
            String columnName = rsm.getColumnName(c);
            text += "<" + columnName + ">";
            String columnValue = rs.getString(columnName);
            if (columnValue == null) {
                columnValue = "";
            }
            text += columnValue;
            text += "</" + columnName + ">\n";
        }
        text += "</" + objectName + "Record>\n";
        return escapeFunctions.stringToHTMLString(text);
    }

    public static String addRecord(Statement st, String pkName, int rec_id, String objectName, String table) throws Exception {
        return addRecord(st, pkName, rec_id, objectName, table, "");
    }

    public static String addChildRecord(Statement st, int opr_id, String table, String parent_fk, String whereClause) throws Exception {
        String sql = "";
        sql += "select * from " + table + " where " + parent_fk + "=" + opr_id;
        if (!whereClause.isEmpty()) {
            sql += " and " + whereClause;
        }
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData rsm = rs.getMetaData();
        int columnCount = rsm.getColumnCount();
        String text = "";
        text += "<" + table + ">\n";
        while (rs.next()) {
            text += "<" + table + "Record>\n";
            for (int c = 1; c <= columnCount; c++) {
                String columnName = rsm.getColumnName(c);
                text += "<" + columnName + ">";
                String columnValue = rs.getString(columnName);
                if (columnValue == null) {
                    columnValue = "";
                }
                text += columnValue;
                text += "</" + columnName + ">\n";
            }
            text += "</" + table + "Record>\n";
        }
        text += "</" + table + ">\n";
        return escapeFunctions.stringToHTMLString(text);
    }

    public static String addChildRecord(Statement st, int opr_id, String table, String parent_fk) throws Exception {
        return addChildRecord(st, opr_id, table, parent_fk, "");
    }
}
