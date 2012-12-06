package com.ewb.Utilities;

import com.ewb.DB.DBAccessOracle;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

public class APEX2XMLISO88592 extends SQLtoXMLISO88592 {

    public void writeTableToXML(Statement st, String tableName, String jsfFileLocation) throws Exception {
        ResultSet rs = st.executeQuery("select * from " + tableName);
        while (rs.next()) {
            writeToFile(getDBTable(st, "select * from " + tableName), jsfFileLocation, tableName + ".jsf");
        }
        rs.close();
    }

    public void writeLOVsToXML(DBAccessOracle db, String jsfFileLocation) throws Exception {
        String text = "<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>\n";
        text += "<LOVs>\n";
        ResultSet rs = db.executeQuery("select unique LIST_OF_VALUES_NAME from APEX_APPLICATION_LOV_ENTRIES");
        Vector LOVs = new Vector();
        while (rs.next()) {
            LOVs.add(rs.getString(1));
        }
        Iterator i = LOVs.iterator();
        while (i.hasNext()) {
            String LOV = i.next().toString();
            rs = db.executeQuery("select DISPLAY_VALUE,RETURN_VALUE from APEX_APPLICATION_LOV_ENTRIES where LIST_OF_VALUES_NAME='" + LOV + "'");
            text += "<LOV name=\"" + LOV + "\">\n";
            while (rs.next()) {
                text += "<entry>\n";
                text += "<RETURN_VALUE>" + rs.getString(1) + "</RETURN_VALUE>";
                text += "<DISPLAY_VALUE>" + rs.getString(2) + "</DISPLAY_VALUE>";
                text += "</entry>\n";
            }
            text += "</LOV>\n";
        }
        rs = db.executeQuery("select LIST_OF_VALUES_NAME,LIST_OF_VALUES_QUERY from APEX_APPLICATION_LOVS where LOV_TYPE='Dynamic'");
        LOVs = new Vector();
        Vector LOVnames = new Vector();
        while (rs.next()) {
            LOVs.add(rs.getString("LIST_OF_VALUES_QUERY"));
            LOVnames.add(rs.getString("LIST_OF_VALUES_NAME"));
        }
        for (int c = 0; c < LOVs.size(); c++) {
            text += "<LOV name=\"" + LOVnames.elementAt(c) + "\">\n";
            rs = db.executeQuery(LOVs.elementAt(c) + "");
            while (rs.next()) {
                text += "<entry>\n";
                text += "<RETURN_VALUE>" + rs.getString(1) + "</RETURN_VALUE>";
                text += "<DISPLAY_VALUE>" + rs.getString(2) + "</DISPLAY_VALUE>";
                text += "</entry>\n";
            }
            text += "</LOV>\n";
        }
        rs.close();
        text += "</LOVs>\n";
        writeToFile(text, jsfFileLocation, "LOVs.xml");
    }
}
