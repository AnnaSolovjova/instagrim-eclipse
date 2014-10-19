package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.States;

/**
 * Servlet implementation class Profile
 */
@WebServlet(name = "/Profile", urlPatterns = { "/Profile",
    "/Profile/*" })
@MultipartConfig
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Cluster cluster;
	

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		 cluster = CassandraHosts.getCluster();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("dohodit2345?");
		
		String args[] = Convertors.SplitRequestPath(request);
		System.out.println(args[2]+"  "+args[1]+" "+args[0] + " argument");
		if(args[2].equals("Settings")){System.out.print("vhodit");changeProfile(request,response); }
		else{ 	
			try {
			 DisplayProfile(args[2], request, response);
	        } catch (Exception et) {
	        	
	        }}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("dohodit23456?");
		String name=request.getParameter("name");
         String surname=request.getParameter("surname");
         String login=request.getParameter("login");
       		User us = new User();
       		us.setCluster(cluster);
       		us.updateProfile(name,surname,login);
       		changeAvatar( request,  response);
       	 RequestDispatcher rd=request.getRequestDispatcher("/UserProfile.jsp");
         request.setAttribute("firstname", name);
         request.setAttribute("lastname", surname);
        
         System.out.println(name+surname+login+"davaj uze");
         try {
         	rd.forward(request, response);
    		} catch (ServletException | IOException e) {
    			e.printStackTrace();
    		}   		
		
		
		 
	}
	private void changeAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalStateException, ServletException
	{	String type = "avatar";
	System.out.println("bulkaaaaa");
		  for (Part part : request.getParts()) {
	            String filename = part.getName();
	            
	            InputStream is = request.getPart(part.getName()).getInputStream();
	            int i = is.available();
	            HttpSession session=request.getSession();
	            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
	            String username="majed";
	            if (lg.getlogedin()){
	                username=lg.getUsername();
	            }
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
	}
	
	
	protected void DisplayProfile(String User, HttpServletRequest request, HttpServletResponse response)
	{
		
		PicModel pm = new PicModel();
        pm.setCluster(cluster);
		User tm = new User();
        tm.setCluster(cluster);
	    tm.getInfo(User);
	    //Pic lsPics = pm.getAvatar(User);    
        String name=tm.getName();
        String surname=tm.getSurname();
       RequestDispatcher rd = request.getRequestDispatcher("/UserProfile.jsp");
        request.setAttribute("firstname", name);
        request.setAttribute("lastname", surname);
        	try {
        		rd.forward(request, response);
        	}
        		catch (ServletException | IOException e) {
        		e.printStackTrace();
        	}
		
	}
	
	private void changeProfile(HttpServletRequest request, HttpServletResponse response)
	{
		 
		 String name=request.getParameter("name");
	     String surname=request.getParameter("surname");
	     String login=request.getParameter("login");
	     System.out.println(name+surname+login);
	     RequestDispatcher rd=request.getRequestDispatcher("/ChangeProfileInfo.jsp");
	     request.setAttribute("firstname", name);
	     request.setAttribute("lastname", surname);
	     //request.setAttribute("UserName", login);
	     System.out.println(name+surname+login);
	     try {
	    	  System.out.println(name+surname+login);
	     	rd.forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
	}
	
}


