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
    "/Profile/*" })
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
		System.out.print("dohodit23?");
		
		String args[] = Convertors.SplitRequestPath(request);
		 try {
			
			 DisplayProfile(args[2], request, response);
			
	        } catch (Exception et) {
	        	System.out.print("checkerror");
	            //error("Bad Operator", response);
	           
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("dohodit???");
		String name=request.getParameter("name");
        String surname=request.getParameter("surname");
	      // RequestDispatcher rd2 = request.getRequestDispatcher("/UserProfile.jsp");
	      request.setAttribute("firstname", name);
	        request.setAttribute("lastname", surname);
	       // try {
			//	rd2.forward(request, response);
			//} catch (ServletException | IOException e) {
			//	e.printStackTrace();
			//}
		
		
		 
	}
	
	protected void DisplayProfile(String User, HttpServletRequest request, HttpServletResponse response)
	{
	
		User tm = new User();
        tm.setCluster(cluster);
	        tm.getInfo(User);
        String name=tm.getName();
        String surname=tm.getSurname();
        RequestDispatcher rd = request.getRequestDispatcher("/UserProfile.jsp");
          request.setAttribute("firstname", name);
        request.setAttribute("lastname", surname);
        try {
        	
			rd.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
