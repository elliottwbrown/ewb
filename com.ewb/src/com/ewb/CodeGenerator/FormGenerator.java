/*
 * ModelGenerator.java
 *
 * Created on April 4, 2003, 4:26 PM
 */

package com.ewb.CodeGenerator;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.lang.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
public class FormGenerator {
    
    public FormGenerator() {
    }
    
    
    public static void main(String[] args) throws java.lang.Exception {
        
        char fs = File.separatorChar;
        
        // pull bean save path from properties file
        PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle)PropertyResourceBundle.getBundle("com.ewb.ApplicationProperties");
        PropertyResourceBundle beangenPty = (PropertyResourceBundle)PropertyResourceBundle.getBundle(ApplicationProperties.getString("GeneratorProperties"));
        String bean_save_path = beangenPty.getString("BeanPath");
        String ProjectName = ApplicationProperties.getString("ProjectName");
        
        String TableName = args[0];
        String AdjustedTableName = TableName;
        String FindColumn = args[2];
        
        //list all the tables
        Vector TableList=com.ewb.DB._DBaccess.runQuery("select TABLE_NAME from user_tables");
        
        System.out.println("generating for table:"+TableName);
        
        Vector ColumnList=com.ewb.DB._DBaccess.runQuery("select data_length,column_name,nullable from user_tab_columns where TABLE_NAME='"+TableName+"'");
        
        Vector v=null;
        if (TableName.substring(0,2).equals("V_")) {
            AdjustedTableName=TableName.substring(2);
        } else {
            AdjustedTableName=TableName;
        }        
        v=com.ewb.DB._DBaccess.runQuery("select column_name as \"COLUMN_NAME\" from all_cons_columns where constraint_name in (select constraint_name from user_constraints where constraint_type='P' and TABLE_NAME='"+AdjustedTableName+"')");

        String PKColumn=(String) ((Hashtable) v.elementAt(0)).get("COLUMN_NAME");
        System.out.println("primary key column:"+PKColumn);
        
        String FileName=AdjustedTableName+"Form.jsp";
        String SavePath = beangenPty.getString("FormPath")+AdjustedTableName+fs;
        String CR = new String(new byte[]{13,10});
        String outputFileName = SavePath + fs + FileName;
        File outputFile = new File(outputFileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        PrintWriter out = new PrintWriter(fos);
        java.util.Date now = new java.util.Date();
        long TestTime = now.getTime();
        
        
        
        System.out.println("Generating Form for table:"+TableName);
        
        String text="";
        
        //Code to write to file
        text+=""+
        "<form name=\"\" action=\"/"+ProjectName+"/inside/"+TableName+"/processForm.jsp\" method=\"POST\">"+CR+
        "<input type=\"hidden\" name=\""+PKColumn+"\" value=\"<%=Current"+TableName+".get"+PKColumn+"()%>\">"+CR+
        CR+
        "<input type=\"submit\" value=\"Save "+TableName+"\">"+CR+
        "<% if (!Current"+TableName+".get"+PKColumn+"().equals(\"\")) { %>"+CR+
        "<input type=\"button\" value=\"Delete "+TableName+"\" onclick=\" if (confirm('Are you sure you want to delete this record ?')) location='/"+ProjectName+"/servlet/FrontController?op=delete"+TableName+"&"+PKColumn+"=<%=Current"+TableName+".get"+PKColumn+"()%>'\">"+CR+
        "<% } %>"+CR+
        "<input type=\"reset\">"+CR+
        "<p>";
        
        text += "<table>"+CR+CR;
        
        text+=""+
        "<% if (!Current"+TableName+".get"+PKColumn+"().equals(\"\")) { %>"+CR+
        "<tr>"+CR+
        "<td valign=top align=right>"+CR+
        ""+TableName+" ID"+CR+
        "</td>"+CR+
        "<td valign=top>"+CR+
        "<input type=\"hidden\" name=\""+PKColumn+"\" size=30 maxlength=30 value=\"<%=Current"+TableName+".get"+PKColumn+"()%>\">"+CR+
        "<%=Current"+TableName+".get"+PKColumn+"()%>"+CR+
        "</td>"+CR+
        "</tr>"+CR+
        "<% } %>"+CR+
        CR;
        
        for (int c=0; c<ColumnList.size(); c++) {
            String column=(String) ((Hashtable) ColumnList.elementAt(c)).get("COLUMN_NAME");
            String data_length=(String) ((Hashtable) ColumnList.elementAt(c)).get("DATA_LENGTH");
            
            if (!column.equals(PKColumn)) {
                text +=
                "    <tr>"+CR+
                "        <td align=right>"+column+"</td>"+CR+
                "        <td>"+CR+
                "            <input name=\""+column+"\" type=\"text\" size="+Math.min(Integer.parseInt(data_length),80)+" maxlength="+data_length+" value=\"<%=Current"+TableName+".get"+column+"()%>\">"+CR+
                "            <font class=\"error\"><%=Current"+TableName+".getErrorMsg(\""+column+"\")%></font>"+CR+
                "        </td>"+CR+
                "    </tr>"+CR;
            }
        }
        text +=  CR+"</table>"+CR;
        
        byte b[] = text.getBytes();
        out.println(text);
        out.close();
        
        System.out.println("Finished");
    }
} // end class
