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
import uk.ac.dundee.computing.aec.instagrim.models.User;
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
    "/InputComment/*",
    "/Like/*"
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
        CommandsMap.put("Like",8);
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
            error("Bad Ocoperator", response);
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
            	ImageEditMode(request,response,args);
                break;
                
            case 5:
            	deletePic(request,response,args);
                break;
                
            case 6:
            	ImageUpload(request,response,args);
                break;
            case 7:
            	InputComment(request,response,args);
            	break;
            case 8:
            	InputLike(request,response,args);
            	break;
            default:
                error("Page Doesnt exist", response);
        }
    }
//All method for editing image page
    private void ImageEditMode(HttpServletRequest request, HttpServletResponse response, String[] args) throws ServletException, IOException
    {
    	MultimediaModel m=new MultimediaModel();
    	m.setCluster(cluster);
    	java.util.LinkedList<Comment> lsComment = m.getComentsForPic(args[3]); 
    	int count=m.countLikes(args[3]);
    	String count2=Integer.toString(count);
        RequestDispatcher rd = request.getRequestDispatcher("/ImageMaintainance.jsp");
        request.setAttribute("uuid", args[3]);
        request.setAttribute("login", args[2]);
        request.setAttribute("Comment", lsComment);
        request.setAttribute("like", count2);
        rd.forward(request, response); 
    
    }
    private void deletePic(HttpServletRequest request, HttpServletResponse response, String[] args) throws ServletException, IOException
    {
    	MultimediaModel m=new MultimediaModel();
    	m.setCluster(cluster);
    	deletePic(args[2]);
    	m.deleteComment(args[2]);
    	m.deleteLikes(args[2]);
    	String login=request.getParameter("login");
    	RequestDispatcher rd = request.getRequestDispatcher("/Images/"+login);
        rd.forward(request, response);
    }
    private void ImageUpload(HttpServletRequest request, HttpServletResponse response, String[] args) throws IOException, ServletException{
    	String effect=request.getParameter("radio1");
    	String login=request.getParameter("login");
    	String test=request.getParameter("test");
    	int dark=Integer.parseInt(test);
    	updatePic(args[2],response,effect,dark);
    	RequestDispatcher rd = request.getRequestDispatcher("/Images/"+login);
        rd.forward(request, response);
    }
    private void InputComment(HttpServletRequest request, HttpServletResponse response, String[] args) throws IOException{
    	MultimediaModel m=new MultimediaModel();
    	m.setCluster(cluster);
    	String comment=request.getParameter("comment");
    	String login=request.getParameter("log");
    	HttpSession session=request.getSession();
    	LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
        String username="majed";
        if (lg.getlogedin()){
            username=lg.getUsername();
        }                
    	m.insertComment(args[2], comment, username);
    	response.sendRedirect("/Instagrim-AS/ImageEditMode/"+login+"/"+args[2]);
    }
    private void InputLike(HttpServletRequest request, HttpServletResponse response, String[] args) throws IOException{
    	MultimediaModel m=new MultimediaModel();
    	m.setCluster(cluster);
    	String login=request.getParameter("log");
    	HttpSession session2=request.getSession();
    	LoggedIn lg= (LoggedIn)session2.getAttribute("LoggedIn");
        String username2="majed";
        if (lg.getlogedin()){
            username2=lg.getUsername();
        }    
        m.insertLikes(username2,args[2]);
        response.sendRedirect("/Instagrim-AS/ImageEditMode/"+login+"/"+args[2]);
    }
    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics=null;
        if(!User.equals("majed")){
        lsPics = tm.getPicsForUser(User);
        }
        else{lsPics=tm.getRandom();}
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
    private void updatePic(String picid, HttpServletResponse response,String effect,int dark) throws IOException
    {	
    	PicModel pm =new PicModel();
    	pm.setCluster(cluster);
    	pm.getPicForModification(java.util.UUID.fromString(picid),effect,dark);
    	
    }
    @Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String args[] = Convertors.SplitRequestPath(request);
    	MultimediaModel m=new MultimediaModel();
    	m.setCluster(cluster);
    	deletePic(args[2]);
    	m.deleteComment(args[2]);
    	m.deleteLikes(args[2]);
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