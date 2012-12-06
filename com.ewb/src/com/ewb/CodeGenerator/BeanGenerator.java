package com.ewb.CodeGenerator;

/*
 * BeanGenerator.java
 *
 * Created on April 4, 2003, 4:26 PM
 */

import java.io.*;
import java.util.*;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;

public class BeanGenerator {
    
    public BeanGenerator() {
    }
    
    public static void main(String[] args) throws java.lang.Exception {
        
        char fs = File.separatorChar;
        
        // pull bean save path from properties file
        PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle)PropertyResourceBundle.getBundle("com.ewb.ApplicationProperties");
        PropertyResourceBundle beangenPty = (PropertyResourceBundle)PropertyResourceBundle.getBundle(ApplicationProperties.getString("GeneratorProperties"));
        String outputPath = beangenPty.getString("BeanPath");
        
        // set Model properties
        String projectName = ApplicationProperties.getString("ProjectName");
        String tableName = args[0];
        String objectName = args[1]; // name displayed in error and confirmation messages
        String findColumn = args[2];
        String parentTableName = args[3]; // used for 'virtual tables' (NOT views!) that 'piggyback' on another table
        // eg. CREDIT_ORDERS piggybacks on CUSTOMER_ORDERS (and V_CREDIT_ORDERS piggybacks on V_CUSTOMER_ORDERS)
        String entityName = ""; // name of Bean objects stored in request attribute
        String pkTableName = args[4]; // used to extract primary key column name
        
        boolean isView = false;
        
        // start
        System.out.println("   Generating bean for table "+tableName);
        
        // view vs. regular
        if (tableName.substring(0,2).equals("V_")) {
            ///// view
            isView = true;
            
            // entity name
            if (entityName.equals("")) {
                // entity name not specified, use base table name
                entityName = tableName;
            }
            
            // primary key table name
            if (pkTableName.equals("")) {
                // primary key name not specified
                if (!entityName.equals("")) {
                    // entity name specified, use entity name without V_
                    if (entityName.startsWith("V_")) {
                        pkTableName = entityName.substring(2);
                    } else {
                        pkTableName = entityName;
                    }
                } else {
                    // entity name not specified, use base table name without V_
                    if (tableName.startsWith("V_")) {
                        pkTableName = tableName.substring(2);
                    } else {
                        pkTableName = tableName;
                    }
                }
            }
            
            // object name
            if (objectName.equals("")) {
                objectName = tableName.substring(2);
            }
            
        } else {
            ///// regular
            
            // entity name
            if (entityName.equals("")) {
                // entity name not specified, use base table name
                entityName = tableName;
            }
            
            // primary key table name
            if (pkTableName.equals("")) {
                // primary key name not specified
                if (!entityName.equals("")) {
                    // entity name specified, use entity name without V_
                    pkTableName = entityName;
                } else {
                    // entity name not specified, use base table name without V_
                    pkTableName = tableName;
                }
            }
            
            // object name
            if (objectName.equals("")) {
                objectName = tableName;
            }
        }
        
        System.out.println("      tableName=["+tableName+"]  objectName=["+objectName+"]  parentTableName=["+parentTableName+"]  pkTableName=["+pkTableName+"]  entityName=["+entityName+"]");
        
        // get columns
        Vector ColumnList = com.ewb.DB._DBaccess.runQuery("select COLUMN_NAME,DATA_TYPE,NULLABLE from USER_TAB_COLUMNS where TABLE_NAME='"+tableName+"' order by column_name");
        
        // primary key
        Vector pkColumnList = com.ewb.DB._DBaccess.runQuery("select COLUMN_NAME as \"COLUMN_NAME\" from ALL_CONS_COLUMNS where CONSTRAINT_NAME in (select CONSTRAINT_NAME from USER_CONSTRAINTS where CONSTRAINT_TYPE='P' and TABLE_NAME='"+pkTableName+"') order by COLUMN_NAME");
        String pkColumn = "";
        if (pkColumnList.size() > 0) {
            pkColumn = (String) ((Hashtable) pkColumnList.elementAt(0)).get("COLUMN_NAME");
        } else {
            pkColumn = "null";
        }
        System.out.println("      Primary key column = ["+pkColumn+"]");
        
