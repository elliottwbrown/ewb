package com.ewb.Beans;
/**
  * GenericTableBean.java
  *
  * Author:		Elliott Brown
  * Project:		ewb
  * Created:		Jan-30-20002
  * Modified:		Jan-20-20003
  *
  *
  **********************************************************************************************
  * This file defines a java bean to act as a generic bean for extension into other
  * beans - which encapsulate entity bean functionality.
  * the following function members:
  *  1 - GenericTableBean() : a constructor
  *  2 - getErrorMsg(),setErrors : retrieves/sets a field's error msg for display on the form
  **********************************************************************************************
  */

import java.util.*;
import java.io.*;
import java.beans.*;
import java.lang.reflect.*;

public class GenericTableBean implements Serializable {
    
    public Hashtable errors;
    
    public GenericTableBean() {
        errors = new Hashtable();
    }
    
    public String getErrorMsg(String s) {
        String errorMsg=(String)errors.get(s.trim());
        if (errorMsg!=null) System.out.println("validation error:"+errorMsg);
        return (errorMsg==null) ? "":errorMsg;
    }

    public void setErrors(String memberKey, String msg) {
        errors.put(memberKey, msg);
    }
    
    public boolean hasErrors() {
        return (!errors.isEmpty());
    }

    public void listErrors() {
        System.out.println("validation error:" + errors.toString());
    }

    public static String HTMLescape(String inStr) {
        
        String outStr = inStr;
        
        if ( inStr.indexOf('"') != -1 ) {
            StringBuffer hold = new StringBuffer();
            char c;
            for (int i=0; i<inStr.length(); i++) {
                if ( (c=inStr.charAt(i)) == '\"' ) {
                    hold.append("&#34;");
                } else {
                    hold.append(c);
                }
            }
            outStr = hold.toString();
        }

        return outStr;
    }
    
    /**
     * Maps fields of source object to an instance of this object.
     * Fields are mapped based on matching type and name.
     * Returns an instance of this object.
     * Useful for downcasting (mapping an instance of a superclass to an instance of a subclass).
     *
     * @param (Object) sourceObject
     * @return (Object) thisObject
     */
    public Object mapObject(Object sourceObject) throws Exception {
        
        // extract fields for this object and source object
        Field[] sourceObjectFields = sourceObject.getClass().getFields();
        Field[] thisObjectFields = this.getClass().getFields();
        
        // step thru this object's fields
        for (int c=0; c<thisObjectFields.length; c++) {
            // step thru source object's fields to find match
            for (int d=0; d<sourceObjectFields.length; d++) {
                // match field type and name
                if (
                        thisObjectFields[c].getType().equals(sourceObjectFields[d].getType()) &&
                        thisObjectFields[c].getName().equals(sourceObjectFields[d].getName())
                        ) {
                    // map source object field to this object field
                    thisObjectFields[c].set(this, sourceObjectFields[d].get(sourceObject));
                    break;
                }
            }
        }
        
        return this;
    }

} // end class