package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Avatar
 */
@WebServlet(urlPatterns = {
	    "/Avatar",
	    "/Avatar/*"})


@MultipartConfig
public class Avatar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Cluster cluster=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Avatar() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(ServletConfig config) throws ServletException {
      
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private void DisplayAvatar(HttpServletResponse response,String User) throws ServletException, IOException {
        System.out.print(User+"USERCHECK");
    	PicModel tm = new PicModel();
        tm.setCluster(cluster);
  
        Pic p = tm.getAvatar(User);
        System.out.println("Length = " + p.getLength());
        OutputStream out = response.getOutputStream();
        response.setContentType("image/png");
        //response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session=request.getSession();
    	LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
        String UserName="majed";
        if (lg.getlogedin()){
            UserName=lg.getUsername();
        }
    	DisplayAvatar(response,UserName);
        
        
    }
    
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);
        User usr = new User();
        for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());

            String type = part.getContentType();
            String filename = part.getSubmittedFileName();
            
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            
            
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertAvatar(b, type, filename, args[2]);

                is.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
             rd.forward(request, response);
        }

    }*/

}
