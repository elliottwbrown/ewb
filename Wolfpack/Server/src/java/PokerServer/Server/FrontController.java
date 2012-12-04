package PokerServer.Server;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class FrontController extends HttpServlet {
    
    int debugLevel=0;
    
    public FrontController() throws Exception {   }
    
    synchronized public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String op = "", returnValue = "";
        ServletConfig scn = getServletConfig();
        try {
            MyModel = new GenericModel();
            op = req.getParameterValues("op")[0];
            java.util.Date start=null;
            if (debugLevel>1) {
                System.out.println("----------------------------------------");
                System.out.println("-- FC:Processing Operation:"+op);
                start= new java.util.Date();
                System.out.println("-- session id: "+req.getSession().getId());
                System.out.println("-- start op: "+op+ " at "+start);
            }
           
            op = (String) req.getParameter("op");
            
            if (!op.equals("")) returnValue=MyModel.ProcessAction(op,req,res,scn);
            else returnValue="<?xml>error</xml>";
            res.setContentType("text/html");
            
            if (!returnValue.startsWith("<?xml")) {
                if (debugLevel>1) System.out.println("-- redirecting to: "+returnValue);
                scn.getServletContext().getRequestDispatcher(returnValue).forward(req, res);
            } else {
                if (debugLevel>1) System.out.println("-- streaming response");
                PrintWriter pw=res.getWriter();
                pw.println(returnValue);
                pw.close();
            }
            
            if (debugLevel>1) {
                java.util.Date end= new java.util.Date();
                System.out.println("-- finish op at "+end);
                System.out.println("-- took: "+(end.getTime()-start.getTime())+" ms");
                System.out.println("----------------------------------------");
            }
        } catch (java.lang.IllegalArgumentException e) {
            System.out.println("Exception trapped in FrontController: Redirect path is invalid");
            e.printStackTrace();
        } catch (java.lang.NullPointerException e) {
            System.out.println("Exception trapped in FrontController: No op");
        } catch (Exception e) {
            System.out.println("error in FC at EOP:"+e);
            e.printStackTrace();
        } finally {
            MyModel = null;
        }
    }
    
    synchronized public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }
    
    public GenericModel MyModel = null;
    static PropertyResourceBundle ApplicationProperties = (PropertyResourceBundle)PropertyResourceBundle.getBundle("PokerServer.Configuration.ApplicationProperties");
    
}
