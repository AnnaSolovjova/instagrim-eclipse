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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

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
    String[]email=null;
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
        String type="register";
        if(IsValidUser(username,Password,type))
        {
        	return false;
        }
       Set <String> email=null;
       email.add(Email);
       System.out.println(email);
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email) Values(?,?,?,?,?)");
        BoundStatement boundStatement = new BoundStatement(ps);
        
        // PreparedStatement ps2 = session.prepare("update userprofiles SET email+=? where login= ? ");
         //BoundStatement boundStatement2 = new BoundStatement(ps2);
        	try{
        		System.out.println("tri");
        		session.execute( boundStatement.bind(username,EncodedPassword,Name,Surname,email));
        		//session.execute( boundStatement2.bind(Email,username));
        	}catch(Exception e)
        	{
        		
        		System.out.println("fail"+Email);	
        		return false;
        	}
        	//If code passed all the checks
                return true;
                
    }
    
    public boolean IsValidUser(String username, String Password,String type){
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
            return false;
        } else {
        	for (Row row : rs) {
        		if(type.equals("register")){return true;}
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
                Set<String>emailset = row.getSet("email", String.class);
                email=emailset.toArray(new String[emailset.size()]);
               
                
               }
           
           
       }

     public String getName(){
     return name;
     }
     public String getSurname(){
     return surname;
     }
     public String[] getEmail(){
         return email;
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
     
     public boolean userExists(String user)
     {
    	 
    	 Session session = cluster.connect("instagrim");
         PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
         ResultSet rs = null;
         BoundStatement boundStatement = new BoundStatement(ps);
         rs = session.execute( boundStatement.bind( user));
         if (rs.isExhausted()) {
             return false;
         } else {
        	 return true;
         }
     }
     
    	}
      	 
   	 
   	 




