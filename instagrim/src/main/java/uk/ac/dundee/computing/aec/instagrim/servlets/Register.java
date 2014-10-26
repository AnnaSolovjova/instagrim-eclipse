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
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String password2=request.getParameter("password2");
        String name=request.getParameter("name");
        String surname=request.getParameter("surname");
        String email2=request.getParameter("email2");
        String street=request.getParameter("street");
        String city=request.getParameter("city");
        String country=request.getParameter("country");
        String post=request.getParameter("post");
        checkIfAll(username,password,password,name,surname,email2,response);
        checkIfSame(password,password2,response);
        User us=new User();
        us.setCluster(cluster);
       
        
        
        boolean success=us.RegisterUser(username, password,name,surname,email2,street,city,post);
        
        if(success==true) 
        {
        uploadAvatar(username,request);
        RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
        rd.forward(request,response);
        }
        else{
        RequestDispatcher rd=request.getRequestDispatcher("register.jsp");
        rd.forward(request,response);
        
        }
        
        
        
    }
    //validate input
    private boolean checkIfAll(String username,String password,String password2,String name,String surname,String email,HttpServletResponse response) throws ServletException, IOException
    {
    	if(""!=username&&""!=password&&""!=password2&&""!=name&&""!=surname&&""!=email)
    	{return true;
    	}
    	else{
    		error("You need to fill all the fields", response);
    		return false;
    	}
    }

    private boolean checkIfSame(String p1,String p2, HttpServletResponse response) throws ServletException, IOException
    {
    	if(!p1.equals(p2)){
    		error("Passwords are different", response);return false;
    	}return true;
    		
    }
    private void checkRightLength(String p1,String login, HttpServletResponse response) throws ServletException, IOException
    {
    	if(login.length()<3||login.length()>10||p1.length()<5||p1.length()<10){
    		error("The length of password is wrong(5-10) or length of login is wrong(3-10)", response);}	
    }
    
    public void uploadAvatar(String username,HttpServletRequest request) throws IOException
    {			
              String type = "avatar";
              String filename = "";
              ServletContext  context =request.getSession().getServletContext();
              System.out.println("Context Path: "+ context.getContextPath());
              InputStream is = context.getResourceAsStream("/images.jpg");
            
              int i = is.available();
              if (i > 0) {
                  byte[] b = new byte[i + 1];
                  is.read(b);
                  PicModel tm = new PicModel();
                  tm.setCluster(cluster);
                  tm.insertPic(b, type, filename, username);
                  is.close();
              }
              
          
    }
    //displays error message
    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.println("<a href=\"register.jsp\">Back</a>");
        out.close();
        return;
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
