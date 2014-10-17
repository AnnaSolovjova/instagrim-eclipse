/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;

import java.io.FileInputStream;
import java.lang.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    String name="";
    String surname="";
    byte[] avatar=null;
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password,String Name, String Surname,String Email,String Addresses) throws IOException{
    	
    	
    	
    	AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        
       
        
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name) Values(?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
       // PreparedStatement ps2 = session.prepare("update userprofiles set email=email+  {?} where login= ? ");
      //  BoundStatement boundStatement2 = new BoundStatement(ps2);
        try{
        	session.execute( boundStatement.bind(username,EncodedPassword,Name,Surname));
        	//session.execute( boundStatement2.bind(Email,username));
        }catch(Exception e)
        {
        	System.out.println("fail"+Email);	
        	return false;
        }
        
        //We are assuming this always works.  Also a transaction would be good here !
                return true;
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( boundStatement.bind( username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
        	for (Row row : rs) {
                
                String StoredPass = row.getString("password");
              
                if (StoredPass.compareTo(EncodedPassword) == 0)
                	
                return true;
            }
        }
   
     
    return false;  
    }
    
    
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
       
     public void getInfo(String username) {
    
    	   Session session = cluster.connect("instagrim");
    	   
    	   PreparedStatement ps = session.prepare("select * from userprofiles where login =?");
    	   
           ResultSet rs = null;
           BoundStatement boundStatement = new BoundStatement(ps);
           rs = session.execute( boundStatement.bind(username));
          
           for (Row row : rs) {
        	  System.out.println("row"+row);
        	
                name = row.getString("first_name");
                surname = row.getString("last_name");
                System.out.println("matrjowka");
                
               }
           
           
       }

     public String getName(){
     return name;
     }
     public String getSurname(){
     return surname;
     }
     public byte[] getAvatar(){
         return avatar;
         }
     
    
     
     public void updateProfile(String Name, String Surname,String UserName) {
    	 Session session = cluster.connect("instagrim");
     	  PreparedStatement ps;
            
      	 if(Name!=null)
       	{
      		  ps = session.prepare("update userprofiles  SET first_name=? WHERE login =?");
      		  ResultSet rs = null;
            	BoundStatement boundStatement = new BoundStatement(ps);
            	rs = session.execute( boundStatement.bind(Name, UserName));
       	}
       	if(Surname!=null)
       	{
       	 ps = session.prepare("update userprofiles  SET last_name=? WHERE login =?");
      		  ResultSet rs = null;
            	BoundStatement boundStatement = new BoundStatement(ps);
            	rs = session.execute( boundStatement.bind(Surname,UserName));
       	}
       	
       
       	
       	}
     
     public void UpdateProfile(String UserName,byte[] avatarPic)
     {
    	 Session session = cluster.connect("instagrim");
     	 PreparedStatement ps;
 
    		if(avatarPic!=null)
           	{
           	 try {
                System.out.println("length of the pic" + avatarPic.length);
                ByteBuffer buffer = ByteBuffer.wrap(avatarPic);
                //The following is a quick and dirty way of doing this, will fill the disk quickly !
                PreparedStatement psInsertPic = session.prepare("update userprofiles Set imageAvatar=? where login = ?");
                BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
                session.execute(bsInsertPic.bind(buffer,UserName));
                session.close();
            	} catch (Exception ex) {
                System.out.println("Error --> " + ex);}
            
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