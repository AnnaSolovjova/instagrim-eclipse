package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    "/Profile/*", "/Profile/Avatar","/Profile/Avatar/*"})
@MultipartConfig
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Cluster cluster;
	 private boolean change=false;

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
	
		
		String args[] = Convertors.SplitRequestPath(request);
		System.out.print(args[2]);
		 try {
			 DisplayProfile(args[2], request, response);
			
			// DisplayAvatar(args[3],  response);
	        } catch (Exception et) {
	        	System.out.print("checkerror");
	            //error("Bad Operator", response);
	           
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 System.out.println("dowlo1234");
       	 String name=request.getParameter("name");
         String surname=request.getParameter("surname");
         String login=request.getParameter("login");
       		User us = new User();
       		PicModel pic = new PicModel();
       		us.setCluster(cluster);
       		us.updateProfile(name,surname,login);
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
	
	protected void DisplayProfile(String User, HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("hujnja");
		PicModel pm = new PicModel();
        pm.setCluster(cluster);
		User tm = new User();
        tm.setCluster(cluster);
	    tm.getInfo(User);
	    Pic lsPics = pm.getAvatar(User);    
        String name=tm.getName();
        String surname=tm.getSurname();
       RequestDispatcher rd = request.getRequestDispatcher("/UserProfile.jsp");
        request.setAttribute("firstname", name);
        request.setAttribute("lastname", surname);
        System.out.println(lsPics+"provero4ka"+lsPics.getSUUID());
     //   request.setAttribute("avatar", lsPics);
        	try {
        		rd.forward(request, response);
        	}
        		catch (ServletException | IOException e) {
        		e.printStackTrace();
        	}
		
	}
	  private void DisplayAvatar(String Image, HttpServletResponse response) throws ServletException, IOException {
	        PicModel tm = new PicModel();
	        tm.setCluster(cluster);
	        Pic p = tm.getPic(4, java.util.UUID.fromString(Image));
	  
	        OutputStream out = response.getOutputStream();
	        response.setContentType(p.getType());
	        response.setContentLength(p.getLength());
	        System.out.println("information"+p.getType()+p.getLength());
	        //out.write(Image);
	        InputStream is = new ByteArrayInputStream(p.getBytes());
	        BufferedInputStream input = new BufferedInputStream(is);
	        byte[] buffer = new byte[8192];
	        for (int length = 0; (length = input.read(buffer)) > 0;) {
	            out.write(buffer, 0, length);
	        }
	        out.close();
	    }
	
	

}
