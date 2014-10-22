package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.imageio.ImageIO;
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

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.MultimediaModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/ImageEditMode/*",
    "/ImageEditMode",
    "/ImageDel/*",
    "/Images",
    "/Images/*",
    "/ImageUp",
    "/ImageUp/*",
    "/InputComment",
    "/InputComment/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);
        CommandsMap.put("ImageEditMode", 4);
        CommandsMap.put("ImageDel", 5);
        CommandsMap.put("ImageUp", 6);
        CommandsMap.put("InputComment",7);
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String args[] = Convertors.SplitRequestPath(request);
        String login;
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
          
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
      
        switch (command) {
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
                
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                
                break;
            case 4:
            	MultimediaModel m=new MultimediaModel();
            	m.setCluster(cluster);
            	java.util.LinkedList<Comment> lsComment = m.getComentsForPic(args[3]); 
            	RequestDispatcher rd = request.getRequestDispatcher("/ImageMaintainance.jsp");
                request.setAttribute("uuid", args[3]);
                request.setAttribute("login", args[2]);
                request.setAttribute("Comment", lsComment);
                rd.forward(request, response); 
                break;
                
            case 5:
            	MultimediaModel m3=new MultimediaModel();
            	m3.setCluster(cluster);
            	deletePic(args[2]);
            	m3.deleteComment(args[2]);
            	login=request.getParameter("login");
            	RequestDispatcher rd1 = request.getRequestDispatcher("/Images/"+login);
                rd1.forward(request, response);
                break;
                
            case 6:
              	
            	updatePic(args[2],response);
            	login=request.getParameter("login");
            	         	RequestDispatcher rd2 = request.getRequestDispatcher("/Images/"+login);
                rd2.forward(request, response);
                break;
            case 7:
            	MultimediaModel m2=new MultimediaModel();
            	m2.setCluster(cluster);
            	String comment=request.getParameter("comment");
            	String login2=request.getParameter("log");
            	HttpSession session=request.getSession();
            	LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
                String username="majed";
                if (lg.getlogedin()){
                    username=lg.getUsername();
                }
                System.out.println("DOHODIT2"+comment +" "+login2+" "+args[2]);
                
            	m2.insertComment(args[2], comment, username);
            	System.out.println("DOHODIT4");
            	RequestDispatcher rd3 = request.getRequestDispatcher("/ImageEditMode/"+login2+"/"+args[2]);
            	rd3.forward(request, response);
            	break;
            default:
                error("Bad Operator", response);
        }
    }

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Pics", lsPics);
        request.setAttribute("login", User);
        rd.forward(request, response);

    }

    private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        Pic p = tm.getPic(type,java.util.UUID.fromString(Image));
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
      
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        
       
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParts()!=null){
    	for (Part part : request.getParts()) {

            String type = part.getContentType();
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
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, username);

                is.close();
            }
    	}
            RequestDispatcher rd = request.getRequestDispatcher("/uploadPic.jsp");
             rd.forward(request, response);
        }

    }
    private void deletePic(String picid)
    {
    	PicModel pm =new PicModel();
    	pm.setCluster(cluster);
    	pm.deletePic(picid);
    }
    private void updatePic(String picid, HttpServletResponse response) throws IOException
    {	
    	
    	PicModel pm =new PicModel();
    	pm.setCluster(cluster);
    	Pic p = pm.getPic(1,java.util.UUID.fromString(picid));
        InputStream in = new ByteArrayInputStream(p.getBytes());
    	BufferedImage bImageFromConvert = ImageIO.read(in);
    	
    	pm.updatePic(bImageFromConvert,picid);
    	
    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
   
}