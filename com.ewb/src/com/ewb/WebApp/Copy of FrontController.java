///**
// * FrontController.java
// *
// * Author:		Elliott Brown
// * Project:		ewb
// * Created:		Oct-1-20002
// * Modified:		Oct-4-20002
// *
// *
// **********************************************************************************************
// * This file routes flow between the presentation layer and application models
// * it should capture all input variables and package them
// * then execute the correct method of the correct ExModel for processing
// * It will receive a URL for relocating the response.
// *
// * It is built to be generic to ewb.commons and so gets
// * its project specific configuration information ...
// * and its operation map from
// *
// * It is also responsible for
// *
// **********************************************************************************************
// */
//
//package com.ewb.WebApp;
//
//import com.ewb.DB.DBAccess;
//import javax.servlet.*;
//import javax.servlet.http.*;
//import java.io.*;
//import java.util.*;
//import java.lang.reflect.*;
//
//public class FrontController extends HttpServlet {
//    
//    public static DBAccess db=null;
//    public static Hashtable params = new Hashtable();
//    public static HttpSession session;
//    //public static USERSBean ThisUser;
//    public static String returnLocation= new String();
//    public static String sql="";
//    public static PropertyResourceBundle ApplicationProperties;
//    public static PropertyResourceBundle prb;
//    public static String op = "";
//    
//    synchronized public void routeAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        
//        ServletConfig scn = getServletConfig();
//        System.out.println(">>> FC:initializing routeAction");
//        
//        try {
//            op = req.getParameterValues("op")[0];
//            System.out.println(">>> FC:op='"+op+"'");
//            if (op.equals("null")){
//                req.setAttribute("PageMessage","Requested operation is not available.");
//                scn.getServletContext().getRequestDispatcher("/Error.jsp").forward(req, res);
//            } else {
//                // process action
//                String opMap = prb.getString(op);
//                String className = opMap.substring(0,opMap.indexOf(" "));
//                String methodName = opMap.substring(opMap.indexOf(" ")+1,opMap.length());
//                processAction(req,res);
//                
//                // redirect
//                res.setContentType("text/html");
//                System.out.println(">>>>>>> redirecting to "+returnLocation);
//                scn.getServletContext().getRequestDispatcher(returnLocation).forward(req, res);
//            }
//        } catch (Exception e) {
//            System.out.println("error in FC:");
//            e.printStackTrace();
//            req.setAttribute("PageMessage","An error has occured in the FrontController.<P>"+e.toString());
//            scn.getServletContext().getRequestDispatcher("/Error.jsp").forward(req, res);
//        }
//    }
//    
//    synchronized public void processAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        ServletConfig scn = getServletConfig();
//        
//        try {
//            // populate the name-value vectors
//            /*
//            Enumeration paramNamesEnum = req.getParameterNames();
//            String CurrentParamName;
//            String value;
//            while (paramNamesEnum.hasMoreElements()) {
//                CurrentParamName = (String) paramNamesEnum.nextElement();
//                value=escape(((String) req.getParameterValues(CurrentParamName)[0]));
//                if (value==null || value.equals("null")) value="";
//                params.put(CurrentParamName,value);
//            }
//             */
//            
//            // invoke the method
//            String opMap = prb.getString(op);
//            String className = opMap.substring(0,opMap.indexOf(" "));
//            String methodName = opMap.substring(opMap.indexOf(" ")+1,opMap.length());
//            System.out.println(">>>>>>> className:"+className);
//            System.out.println(">>>>>>> methodName:"+methodName);
//            Class clazz = Class.forName(className);
//            Method[] methods=clazz.getMethods();
//            Method method=null;
//            for (int c=0;c<methods.length;c++) {
//                if (methods[c].getName().equals(methodName)) {
//                    method=methods[c];
//                }
//            }
//            returnLocation=(String) method.invoke(null,new Object[] {req,res,db});
//            
//        } catch (Exception e) {
//            System.out.println("error in FC:");
//            e.printStackTrace();
//            req.setAttribute("PageMessage","An error has occured in the FrontController.<P>"+e.toString());
//            scn.getServletContext().getRequestDispatcher("/Error.jsp").forward(req, res);
//        }
//    }
//    
//    synchronized public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        routeAction(req, res);
//    }
//    
//    synchronized public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        routeAction(req, res);
//    }
//    
//} // end class