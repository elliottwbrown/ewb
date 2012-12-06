package PokerServer.Server;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GenericModel extends FrontController {
    
    public static Hashtable params = new Hashtable();
    public static HttpSession session;
    public static String returnLocation = new String();
    public static PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle)PropertyResourceBundle.getBundle("PokerServer.Configuration.ApplicationProperties");
    public static PropertyResourceBundle prb = (PropertyResourceBundle)PropertyResourceBundle.getBundle(ApplicationProperties.getString("OperationMap"));
    
    public GenericModel() throws Exception {
    }
    
    synchronized public String ProcessAction(String op, HttpServletRequest req, HttpServletResponse res,ServletConfig scn) throws Exception {
        String opMap=null;
        try {
            opMap = prb.getString(op);
            String className = opMap.substring(0,opMap.indexOf(" "));
            String methodName = opMap.substring(opMap.indexOf(" ")+1,opMap.length());
            session = req.getSession(true);
            Class clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            Method method = null;
            for (int c=0;c<methods.length;c++)
                if (methods[c].getName().equals(methodName))
                    method = methods[c];
            if (debugLevel>1) System.out.println("    class: "+className);
            if (debugLevel>1) System.out.println("    method: "+methodName);
            if (debugLevel>2) System.out.println("    method: "+method);
            method.invoke(null,new Object[] {req,res});
        } catch (java.lang.NullPointerException e) {
            //System.out.println("Exception trapped in GenericModel: Method not found in class");
            e.printStackTrace();
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Exception trapped in GenericModel: ClassNotFoundException");
        } catch (java.util.MissingResourceException e) {
            System.out.println("Exception trapped in GenericModel: Operation not found");
            returnLocation="<?xml>error</xml>";
        } catch (Exception e) {
            System.out.println("Exception trapped in GenericModel: Unhandled");
            printErrorMsg(e,req,res);
            throw e;
        }
        
        return returnLocation;
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
            req.setAttribute(CurrentParamName, (String) req.getParameterValues(CurrentParamName)[0]);
        }
        Enumeration paramValues = params.elements();
        Enumeration paramNames = params.keys();
        while (paramNames.hasMoreElements()) {
            System.out.println(paramNames.nextElement()+" = "+paramValues.nextElement());
        }
    } // end method
    
}
