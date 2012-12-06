package com.ewb.DB;

import com.ewb.FileSystem.FileAccess;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class SQLtoFile extends FileAccess {

    public static String getHTMLTable(Statement st,String sql) throws IOException, Exception {
        String text = "<table width=\"100%\">\n<tr>";
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData rsm = rs.getMetaData();
        int columnCount = rsm.getColumnCount();
        for (int c = 1; c <= columnCount; c++) {
            String columnName = rsm.getColumnName(c);
            text += "<th>" + columnName + "</th>";
        }
        text += "</tr>\n";
        while (rs.next()) {
            text += "<tr>";
            for (int c = 1; c <= columnCount; c++) {
                String columnName = rsm.getColumnName(c);
                String columnValue = rs.getString(columnName);
                if (columnValue == null) {
                    columnValue = "";
                }
                text += "<td>" + columnValue + "</td>";
            }
            text += "</tr>\n";
        }
        text += "</table>";
        return text;
    }

    public static String getProperties(Statement st,String sql) throws IOException, Exception {
        String text = "";
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData rsm = rs.getMetaData();
        while (rs.next()) {
            text += rs.getString("PROPERTY") + "=" + rs.getString("VALUE") + "\n";
        }
        return text;
    }

}
