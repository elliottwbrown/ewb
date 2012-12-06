/**
 * Generator.java
 */

package com.ewb.CodeGenerator;

import java.io.*;
import java.util.*;

public class Generator {
    
    public PropertyResourceBundle ApplicationProperties;
    public PropertyResourceBundle beangenPty;
    public static String GenerateBeans;
    public static String GenerateModels;
    public static String GenerateForms;
    
    public Generator() {
    }
    
    public static void main(String[] args) throws java.lang.Exception {
        
        System.out.println("Generator started.");
        
        char fs = File.separatorChar;
        
        // get generater properties
        PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle) PropertyResourceBundle.getBundle("com.ewb.ApplicationProperties");
        PropertyResourceBundle beangenPty = (PropertyResourceBundle) PropertyResourceBundle.getBundle(ApplicationProperties.getString("GeneratorProperties"));
        GenerateBeans = beangenPty.getString("GenerateBeans");
        GenerateModels = beangenPty.getString("GenerateModels");
        GenerateForms = beangenPty.getString("GenerateForms");
        
        // list all tables
        Vector List = com.ewb.DB._DBaccess.runQuery("select * from GENERATOR");
        for (int c=0; c<List.size(); c++) {
            Hashtable currentRow = (Hashtable) List.elementAt(c);
            
            // bean
            if (GenerateBeans.equals("1")) {
                if ( ((String)currentRow.get("GENERATE_BEAN")).equals("1") ) {
                    BeanGenerator.main(new String[] {(String) currentRow.get("TABLE_NAME"),(String) currentRow.get("OBJECT_NAME"),(String) currentRow.get("FIND_COLUMN"),(String) currentRow.get("ENTITY_NAME"),(String) currentRow.get("PK_TABLE_NAME"),(String) currentRow.get("FORM_NAME")});
                }
            }
            
            // model
            if (GenerateModels.equals("1")) {
                if ( ((String)currentRow.get("GENERATE_MODEL")).equals("1") ) {
                    ModelGenerator.main(new String[] {(String) currentRow.get("TABLE_NAME"),(String) currentRow.get("OBJECT_NAME"),(String) currentRow.get("FIND_COLUMN"),(String) currentRow.get("ENTITY_NAME"),(String) currentRow.get("PK_TABLE_NAME"),(String) currentRow.get("FORM_NAME")});
                }
            }
            
            // form
            if (GenerateForms.equals("1")) {
                if ( ((String)currentRow.get("GENERATE_FORM")).equals("1") ) {
                    FormGenerator.main(new String[] {(String) currentRow.get("TABLE_NAME"),(String) currentRow.get("OBJECT_NAME"),(String) currentRow.get("FIND_COLUMN"),(String) currentRow.get("ENTITY_NAME"),(String) currentRow.get("PK_TABLE_NAME"),(String) currentRow.get("FORM_NAME")});
                }
            }
        }
        
        System.out.println("Generator finished.");
    }
    
}
