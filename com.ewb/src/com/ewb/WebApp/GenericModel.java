/**
 * GenericModel.java
 *
 */

package com.ewb.WebApp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.ewb.DB._DBaccess;
import com.ewb.Utilities.*;

public class GenericModel extends com.ewb.WebApp.FrontController {
    
    public static Hashtable params = new Hashtable();
    public static HttpSession session;
    //public static ewb.Beans.USERSBean ThisUser;
    public static String returnLocation= new String();
    public static String PKID = "";
    
    public GenericModel() throws Exception {  }
    
    public static void printParams(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        Enumeration paramNamesEnum = req.getParameterNames();
        String CurrentParamName;
        Hashtable params = new Hashtable();
        while (paramNamesEnum.hasMoreElements()) {
            CurrentParamName = (String) paramNamesEnum.nextElement();
            params.put(CurrentParamName,escapeFunctions.escape( (String) req.getParameterValues(CurrentParamName)[0]));
        }
        Enumeration paramValues = params.elements();
        Enumeration paramNames = params.keys();
        while (paramNames.hasMoreElements()) {
            System.out.println(paramNames.nextElement()+" = "+paramValues.nextElement());
            
        }
    }
    
    public void printErrorMsg(Exception e, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        e.printStackTrace();
        
        if (e.getCause()!=null) {
            e.getCause().printStackTrace();
        }
        
        Enumeration paramNamesEnum = req.getParameterNames();
        String CurrentParamName;
        Hashtable params = new Hashtable();
        while (paramNamesEnum.hasMoreElements()) {
            CurrentParamName = (String) paramNamesEnum.nextElement();
            params.put(CurrentParamName,escapeFunctions.escape( (String) req.getParameterValues(CurrentParamName)[0]));
        }
        Enumeration paramValues = params.elements();
        Enumeration paramNames = params.keys();
        while (paramNames.hasMoreElements()) {
            System.out.println(paramNames.nextElement()+" = "+paramValues.nextElement());
        }
    } // end method
}
