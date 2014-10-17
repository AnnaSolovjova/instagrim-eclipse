/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "/Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
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
    	 System.out.println("mandarin12");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String name=request.getParameter("name");
        String surname=request.getParameter("surname");
        String email=request.getParameter("email");
        String addresses=request.getParameter("addresses");
        User us=new User();
        System.out.println("mandarin1");
       us.setCluster(cluster);
        System.out.println("mandarin2");
        boolean success=us.RegisterUser(username, password,name,surname,email,addresses);
        System.out.println("mandarin333");
        if(success==true) 
        uploadAvatar(username);
	response.sendRedirect("/Instagrim");
        
    }
    
    public void uploadAvatar(String username) throws IOException
    {
              String type = "avatar";
              String filename = "";
            		  InputStream is = new FileInputStream(new File("C:\\Users\\Anna\\Desktop\\images.jpg"));
              System.out.println(is+"ajajaj");
              int i = is.available();
              if (i > 0) {
                  byte[] b = new byte[i + 1];
                  is.read(b);
                  System.out.println("Length : " + b.length);
                  PicModel tm = new PicModel();
                  tm.setCluster(cluster);
                  tm.insertPic(b, type, filename, username);

                  is.close();
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

}
