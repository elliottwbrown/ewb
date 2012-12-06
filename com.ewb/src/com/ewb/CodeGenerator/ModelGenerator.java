/**
 * ModelGenerator.java
 *
 * Created on April 4, 2003, 4:26 PM
 */

package com.ewb.CodeGenerator;
import java.io.*;
import java.util.*;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
public class ModelGenerator {
    
    public ModelGenerator() {
    }
    
    public static void main(String[] args) throws java.lang.Exception {
        
        // pull Bean save path from properties file
        PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle)PropertyResourceBundle.getBundle("com.ewb.ApplicationProperties");
        PropertyResourceBundle beangenPty = (PropertyResourceBundle)PropertyResourceBundle.getBundle(ApplicationProperties.getString("GeneratorProperties"));
        String outputPath = beangenPty.getString("ModelPath");
        
        // set Model properties
        String projectName = ApplicationProperties.getString("ProjectName");
        String tableName = args[0];
        String objectName = args[1]; // name displayed in error and confirmation messages
        String findColumn = args[2];
        String entityName = args[3]; // entity to be used
        // eg. CREDIT_ORDERS uses the CUSTOMER_ORDERS entity (and V_CREDIT_ORDERS uses the V_CUSTOMER_ORDERS entity)
        // CREDIT_ORDERS has its own Beans, but these are functionally identical to CUSTOMER_ORDERS Beans
        // if not specified, the base table entity will be used
        
        String pkTableName = args[4]; // primary key source for an object
        // eg. V_CATALOG_ITEMS_ORDERS uses the primary key from CATALOG_ITEMS
        // if not specified, the entity name will be used for the primary key
        // if not specified, and the entity name is also not specified, the base table name will be used for the primary key
        
        String jspName = args[5]; // used to specify the JSP folder and filename(s)
        // eg. V_ORDERS uses the V_CUSTOMER_ORDERS JSPs
        // if not specified, the base table name will be used for JSP naming
        
        // examples:
        //
        //ACCOUNTS
        // - Entity with its own DB table.
        // - Uses the ACCOUNTS entity (Beans)
        // - Uses the ACCOUNTS primary key
        // - Uses the ACCOUNTS JSPs
        //
        // V_ACCOUNTS
        // - View with its own DB view, and a related non-view DB table (ACCOUNTS)
        // - Uses the V_ACCOUNTS entity (Beans) - no entity name specified (uses base table name 'V_ACCOUNTS' by default)
        // - Uses the V_ACCOUNTS primary key - no primary key table name specified (uses base
        //                                     table name 'V_ACCOUNTS' by default)
        // - Uses the ACCOUNTS JSPs - no jsp name specified; the V_ is automatically removed from the
        //                            base table name 'V_ACCOUNTS' to obtain the JSP name 'ACCOUNTS'
        //                            (JSP always have non-'V_' names)
        //
        // V_CREDIT_ORDERS
        // - View with its own DB view, but does not have a related non-view DB table (there is no CREDIT_ORDERS table)
        // - Does not have its own Beans, but uses V_CUSTOMER_ORDERSBeans instead.
        // - Since there is no related non-view DB table, uses another view for its primary key.
        // - Has its own JSPs.
        // - Uses the V_CUSTOMER_ORDERS entity (Beans) - entity name is specified as V_CUSTOMER_ORDERS
        // - Uses the V_CUSTOMER_ORDERS primary key - no primary key table name is specified; the primary
        //                                            key from the entity table (V_CUSTOMER_ORDERS) is used
        // - Uses the CREDIT_ORDERS JSPs - no JSP name is specified
        //
        // V_CATALOG_ITEMS_ORDERS
        // - View with its own DB view, but does not have a related non-view DB table (there is
        //   no CATALOG_ITEMS_ORDERS table).
        // - Has its own Beans, but uses another table for its primary key.
        // - Uses the V_CATALOG_ITEMS_ORDERS entity (Beans)
        // - Uses the CATALOG_ITEMS primary key - the primary key table name is specified as CATALOG_ITEMS
        // - Uses the CATALOG_ITEMS_ORDERS JSPs - no JSP name is specified; the V_ is automatically
        //                                        removed to obtain the JSP name
        //
        // V_ORDERS
        // - View with its own DB view, but does not have a related non-view DB table (there is
        //   no ORDERS table).
        // - Does not have its own Beans, but uses V_CUSTOMER_ORDERSBean instead.
        // - Since there is no related non-view DB table, uses another table for its primary key.
        // - Uses the V_CUSTOMER_ORDERS entity (Beans)
        // - Uses the CUSTOMER_ORDER primary key
        // - Uses the ORDERS JSPs
        