        // output vars
        String FileName = tableName+"Bean.java";
        String CR = new String(new byte[]{13,10});
        String outputFileName = outputPath + fs + FileName;
        File outputFile = new File(outputFileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        PrintWriter out = new PrintWriter(fos);
        java.util.Date now = new java.util.Date();
        String currentTime = com.ewb.Time.Dates.currentDateString("EEE MMM d, yyyy h:mm:ss a");
        
        ///// write to file
        System.out.println("      Writing bean file...");
        StringBuffer buffer = new StringBuffer("");
        
        // ********** header **********
        buffer.append("/**"+CR);
        buffer.append(" * "+FileName+CR);
        buffer.append(" *"+CR);
        buffer.append(" * Project:    "+projectName+CR);
        buffer.append(" * Created on: "+currentTime+CR);
        buffer.append(" *"+CR);
        buffer.append(" * This file defines a java bean to encapsulate an"+CR);
        buffer.append(" * entity. The bean contains data members corresponding to the"+CR);
        buffer.append(" * database tables, data members to help with validation and "+CR);
        buffer.append(" * the following function members:"+CR);
        buffer.append(" *  1 - Accessor Methods : getZZZ() and setZZZ() for each ZZZ data member"+CR);
        buffer.append(" *  2 - "+FileName+" : a constructor"+CR);
        buffer.append(" *  3 - Validate() : validates the data and populates error msgs"+CR);
        buffer.append(" */"+CR);
        buffer.append(CR);
        buffer.append("package "+projectName+".Beans;"+CR);
        buffer.append(CR);
        
        // ********** import **********
        buffer.append("import java.util.*;"+CR);
        buffer.append("import java.io.*;"+CR);
        buffer.append("import java.beans.*;"+CR);
        buffer.append(CR);
        
        // ********** class def **********
        buffer.append("public class "+tableName+"Bean extends ewb.GenericTableBean implements Serializable {"+CR);
        buffer.append(CR);
        
        // ********** member list **********
        for (int c=0; c<ColumnList.size(); c++) {
            String column=(String) ((Hashtable) ColumnList.elementAt(c)).get("COLUMN_NAME");
            buffer.append("    public String "+column+";");
            if (column.equals(pkColumn)) {
                buffer.append(" // primary key");
            }
            buffer.append(CR);
        }
        buffer.append(CR);
        
        // ********** constructor **********
        buffer.append("    public "+tableName+"Bean() {"+CR);
        for (int c=0; c<ColumnList.size(); c++) {
            String column=(String) ((Hashtable) ColumnList.elementAt(c)).get("COLUMN_NAME");
            buffer.append("        "+column+" = \"\";");
            if (column.equals(pkColumn)) {
                buffer.append(" // primary key");
            }
            buffer.append(CR);
        }
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** accessor methods **********
        buffer.append("    /**"+CR);
        buffer.append("     * Accessor methods."+CR);
        buffer.append("     */"+CR);
        for (int c=0; c<ColumnList.size(); c++) {
            String column = (String) ((Hashtable) ColumnList.elementAt(c)).get("COLUMN_NAME");
            buffer.append("    public String get"+column+"() {"+CR);
            buffer.append("        return "+column+";"+CR);
            buffer.append("    }"+CR);
            buffer.append("    public void set"+column+"(String value) {"+CR);
            buffer.append("        "+column+" = HTMLescape(value); "+CR);
            buffer.append("    }"+CR);
        }
        buffer.append(CR);
        
        // ********** set bean method **********
        buffer.append("    /**"+CR);
        buffer.append("     * Set bean from resultset."+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @param (Hastable) resultset"+CR);
        buffer.append("     *"+CR);
        buffer.append("     */"+CR);
        buffer.append("    public void setBean(Hashtable rset)"+CR);
        buffer.append("    throws java.io.IOException,java.sql.SQLException { "+CR);
        buffer.append(CR);
        // list columns
        for (int c=0; c<ColumnList.size(); c++) {
            String column = (String) ((Hashtable) ColumnList.elementAt(c)).get("COLUMN_NAME");
            buffer.append("        set"+column+"( ((String) rset.get(\""+column+"\"))==null ? \"\" : HTMLescape(((String) rset.get(\""+column+"\"))) );"+CR);
        }
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** validation method **********
        buffer.append("    /**"+CR);
        buffer.append("     * Validation."+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @return (boolean) ok"+CR);
        buffer.append("     *"+CR);
        buffer.append("     */"+CR);
        buffer.append("    public boolean validate() {"+CR);
        buffer.append(CR);
        buffer.append("        boolean ok = true;"+CR);
        buffer.append(CR);
        
        // step thru columns
        for (int c=0; c<ColumnList.size(); c++) {
            
            String column = (String) ((Hashtable)ColumnList.elementAt(c)).get("COLUMN_NAME");
            String nullable = (String) ((Hashtable)ColumnList.elementAt(c)).get("NULLABLE");
            String dataType = (String) ((Hashtable)ColumnList.elementAt(c)).get("DATA_TYPE");
            
            buffer.append("        // "+column+CR);
            
            // mandatory entry validator
            if (nullable.equals("N") && !column.equals(pkColumn)) {
                buffer.append("        if (" + column + ".trim().equals(\"\")) {"+CR);
                buffer.append("            errors.put(\"" + column + "\",\"Please enter a valid " + column + ".\");"+CR);
                buffer.append("            ok = false;"+CR);
                buffer.append("        }"+CR);
                buffer.append(CR);
            }
            
            // numeric entry validator
            if (dataType.equals("NUMBER") && !column.equals(pkColumn)) {
                buffer.append("        if (!" + column + ".equals(\"\")) {"+CR);
                buffer.append("            try {"+CR);
                buffer.append("                float i = Float.parseFloat("+column+");"+CR);
                buffer.append("            } catch (Exception e) {"+CR);
                buffer.append("                errors.put(\""+column+"\",\"Please enter a valid "+column+".\");"+CR);
                buffer.append("                ok = false;"+CR);
                buffer.append("            }"+CR);
                buffer.append("        }" + CR);
            }
            
            buffer.append(CR);
        } // end for
        
        buffer.append(CR);
        buffer.append("        return ok;"+CR);
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** end class def **********
        buffer.append("} // end class"+CR);
        
        // finish, output file
        String text = buffer.toString();
        //byte b[] = text.getBytes();
        out.println(text);
        out.close();
        
        System.out.println("   Bean generator finished.");
    }
}
