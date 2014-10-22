package uk.ac.dundee.computing.aec.instagrim.models;

import java.util.Date;

import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.Bytes;

public class MultimediaModel {
	
	private Cluster cluster;

	public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

	 public java.util.LinkedList<Comment> getComentsForPic(String picid) {
	        java.util.LinkedList<Comment> CommentList = new java.util.LinkedList<>();
	        Session session = cluster.connect("instagrim");
	        PreparedStatement ps = session.prepare("select text,user from comments2 where picid =?  ALLOW FILTERING");
	        ResultSet rs = null;
	        BoundStatement boundStatement = new BoundStatement(ps);
	        rs = session.execute(boundStatement.bind(java.util.UUID.fromString(picid)));
	        if (rs.isExhausted()) {
	            System.out.println("No Images returned");
	            return null;
	        } else {
	            for (Row row : rs) {
	                Comment c = new Comment();
	                String user = row.getString("user");
	                String comment = row.getString("text");
	                c.setComment(comment, user, "date");
	                CommentList.add(c);
	            }
	        }
	        return CommentList;
	    }
	 
	 
	 public void insertComment(String picid, String comment, String user)
	 {
		 Session session = cluster.connect("instagrim");
     
         
         PreparedStatement psInsertPic = session.prepare("insert into comments2( picid, user, commentadded, text) values(?,?,?,?)");
          BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
        
         Date DateAdded = new Date();
        
         session.execute(bsInsertPic.bind(java.util.UUID.fromString(picid),  user, DateAdded, comment));
         System.out.println("DOHODIT3");
	 }
	 
	 
	 
public void deleteComment(String picid)
{
	Session session = cluster.connect("instagrim");
	
    PreparedStatement psInsertPic = session.prepare("delete from comments2 where picid=?");
     BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
    System.out.println("DOHODIT");
    session.execute(bsInsertPic.bind(java.util.UUID.fromString(picid)));
    System.out.println("DOHODIT2");
}
}
	 
	