        boolean isView = false;
        
        // start
        System.out.println("   Generating model for table "+tableName);
        
        // view vs. regular
        if (tableName.substring(0,2).equals("V_")) {
            ///// view
            isView = true;
            
            // entity name
            if (entityName.equals("")) {
                // entity name not specified, use base table name
                entityName = tableName;
            }
            
            
            // jsp name
            if (jspName.equals("")) {
                // jsp name not specified, use base table name without V_
                if (tableName.startsWith("V_")) {
                    jspName = tableName.substring(2);
                } else {
                    jspName = tableName;
                }
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
            
            // jsp name
            if (jspName.equals("")) {
                // jsp name not specified, use base table name
                jspName = tableName;
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
        
        System.out.println("      tableName=["+tableName+"]  objectName=["+objectName+"]  entityName=["+entityName+"]  pkTableName=["+pkTableName+"]  jspName=["+jspName+"]");
        
        // get DB tables, columns
        Vector tableList = com.ewb.DB._DBaccess.runQuery("select TABLE_NAME from USER_TABLES");
        Vector columnList = com.ewb.DB._DBaccess.runQuery("select COLUMN_NAME,NULLABLE,DATA_TYPE from USER_TAB_COLUMNS where TABLE_NAME='"+tableName+"' order by COLUMN_NAME");
        
        // primary key
        Vector pkColumnList = com.ewb.DB._DBaccess.runQuery("select COLUMN_NAME as \"COLUMN_NAME\" from ALL_CONS_COLUMNS where CONSTRAINT_NAME in (select CONSTRAINT_NAME from USER_CONSTRAINTS where CONSTRAINT_TYPE='P' and TABLE_NAME='"+pkTableName+"') order by COLUMN_NAME");
        String pkColumn = "";
        if (pkColumnList.size() > 0) {
            pkColumn = (String) ((Hashtable) pkColumnList.elementAt(0)).get("COLUMN_NAME");
        } else {
            pkColumn = "null";
        }
        System.out.println("      Primary key column=["+pkColumn+"]");
        
        // output vars
        char fs = File.separatorChar;
        String FileName = tableName + "Model.java";
        String CR = new String(new byte[]{13,10});
        String outputFileName = outputPath + fs + FileName;
        File outputFile = new File(outputFileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        PrintWriter out = new PrintWriter(fos);
        java.util.Date now = new java.util.Date();
        String currentTime = com.ewb.Time.Dates.currentDateString("EEE MMM d, yyyy h:mm:ss a");
        
        ///// write to file
        System.out.println("      Writing model file...");
        StringBuffer buffer = new StringBuffer("");
        
        // ********** header **********
        buffer.append("/**"+CR);
        buffer.append(" * "+FileName+CR);
        buffer.append(" *"+CR);
        buffer.append(" * Project:    "+projectName+CR);
        buffer.append(" * Created on: "+currentTime+CR);
        buffer.append(" *"+CR);
        buffer.append(" * This class defines methods to manipulate ");
        if (isView) {
            buffer.append("view");
        } else if (pkColumn.equals("null")) {
            buffer.append("join table");
        } else {
            buffer.append("table");
        }
        buffer.append(" "+tableName+"."+CR);
        buffer.append(" */"+CR);
        buffer.append(CR);
        buffer.append("package "+projectName+".Models;"+CR);
        buffer.append(CR);
        
        // ********** import **********
        buffer.append("import java.io.*;"+CR);
        buffer.append("import java.util.*;"+CR);
        buffer.append("import java.sql.*;"+CR);
        buffer.append("import javax.servlet.*;"+CR);
        buffer.append("import javax.servlet.http.*;"+CR);
        buffer.append("import java.math.*;"+CR);
        buffer.append("import ewb.DBPoolAccess.*;"+CR);
        buffer.append(CR);
        
        // ********** class def **********
        buffer.append("public class "+tableName+"Model extends ewb.GenericModel {"+CR);
        buffer.append(CR);
        
        // ********** baseSelectQuery **********
        buffer.append("    // basic select query"+CR);
        buffer.append("    static String baseSelectQuery = \"select ");
        // step thru columns
        for (int c=0; c<columnList.size(); c++) {
            String column = (String) ((Hashtable) columnList.elementAt(c)).get("COLUMN_NAME");
            String data_type = (String)((Hashtable)columnList.elementAt(c)).get("DATA_TYPE");
            if (data_type.equals("DATE")) {
                buffer.append("to_char("+column+",'YYYY-MM-DD-HH24-MI-SS') as \\\""+column+"\\\"");
            } else {
                buffer.append(column);
            }
            if ( c < (columnList.size()-1) ) {
                buffer.append(",");
            }
        }
        buffer.append(" from "+tableName+" \";"+CR);
        buffer.append(CR);
        
        // ********** constructor **********
        buffer.append("    public "+tableName+"Model() throws Exception {"+CR);
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** getBaseSelectQuery **********
        buffer.append("    /**"+CR);
        buffer.append("     * Returns base select query."+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @return (String) baseSelectQuery"+CR);
        buffer.append("     */"+CR);
        buffer.append("    public static String getBaseSelectQuery()"+CR);
        buffer.append("    throws Exception {"+CR);
        buffer.append("        return baseSelectQuery;"+CR);
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** insert **********
        if (!isView) {
            buffer.append("    /**"+CR);
            buffer.append("     * Inserts Bean into DB."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @return (boolean) success"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.(bison.Beans."+entityName+"Bean)) Current"+entityName+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Sets:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) PKID"+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static boolean insert(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("        "+projectName+".Beans."+entityName+"Bean Current"+entityName+" = ("+projectName+".Beans."+entityName+"Bean) req.getAttribute(\"Current"+entityName+"\");"+CR);
            buffer.append(CR);
            buffer.append("        try {"+CR);
            buffer.append("            // assemble query"+CR);
            
            // list columns
            buffer.append("            String SQLString=\"insert into "+tableName+" (\"+"+CR);
            buffer.append("            \"");
            for (int c=0; c<columnList.size(); c++) {
                String column=(String) ((Hashtable) columnList.elementAt(c)).get("COLUMN_NAME");
                if (!column.equals(pkColumn)) {
                    buffer.append(column + ",");
                }
            }
            buffer.delete(buffer.length()-1,buffer.length()); // remove final comma
            
            buffer.append("\"+"+CR);
            buffer.append("            \") values (\"+");
            buffer.append(CR);
            
            // list values
            for (int c=0; c<columnList.size(); c++) {
                String column=(String) ((Hashtable) columnList.elementAt(c)).get("COLUMN_NAME");
                String data_type = (String)((Hashtable)columnList.elementAt(c)).get("DATA_TYPE");
                if (!column.equals(pkColumn)) {
                    if (data_type.equals("DATE")) {
                        buffer.append("            \"to_date('\"+Current"+entityName+".get"+column+"()+\"','YYYY-MM-DD-HH24-MI-SS'),\"+"+CR);
                    } else {
                        buffer.append("            \"'\"+escape(Current"+entityName+".get"+column+"())+\"',\"+"+CR);
                    }
                }
            }
            buffer.delete(buffer.length()-5,buffer.length()-4); // remove final comma
            
            buffer.append("            \")\";"+CR);
            buffer.append(CR);
            buffer.append("            // insert"+CR);
            buffer.append("            MyPool.runCommand(SQLString);"+CR);
            if (!pkColumn.equals("null")) {
                buffer.append(CR);
                buffer.append("            // get primary key"+CR);
                buffer.append("            Vector rset = MyPool.runQuery(\"select max("+pkColumn+") as "+pkColumn+" from "+tableName+"\");"+CR);
                buffer.append("            String PKID = (String) ((Hashtable) rset.elementAt(0)).get(\""+pkColumn+"\");"+CR);
                buffer.append("            req.setAttribute(\"PKID\",PKID);"+CR);
                buffer.append(CR);
                buffer.append("            // update Bean in request w/ primary key"+CR);
                buffer.append("            Current"+tableName+".set"+pkColumn+"(PKID);"+CR);
                buffer.append("            req.setAttribute(\"Current"+tableName+"\", Current"+tableName+");"+CR);
            }
            buffer.append("        }"+CR);
            buffer.append("        catch (Exception e) {"+CR);
            buffer.append("            return false;"+CR);
            buffer.append("        }"+CR);
            buffer.append(CR);
            buffer.append("        return true;"+CR);
            buffer.append(CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** update **********
        if (!isView && !pkColumn.equals("null")) {
            buffer.append("    /**"+CR);
            buffer.append("     * Updates Bean in DB."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * TO BE RENAMED TO update()"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @return (boolean) success"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.(bison.Beans."+entityName+"Bean)) Current"+entityName+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static boolean update(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("        "+projectName+".Beans."+entityName+"Bean Current"+entityName+" = ("+projectName+".Beans."+entityName+"Bean) req.getAttribute(\"Current"+entityName+"\");"+CR);
            buffer.append(CR);
            buffer.append("        try {"+CR);
            buffer.append("            // assemble query"+CR);
            buffer.append("            String SQLString=\"update "+tableName+" set \"+");
            buffer.append(CR);
            
            // list columns
            for (int c=0; c<columnList.size(); c++) {
                String column=(String) ((Hashtable) columnList.elementAt(c)).get("COLUMN_NAME");
                String data_type = (String)((Hashtable)columnList.elementAt(c)).get("DATA_TYPE");
                if (!column.equals(pkColumn)) {
                    if (data_type.equals("DATE")) {
                        buffer.append("            \""+column+"=to_date('\"+ Current"+entityName+".get"+column+"()"+"+\"','YYYY-MM-DD-HH24-MI-SS')");
                    } else {
                        buffer.append("            \""+column+"='\"+escape(Current"+entityName+".get"+column+"())"+"+\"'");
                    }
                    buffer.append(",\"+"+CR);
                }
            }
            buffer.delete(buffer.length()-5,buffer.length()-4); // remove final comma
            
            buffer.append("            \" where "+pkColumn+"=\"+escape(Current"+entityName+".get"+pkColumn+"())"+";"+CR);
            buffer.append(CR);
            buffer.append("            // update"+CR);
            buffer.append("            MyPool.runCommand(SQLString);"+CR);
            buffer.append(CR);
            buffer.append("            return true;"+CR);
            buffer.append("        }"+CR);
            buffer.append("        catch (Exception e) {"+CR);
            buffer.append("            return false;"+CR);
            buffer.append("        }"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** add **********
        if (!isView) {
            buffer.append("    /**"+CR);
            buffer.append("     * Adds "+objectName+"."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @return (boolean) success"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.(bison.Beans."+entityName+"Bean)) Current"+entityName+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Sets:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) PageMessage"+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static boolean add(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("       "+projectName+".Beans."+entityName+"Bean Current"+entityName+" = ("+projectName+".Beans."+entityName+"Bean) req.getAttribute(\"Current"+entityName+"\");"+CR);
            buffer.append(CR);
            buffer.append("        if (Current"+entityName+".validate()) {"+CR);
            buffer.append("            // pass validation"+CR);
            buffer.append("            if ( insert(req,res) ) {"+CR);
            buffer.append("                // success"+CR);
            buffer.append("                req.setAttribute(\"PageMessage\",\""+objectName+" added.\");"+CR);
            
            // regular table vs. join table
            if (!pkColumn.equals("null")) {
                buffer.append(CR);
                // reload page
                buffer.append("                // reload"+CR);
                buffer.append("                Current"+entityName+" = ("+projectName+".Beans."+entityName+"Bean) req.getAttribute(\"Current"+entityName+"\");"+CR);
                buffer.append("                returnLocation = \"/servlet/FrontController?op=get"+tableName+"ByID&"+pkColumn+"=\"+Current"+entityName+".get"+pkColumn+"();"+CR);
            } else {
                // message
                buffer.append("                returnLocation = \"/inside/Message.jsp\";"+CR);
            }
            
            buffer.append("                return true;"+CR);
            buffer.append("            } else {"+CR);
            buffer.append("                // error...show view"+CR);
            buffer.append("                req.setAttribute(\"PageMessage\",\"An error occured while trying to add "+objectName+".  Please contact the administrator.\");"+CR);
            buffer.append("                returnLocation = \"/inside/"+jspName+"/Add"+jspName+".jsp\";"+CR);
            buffer.append("                return false;"+CR);
            buffer.append("            }"+CR);
            buffer.append(CR);
            buffer.append("        } else {"+CR);
            buffer.append("            // fail validation...show view"+CR);
            buffer.append("            req.setAttribute(\"PageMessage\",\"Please correct any errors before saving.\");"+CR);
            buffer.append("            returnLocation = \"/inside/"+jspName+"/Add"+jspName+".jsp\";"+CR);
            buffer.append("            return false;"+CR);
            buffer.append("        }"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** edit **********
        if (!isView && !pkColumn.equals("null")) {
            buffer.append("    /**"+CR);
            buffer.append("     * Edits "+objectName+"."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @return (boolean) success"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.(bison.Beans."+entityName+"Bean)) Current"+entityName+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Sets:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) PageMessage"+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static boolean edit(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("         "+projectName+".Beans."+entityName+"Bean Current"+entityName+" = ("+projectName+".Beans."+entityName+"Bean) req.getAttribute(\"Current"+entityName+"\");"+CR);
            buffer.append(CR);
            buffer.append("        if (Current"+entityName+".validate()) {"+CR);
            buffer.append("            // pass validation"+CR);
            buffer.append("            if ( update(req,res) ) {"+CR);
            buffer.append("                // success"+CR);
            buffer.append("                req.setAttribute(\"PageMessage\",\""+objectName+" updated.\");"+CR);
            buffer.append(CR);
            buffer.append("                // reload"+CR);
            buffer.append("                Current"+entityName+" = ("+projectName+".Beans."+entityName+"Bean) req.getAttribute(\"Current"+entityName+"\");"+CR);
            buffer.append("                returnLocation = \"/servlet/FrontController?op=get"+tableName+"ByID&"+pkColumn+"=\"+Current"+entityName+".get"+pkColumn+"();"+CR);
            buffer.append("                return true;"+CR);
            buffer.append("             } else {"+CR);
            buffer.append("                // error...show view"+CR);
            buffer.append("                req.setAttribute(\"PageMessage\",\"An error occured while trying to edit "+objectName+".  Please contact the administrator.\");"+CR);
            buffer.append("                returnLocation = \"/inside/"+jspName+"/Edit"+jspName+".jsp\";"+CR);
            buffer.append("                return false;"+CR);
            buffer.append(CR);
            buffer.append("            }"+CR);
            buffer.append(CR);
            buffer.append("        } else {"+CR);
            buffer.append("            // fail validation...show view"+CR);
            buffer.append("            req.setAttribute(\"PageMessage\",\"Please correct any errors before saving.\");"+CR);
            buffer.append("            returnLocation = \"/inside/"+jspName+"/Edit"+jspName+".jsp\";"+CR);
            buffer.append("            return false;"+CR);
            buffer.append("        }"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** delete **********
        if (!isView) {
            buffer.append("    /**"+CR);
            buffer.append("     * Delete."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * optional (HttpServletRequest.parameter.String) "+pkColumn+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) "+pkColumn+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static void delete(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception { "+CR);
            buffer.append(CR);
            
            // get primary key from request if regular table
            if (!pkColumn.equals("null")) {
                buffer.append("        // get primary key from request"+CR);
                buffer.append("        String PKID = \"\";"+CR);
                buffer.append("        if ( req.getParameter(\""+pkColumn+"\")!=null && !req.getParameter(\""+pkColumn+"\").equals(\"\") ) {"+CR);
                buffer.append("            PKID = (String) req.getParameter(\""+pkColumn+"\");"+CR);
                buffer.append("        } else if ( req.getAttribute(\""+pkColumn+"\")!=null && !req.getAttribute(\""+pkColumn+"\").equals(\"\") ) {"+CR);
                buffer.append("            PKID = (String) req.getAttribute(\""+pkColumn+"\");"+CR);
                buffer.append("        }"+CR);
                buffer.append(CR);
            }
            buffer.append("        // delete"+CR);
            buffer.append("        String SQLString=\"delete from "+tableName+" where \"+"+CR);
            
            // regular table vs. join table
            if (!pkColumn.equals("null")) {
                // primary key
                buffer.append("        \""+pkColumn+"=\"+PKID;"+CR);
            } else {
                // list columns
                for (int c=0; c<columnList.size(); c++) {
                    String column = (String) ((Hashtable) columnList.elementAt(c)).get("COLUMN_NAME");
                    buffer.append("        \""+column+"=\"+req.getParameter(\""+column+"\")+\" and \"+"+CR);
                }
                buffer.delete(buffer.length()-11,buffer.length()); // remove 'and' separator
                buffer.append(";"+CR);
            }
            
            buffer.append("        MyPool.runCommand(SQLString);"+CR);
            buffer.append(CR);
            buffer.append("        // view"+CR);
            
            // regular table vs. join table
            if (!pkColumn.equals("null")) {
                // message w/ 'show all' button
                buffer.append("        req.setAttribute(\"PageMessage\",\""+objectName+" deleted.\"+"+CR);
                buffer.append("        \"<p><input type=\\\"button\\\" onClick=\\\"document.location.href='/bison/servlet/FrontController?op=loadJSP&jsp=/inside/"+jspName+"/"+jspName+"Frame.jsp';\\\" value=\\\"Show All\\\">\");"+CR);
            } else {
                // message only
                buffer.append("        req.setAttribute(\"PageMessage\",\""+objectName+" deleted.\");"+CR);
            }
            
            buffer.append(CR);
            buffer.append("        returnLocation = \"/inside/Message.jsp\";"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        
        // ********** getByID **********
        if (!pkColumn.equals("null")) {
            buffer.append("    /**"+CR);
            buffer.append("     * Get Bean by ID."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * optional (HttpServletRequest.parameter.String) "+pkColumn+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) "+pkColumn+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Sets:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.(bison.Beans."+entityName+"Bean)) Current"+entityName+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static void getByID(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("        // get primary key from request"+CR);
            buffer.append("        String PKID = \"\";"+CR);
            buffer.append("        if ( req.getParameter(\""+pkColumn+"\")!=null && !req.getParameter(\""+pkColumn+"\").equals(\"\") ) {"+CR);
            buffer.append("            PKID = (String) req.getParameter(\""+pkColumn+"\");"+CR);
            buffer.append("        } else if ( req.getAttribute(\""+pkColumn+"\")!=null && !req.getAttribute(\""+pkColumn+"\").equals(\"\") ) {"+CR);
            buffer.append("            PKID = (String) req.getAttribute(\""+pkColumn+"\");"+CR);
            buffer.append("        }"+CR);
            buffer.append(CR);
            buffer.append("        // run query"+CR);
            buffer.append("        String query = baseSelectQuery + \" where "+pkColumn+"=\"+PKID;"+CR);
            buffer.append("        Vector rset = MyPool.runQuery(query);"+CR);
            buffer.append(CR);
            buffer.append("        // assign results"+CR);
            buffer.append("        "+projectName+".Beans."+entityName+"Bean CurrentBean = new "+projectName+".Beans."+entityName+"Bean();"+CR);
            buffer.append("        if (rset.size() > 0) {"+CR);
            buffer.append("            CurrentBean.setBean(((Hashtable) rset.elementAt(0)));"+CR);
            buffer.append("        }"+CR);
            buffer.append(CR);
            buffer.append("        // view"+CR);
            buffer.append("        req.setAttribute(\"Current"+entityName+"\", CurrentBean);"+CR);
            buffer.append("        returnLocation = \"/inside/"+jspName+"/Edit"+jspName+".jsp\";"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** select **********
        buffer.append("    /**"+CR);
        buffer.append("     * Select."+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @param (HttpServletRequest) req"+CR);
        buffer.append("     * @param (HttpServletResponse) res"+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @return (String) query"+CR);
        buffer.append("     *"+CR);
        buffer.append("     * Sets:"+CR);
        buffer.append("     * (HttpServletRequest.attribute.Vector) "+entityName+"List"+CR);
        buffer.append("     */"+CR);
        buffer.append("    public static String select(HttpServletRequest req, HttpServletResponse res)"+CR);
        buffer.append("    throws Exception {"+CR);
        buffer.append(CR);
        buffer.append("        Vector beanList = new Vector();"+CR);
        buffer.append("        String query = baseSelectQuery;"+CR);
        buffer.append("        Vector rset = MyPool.runQuery(query);"+CR);
        buffer.append(CR);
        buffer.append("        for (int c=0; c<rset.size(); c++) {"+CR);
        buffer.append("            "+projectName+".Beans."+entityName+"Bean CurrentBean = new "+projectName+".Beans."+entityName+"Bean();"+CR);
        buffer.append("            CurrentBean.setBean(((Hashtable) rset.elementAt(c)));"+CR);
        buffer.append("            beanList.addElement(CurrentBean);"+CR);
        buffer.append("            CurrentBean = null;"+CR);
        buffer.append("        }"+CR);
        buffer.append("        req.setAttribute(\""+entityName+"List\",beanList);"+CR);
        buffer.append(CR);
        buffer.append("        return query;"+CR);
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** select w/ whereClause **********
        buffer.append("    /**"+CR);
        buffer.append("     * Select with where clause."+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @param (HttpServletRequest) req"+CR);
        buffer.append("     * @param (HttpServletResponse) res"+CR);
        buffer.append("     * @param (String) whereClause"+CR);
        buffer.append("     *"+CR);
        buffer.append("     * @return (String) query"+CR);
        buffer.append("     *"+CR);
        buffer.append("     * Sets:"+CR);
        buffer.append("     * (HttpServletRequest.attribute.Vector) "+entityName+"List"+CR);
        buffer.append("     *"+CR);
        buffer.append("     */"+CR);
        buffer.append("    public static String select(HttpServletRequest req, HttpServletResponse res, String whereClause)"+CR);
        buffer.append("    throws Exception {"+CR);
        buffer.append(CR);
        buffer.append("        Vector beanList = new Vector();"+CR);
        buffer.append("        String query = baseSelectQuery + whereClause;"+CR);
        buffer.append("        Vector rset = MyPool.runQuery(query);"+CR);
        buffer.append(CR);
        buffer.append("        for (int c=0; c<rset.size(); c++) {"+CR);
        buffer.append("            "+projectName+".Beans."+entityName+"Bean CurrentBean = new "+projectName+".Beans."+entityName+"Bean();"+CR);
        buffer.append("            CurrentBean.setBean(((Hashtable) rset.elementAt(c)));"+CR);
        buffer.append("            beanList.addElement(CurrentBean);"+CR);
        buffer.append("            CurrentBean = null;"+CR);
        buffer.append("        }"+CR);
        buffer.append("        req.setAttribute(\""+entityName+"List\",beanList);"+CR);
        buffer.append(CR);
        buffer.append("        return query;"+CR);
        buffer.append("    }"+CR);
        buffer.append(CR);
        
        // ********** list **********
        if (!pkColumn.equals("null")) {
            buffer.append("    /**"+CR);
            buffer.append("     * List. Calls select()."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) Query - encoded SQL query from previous list operation"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) OrderCol - order column"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) OrderDesc - 0=ascending, 1=descending"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) OrderType - 'numeric'=numeric, (any other value)=alphanumeric"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) StartingRow"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) EndingRow"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Sets:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) Query"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) StartingRow"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) EndingRow"+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static void list(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("        // previous vs. new query"+CR);
            buffer.append("        String query = \"\";"+CR);
            buffer.append("        if (req.getParameter(\"Query\")!=null && !((String) req.getParameter(\"Query\")).equals(\"\") ) {"+CR);
            buffer.append("            // previous query found..."+CR);
            buffer.append("            // ...decode previous query and extract where clause"+CR);
            buffer.append("            String previousWhereClause = com.jaz.Common.stripMainClause(jaz.Common.stripOrderClause( com.jaz.Common.decode( (String) req.getParameter(\"Query\") ) ));"+CR);
            buffer.append("            // ...select using (previous where clause) and (new order clause from request parameters)"+CR);
            buffer.append("            query = select(req, res, previousWhereClause.trim() + ewb.GenericModel.getOrderClauseFromParams(req,res));"+CR);
            buffer.append("        } else {"+CR);
            buffer.append("            // new query"+CR);
            buffer.append("            query = select(req, res, ewb.GenericModel.getOrderClauseFromParams(req,res));"+CR);
            buffer.append("        }"+CR);
            buffer.append(CR);
            buffer.append("        // addtl attributes"+CR);
            buffer.append("        req.setAttribute(\"Query\", com.jaz.Common.encode(query.trim()));"+CR);
            buffer.append("        req.setAttribute(\"StartingRow\", ( req.getParameter(\"StartingRow\")!=null ? (String) req.getParameter(\"StartingRow\") : \"1\" ) );"+CR);
            buffer.append("        req.setAttribute(\"EndingRow\", ( req.getParameter(\"EndingRow\")!=null ? (String) req.getParameter(\"EndingRow\") : \"20\" ) );"+CR);
            buffer.append(CR);
            buffer.append("        // view"+CR);
            buffer.append("        returnLocation = \"/inside/"+jspName+"/List"+jspName+".jsp\";"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** listWhere **********
        if (!pkColumn.equals("null")) {
            buffer.append("    /**"+CR);
            buffer.append("     * List with where clause. Calls select()."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     * @param (String) whereClause"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) OrderCol - order column"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) OrderDesc - 0=ascending, 1=descending"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) OrderType - 'numeric'=numeric, (any other value)=alphanumeric"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) StartingRow"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) EndingRow"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Sets:"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) Query"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) StartingRow"+CR);
            buffer.append("     * (HttpServletRequest.attribute.String) EndingRow"+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static void listWhere(HttpServletRequest req, HttpServletResponse res, String whereClause)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("        String query = select(req, res, whereClause + ewb.GenericModel.getOrderClauseFromParams(req,res));"+CR);
            buffer.append(CR);
            buffer.append("        // addtl attributes"+CR);
            buffer.append("        req.setAttribute(\"Query\", com.jaz.Common.encode(query.trim()));"+CR);
            buffer.append("        req.setAttribute(\"StartingRow\", ( req.getParameter(\"StartingRow\")!=null ? (String) req.getParameter(\"StartingRow\") : \"1\" ) );"+CR);
            buffer.append("        req.setAttribute(\"EndingRow\", ( req.getParameter(\"EndingRow\")!=null ? (String) req.getParameter(\"EndingRow\") : \"20\" ) );"+CR);
            buffer.append(CR);
            buffer.append("        // view"+CR);
            buffer.append("        returnLocation = \"/inside/"+jspName+"/List"+jspName+".jsp\";"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** find **********
        if (!pkColumn.equals("null")) {
            buffer.append("    /**"+CR);
            buffer.append("     * Finds "+objectName+". Calls listWhere()."+CR);
            buffer.append("     *"+CR);
            buffer.append("     * @param (HttpServletRequest) req"+CR);
            buffer.append("     * @param (HttpServletResponse) res"+CR);
            buffer.append("     *"+CR);
            buffer.append("     * Expects:"+CR);
            buffer.append("     * optional (HttpServletRequest.attribute.String) "+findColumn+CR);
            buffer.append("     */"+CR);
            buffer.append("    public static void find(HttpServletRequest req, HttpServletResponse res)"+CR);
            buffer.append("    throws Exception {"+CR);
            buffer.append(CR);
            buffer.append("        // assemble where clause"+CR);
            buffer.append("        StringBuffer whereClause = new StringBuffer(\" where 1=1\");"+CR);
            buffer.append(CR);
            buffer.append("        // "+findColumn+CR);
            buffer.append("        if ( req.getParameter(\""+findColumn+"\")!=null && !((String) req.getParameter(\""+findColumn+"\")).equals(\"\") ) {"+CR);
            buffer.append("            whereClause.append(\" and lower("+findColumn+") like '%\"+((String) req.getParameter(\""+findColumn+"\")).toLowerCase()+\"%'\");"+CR);
            buffer.append("        }"+CR);
            buffer.append(CR);
            buffer.append("        listWhere(req,res,whereClause.toString());"+CR);
            buffer.append("    }"+CR);
            buffer.append(CR);
        }
        
        // ********** end class def **********
        buffer.append("} // end class"+CR);
        
        // finish, output file
        String text = buffer.toString();
        out.println(text);
        out.close();
        
        System.out.println("   Model generator finished.");
    }
    
} // end class
