/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    Cluster cluster=null;


    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        
        
        
        User us=new User();
        us.setCluster(cluster);
        boolean isValid=us.IsValidUser(username, password,"login");
        HttpSession session=request.getSession();
        System.out.println("Session in servlet "+session);
        
        if (isValid){
            LoggedIn lg= new LoggedIn();
            lg.setLogedin();
            lg.setUsername(username);
            request.setAttribute("LoggedIn", lg);
            session.setAttribute("LoggedIn", lg);
            System.out.println("Session in servlet "+session);
            RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
            rd.forward(request,response);
        
            		
            
            
        }else{
            response.sendRedirect("/Instagrim/login.jsp");
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
public void dropTable(){
	 Session session = cluster.connect("instagrim");
	  PreparedStatement ps,ps2,ps3,ps4,ps5,ps6,ps7,ps8,ps9;
	  ps = session.prepare("DROP TABLE Pics");
	  ps2 =session.prepare("DROP TABLE userpiclist");
	  ps3 =session.prepare("DROP TABLE address");
	  ps4 =session.prepare("DROP TABLE userprofiles");
	  ps5 =session.prepare("DROP TABLE comments2");
	  ps6 =session.prepare("DROP TABLE likes");
	  ps7 =session.prepare("DROP TABLE comments");
      BoundStatement boundStatement = new BoundStatement(ps);
      BoundStatement boundStatement2 = new BoundStatement(ps2);
      BoundStatement boundStatement3= new BoundStatement(ps3);
      BoundStatement boundStatement4 = new BoundStatement(ps4);
      BoundStatement boundStatement5 = new BoundStatement(ps5);
      BoundStatement boundStatement6 = new BoundStatement(ps6);
      BoundStatement boundStatement7 = new BoundStatement(ps7);
      session.execute( boundStatement.bind());
      session.execute( boundStatement2.bind());
      session.execute( boundStatement3.bind());
      session.execute( boundStatement4.bind());
      session.execute( boundStatement5.bind());
      session.execute( boundStatement6.bind());
      session.execute( boundStatement7.bind());
}
}
    

