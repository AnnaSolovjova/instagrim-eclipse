package uk.ac.dundee.computing.aec.instagrim.models;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.Bytes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    
public void insertPic(byte[] b, String type, String name, String user) {
        try {
        	Convertors convertor = new Convertors();
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            System.out.println(picid+"lalalal");
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));
            output.write(b);
            
           if(type!="avatar"){
            String types[]=Convertors.SplitFiletype(type);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim");
            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);
           
            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();
           }else{
        	
        	   Session session = cluster.connect("instagrim");
        	   PreparedStatement psInsertAvatar = session.prepare("UPDATE userprofiles SET picId = ?, image =? WHERE login = ?");
        	   BoundStatement bsInsertAvatar = new BoundStatement(psInsertAvatar);
        	   session.execute(bsInsertAvatar.bind(picid,buffer,user));
        	   
               session.close();
           }

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
       
    }
   
    public byte[] darker(String picid,String type)
    {
    	 try {
             BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
             BufferedImage blacknwhite = dark(BI);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageIO.write(blacknwhite, type, baos);
             baos.flush();
             
             byte[] imageInByte = baos.toByteArray();
             baos.close();
             return imageInByte;
         } catch (IOException et) {

         }
         return null;
    }
    public byte[] lighter(String picid,String type)
    {
    	 try {
             BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
             BufferedImage blacknwhite = light(BI);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageIO.write(blacknwhite, type, baos);
             baos.flush();
             
             byte[] imageInByte = baos.toByteArray();
             baos.close();
             return imageInByte;
         } catch (IOException et) {

         }
         return null;
    }
    
    
    public void deletePic(String picid)
    {
    	System.out.println("CHECK"+picid);
    	 Session session = cluster.connect("instagrim");
         PreparedStatement ps = session.prepare("delete from userpiclist where picid =?");
    	 PreparedStatement ps2 = session.prepare("delete from Pics where picid =?");
         ResultSet rs,rs2 = null;
         	BoundStatement boundStatement = new BoundStatement(ps);
        	BoundStatement boundStatement2 = new BoundStatement(ps2);
         rs = session.execute( boundStatement.bind(java.util.UUID.fromString(picid)));
         rs2 = session.execute( boundStatement2.bind(java.util.UUID.fromString(picid)));
      
         if (rs2.isExhausted()) {
             System.out.println("No images deleted");
            
         } 
    }
    
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 300, OP_ANTIALIAS);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-3;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS);
        return pad(img, 2);
    }
   
   public static BufferedImage grey(BufferedImage img) {  
       img = resize(img, Method.SPEED, img.getWidth()-1, OP_ANTIALIAS, OP_GRAYSCALE);
       return pad(img, 2);
   }
   
   public static BufferedImage dark(BufferedImage img) {
       img = resize(img, Method.SPEED, img.getWidth(), OP_ANTIALIAS, OP_DARKER );
       return pad(img, 6);
   }
   public static BufferedImage light(BufferedImage img) {
       img = resize(img, Method.SPEED, img.getWidth(), OP_ANTIALIAS, OP_BRIGHTER  );
       return pad(img, 5);
   }
  
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?  ALLOW FILTERING");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(
                boundStatement.bind( User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);

                
                
                
            }
        }
        return Pics;
    }

    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
                System.out.println("TEST1");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
            	System.out.println("TEST2");
              ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            	
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(  picid));
            System.out.println("MOKAKA "+picid);
            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
            	
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                       
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                        
                        System.out.println("SETT2 "+ bImage);
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        System.out.println("babajka2"+bImage);
        p.setPic(bImage, length, type);

        return p;

    }
    
    //Methods for managing profileImage
    public Pic getAvatar(String user){
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            ResultSet rs = null;
         
            Statement select = QueryBuilder.select().column("image").from("instagrim", "userprofiles")
                .where((QueryBuilder.eq("login",user)));
            System.out.println("Statement: " + select);
            rs = session.execute(select);
            session.close();

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    bImage = row.getBytes("image");
                    

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p; 
    }


    public void updatePic(BufferedImage image,String picid) throws IOException
    {	Session session = cluster.connect("instagrim");
    	ByteBuffer bImage = null;
    	PreparedStatement ps = null; ResultSet rs = null;BoundStatement boundStatement;
    	
    	byte[] newImage=toByte(grey(image));
    	int length= newImage.length;
    	byte[] thumb=toByte(grey(image));
    	int thumblength= thumb.length;
    	byte[] processed=toByte(createProcessed(grey(image)));	
    	int processedlength= processed.length;
    	ByteBuffer buffer = ByteBuffer.wrap(toByte(image));
    	
        ByteBuffer thumbbuf=ByteBuffer.wrap(toByte(image));
        ByteBuffer processedbuf=ByteBuffer.wrap(toByte(image));
    	
        ps = session.prepare("UPDATE Pics SET image=?, thumb=?, processed=? ,imagelength=? ,thumblength=?,processedlength=?  WHERE picid =?");
        boundStatement = new BoundStatement(ps);
        rs = session.execute(boundStatement.bind(buffer,thumbbuf,processedbuf,length,thumblength,processedlength, (java.util.UUID.fromString(picid))));
            
    	
    }


public byte[] toByte(BufferedImage i)
{
	 try {
        
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write(i, "type", baos);
         baos.flush();
         byte[] imageInByte = baos.toByteArray();
         baos.close();
         return imageInByte;
     } catch (IOException et) {

     }
     return null;
}

}
