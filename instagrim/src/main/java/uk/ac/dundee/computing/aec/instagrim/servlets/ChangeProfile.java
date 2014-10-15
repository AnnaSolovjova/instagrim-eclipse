package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.dundee.computing.aec.instagrim.models.User;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;

/**
 * Servlet implementation class ChangeProfile
 */
@WebServlet(name = "/ChangeProfile", urlPatterns = { "/ChangeProfile","/ChangeProfile/* "})
@MultipartConfig

public class ChangeProfile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Cluster cluster;
	/**
 	* @see HttpServlet#HttpServlet()
 	*/
	public ChangeProfile() {
    	super();
    	// TODO Auto-generated constructor stub
	}
	public void init(ServletConfig config) throws ServletException {
		 cluster = CassandraHosts.getCluster();
	}

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   	 System.out.println("dowlo");
   	 System.out.println("ch");
	 String name=request.getParameter("name");
     String surname=request.getParameter("surname");
     String login=request.getParameter("login");
     System.out.println(name+surname+login);
     RequestDispatcher rd=request.getRequestDispatcher("/ChangeProfileInfo.jsp");
     request.setAttribute("firstname", name);
     request.setAttribute("lastname", surname);
   //  request.setAttribute("UserName", login);
     System.out.println(name+surname+login);
     try {
    	  System.out.println(name+surname+login);
     	rd.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
    
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 System.out.println("dowlo123");
       	 String name=request.getParameter("name");
         String surname=request.getParameter("surname");
         String login=request.getParameter("login");
       		User us = new User();
       		us.setCluster(cluster);
       		us.updateProfile(name,surname,login);
       	 RequestDispatcher rd=request.getRequestDispatcher("/Profile/"+login);
         request.setAttribute("firstname", name);
         request.setAttribute("lastname", surname);
       
         System.out.println(name+surname+login);
         try {
         	rd.forward(request, response);
    		} catch (ServletException | IOException e) {
    			e.printStackTrace();
    		}   		
    }

}
