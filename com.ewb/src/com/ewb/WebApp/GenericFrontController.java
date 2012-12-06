package com.ewb.WebApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class GenericFrontController extends HttpServlet {
    
    public ResourceBundle props,  sqlStatements;
    private String destination,  serverName,  portNumber,  sid,  url,  username,  password;
    boolean initialized = false;
    public static final int debugLevel =0;

    public abstract void setInitParameters() throws Exception;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            initialized = false;
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Initializing application ...");
            setInitParameters();
//            Iterator<String> i = sqlStatements.keySet().iterator();
//            while (i.hasNext()) {
//                String s = i.next();
//                DBAccess.addPreparedStatement(s, sqlStatements.getString(s));
//            }
            initialized = true;
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException");
        } catch (SQLException e) {
            String msg = e.getMessage();
            if (msg.contains("The Network Adapter could not establish the connection")) {
                System.err.println("  Could not connect to the database");
            } else if (msg.contains("invalid SQL statement")) {
                System.err.println("  SQL is bad");
            } else if (e.getErrorCode() == 17011) {
                System.err.println("  Empty resultset");
            } else if (msg.contains("Listener refused the connection")) {
                System.err.println("  Listener refused the connection");
            } else {
                System.err.println("  unhandled sql exception:" + msg);
                System.err.println(e.getErrorCode());
            }
        } catch (Exception e) {
            System.err.println("  unhandled exception in servlet init");
            e.printStackTrace();
        } finally {
            System.err.println("  Application initialized:" + initialized);
            System.out.println("------------------------------------------------------------------------");
        }
    }
    
    private boolean reinitialize(int reInitCnt, HttpServletRequest request) throws ServletException {
        boolean processed = true;
        System.err.println("Application not initialized - attempting re-initialization.");
        init(this.getServletConfig());
        if (initialized) {
            processed = false;
            System.err.println("Application re-initialized!");
        } else {
            request.setAttribute("msg", "The application could not be initialized.");
        }
        return processed;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Processing request at " + new java.util.Date());
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        destination = "/error.jsp";
        String msg = "operation processed", op = request.getParameter("op");
        System.out.println("  op=" + op);
        int reInitCnt = 0;
        boolean processed = false;
        while (!processed) {
            if (initialized) {
                try {
                    String opHandler = props.getString(op);
                    Method m = Class.forName(opHandler.substring(0, opHandler.indexOf(" "))).
                            getMethod(opHandler.substring(opHandler.indexOf(" ") + 1,
                            opHandler.length()), HttpServletRequest.class);
                    destination = (String) m.invoke(null, request);
                    processed = true;
                } catch (MissingResourceException e) {
                    request.setAttribute("msg", "That operation is not allowed.");
                    processed = true;
                } catch (Exception e) {
                    if (!handleException(request, e)) {
                        processed = true;
                    }
                    handleException(request, e);
                }
            } else {
                if (reInitCnt++ < 1) {
                    processed = reinitialize(reInitCnt, request);
                } else {
                    System.err.println("Application not re-initialized.");
                    request.setAttribute("msg", "The application could not be initialized.");
                }
            }
        }
        if (destination.startsWith("http")) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            destination=destination.replace("&amp;","&");
            response.setHeader("Location",destination);
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
        }
        out.close();
        System.out.println("Processed request at " + new java.util.Date());
        System.out.println("------------------------------------------------------------------------");
    }
    
    private boolean handleException(HttpServletRequest request, Exception e) {
        boolean recovered = false;
        String msg = "";
        System.err.println("  An exception has occured while processing a request.");
        boolean isSQLException = false;
        int SQLErrorCode=0;
        try {
            if (e.getCause()!=null && e.getCause().getClass() == Class.forName("java.sql.SQLException")) {
                isSQLException = true;
                SQLErrorCode = ((java.sql.SQLException) e.getCause()).getErrorCode();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenericFrontController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isSQLException && SQLErrorCode == 17008) {
            try {
                init(this.getServletConfig());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            recovered = initialized;
        } else {
            msg = "An error has occured: <b>" + e.getClass() + "<br/>" + e.getMessage() + "<br/>" + e.getCause() + "</b><br/>";
            StackTraceElement[] stas = e.getStackTrace();
            for (int x = 0; x < stas.length; x++) {
                msg += stas[x].toString() + "<br/>";
            }
            e.printStackTrace();
        }
        request.setAttribute("msg", msg);
        return recovered;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
