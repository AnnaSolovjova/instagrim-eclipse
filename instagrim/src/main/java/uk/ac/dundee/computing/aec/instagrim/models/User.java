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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    String name="";
    String surname="";
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password,String Name, String Surname,String Email,String Addresses){
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
                
               
               }
           
           
       }

     public String getName(){
     return name;
     }
     public String getSurname(){
     return surname;
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
    	}
      	 
   	 
   	 




