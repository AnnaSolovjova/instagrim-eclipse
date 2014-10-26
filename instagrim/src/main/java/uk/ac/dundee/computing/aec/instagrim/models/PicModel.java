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
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
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
       
        	Convertors convertor = new Convertors();
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();    
         
           if(type!="avatar"){
            String types[]=Convertors.SplitFiletype(type);
            byte []  thumbb = picresize(b,types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(b,types[1]);
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

      
}


public void deletePic(String picid)
{
	
	 Session session = cluster.connect("instagrim");
     PreparedStatement ps = session.prepare("delete from userpiclist where picid =?");
	 PreparedStatement ps2 = session.prepare("delete from Pics where picid =?");
     ResultSet rs,rs2 = null;
     	BoundStatement boundStatement = new BoundStatement(ps);
    	BoundStatement boundStatement2 = new BoundStatement(ps2);
     rs = session.execute( boundStatement.bind(java.util.UUID.fromString(picid)));
     rs2 = session.execute( boundStatement2.bind(java.util.UUID.fromString(picid)));
  
    
}

public void deleteAllPic(String user)
{
	MultimediaModel mm=new MultimediaModel();
	mm.setCluster(cluster);
	//ResultSet rs,rs2 = null;
	java.util.LinkedList<Pic> picList= getPicsForUser(user);
	 Session session = cluster.connect("instagrim");
	 if (picList == null) {
	        } else {
	        	
	            Iterator<Pic> iterator;
	            iterator = picList.iterator();
	            PreparedStatement ps = session.prepare("delete from userpiclist where picid =?");
	       	 	PreparedStatement ps2 = session.prepare("delete from Pics where picid =?");
	            ResultSet rs1,rs2= null;
	        
	            BoundStatement boundStatement = new BoundStatement(ps);
	           	BoundStatement boundStatement2 = new BoundStatement(ps2);
	            while (iterator.hasNext()) {
	            	System.out.println("notempty6");
	                Pic p = (Pic) iterator.next();
	       
	                rs1 = session.execute( boundStatement.bind(java.util.UUID.fromString(p.getSUUID())));
		            rs2 = session.execute( boundStatement2.bind(java.util.UUID.fromString(p.getSUUID())));
	                System.out.println("OK");
	                mm.deleteComment(p.getSUUID()); 
	                mm.deleteLikes(p.getSUUID());
	                System.out.println("OK2");

	            } }
	            
	 
   
  
  
  
    
}

//Picture modification
    public byte[] picresize(byte[] in,String type) {
        try {
        	InputStream input = new ByteArrayInputStream(in);
            BufferedImage BI = ImageIO.read(input);
            //will be hard to use in when editing pictures-uses a lot of memory
            // BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
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
    
    public byte[] picdecolour(byte[] in,String type) {
        try {
        	InputStream input = new ByteArrayInputStream(in);
            BufferedImage BI = ImageIO.read(input);
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
    
    public static BufferedImage serpia(BufferedImage img, int sepiaIntensity) {

        BufferedImage sepia = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Play around with this.  20 works well and was recommended
        int sepiaDepth = 20;

        int w = img.getWidth();
        int h = img.getHeight();

        WritableRaster raster = sepia.getRaster();

        // We need 3 integers (for R,G,B color values) per pixel.
        int[] pixels = new int[w * h * 3];
        img.getRaster().getPixels(0, 0, w, h, pixels);

        for (int i = 0; i < pixels.length; i += 3) {
            int r = pixels[i];
            int g = pixels[i + 1];
            int b = pixels[i + 2];

            int gry = (r + g + b) / 3;
            r = g = b = gry;
            r = r + (sepiaDepth * 2);
            g = g + sepiaDepth;

            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }

            // Darken blue color to increase sepia effect
            b -= sepiaIntensity;

            // normalize if out of bounds
            if (b < 0) {
                b = 0;
            }
            if (b > 255) {
                b = 255;
            }

            pixels[i] = r;
            pixels[i + 1] = g;
            pixels[i + 2] = b;
        }
        raster.setPixels(0, 0, w, h, pixels);

        return sepia;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 300, OP_ANTIALIAS);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth();
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS);
        return pad(img, 2);
    }
   
   public static BufferedImage grey(BufferedImage img) {  
       img = resize(img, Method.SPEED, img.getWidth()-1, OP_ANTIALIAS, OP_GRAYSCALE);
       return pad(img, 2);
   }
   
   public static BufferedImage dark(BufferedImage img, int scale) {
	   float scaleFactor = 0.9f-((scale)/10);
	   RescaleOp op = new RescaleOp(scaleFactor, 0, null);
	   img = op.filter(img, null);
	   return img;
   }
   //taken from http://exampledepot.8waytrips.com/egs/java.awt.image/Bright.html
   public static BufferedImage light(BufferedImage img,int scale) {
	   float scaleFactor = 1.0f+(scale/5);
	   RescaleOp op = new RescaleOp(scaleFactor, 0, null);
	   img = op.filter(img, null);
	   return img;
   }
  
   
   //getting pictures
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
         
            } else if (image_type == Convertors.DISPLAY_THUMB) {

              ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
             
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            	
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(picid));
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
        p.setPic(bImage, length, type);

        return p;

    }
    
   public void getPicForModification(java.util.UUID picid,String effect,int darknes) throws IOException
   {
	   Session session = cluster.connect("instagrim");
	   byte[] retreived=null;
       ByteBuffer bImage = null;
       String type = null;int length = 0;
       ResultSet rs = null;
       PreparedStatement ps = null;
       ps = session.prepare("select image,imagelength,type from pics where picid =?");
       BoundStatement boundStatement = new BoundStatement(ps);
       rs = session.execute(boundStatement.bind(picid));
       		if (rs.isExhausted()) {
       		}
       		else {
       			for (Row row : rs) {
       				bImage = row.getBytes("image");
       				length = row.getInt("imagelength");
       				type = row.getString("type");
       			
       			 String types[]=Convertors.SplitFiletype(type);
                 bImage = row.getBytes("image");  
                 retreived = new byte[bImage.remaining()];
                 bImage.get(retreived);
                 InputStream in = new ByteArrayInputStream(retreived);
                 BufferedImage BI = ImageIO.read(in);
                 //effect
                 BI=updatePic(BI,effect,darknes);     
                 BufferedImage BI2,BI3;
                 BI2=createThumbnail(BI);
                 BI3= createProcessed(BI);
                 byte[] newPic,newPic2,newPic3;
                 newPic=toByte(BI,types[1]);
                 newPic2=toByte(BI2,types[1]);
                 newPic3=toByte(BI3,types[1]);
            
                 	length = newPic.length;
             		int thumblength= newPic2.length;
             	    int processedlength= newPic3.length;
             	    ByteBuffer buffer = ByteBuffer.wrap(newPic);
             	    ByteBuffer thumbbuf=ByteBuffer.wrap(newPic2);
             	    ByteBuffer processedbuf=ByteBuffer.wrap(newPic3);
           
               ps = session.prepare("UPDATE Pics SET image=?, thumb=?, processed=?, imagelength=?, thumblength=? ,processedlength=? WHERE picid =?");
               boundStatement = new BoundStatement(ps);
               rs = session.execute(boundStatement.bind(buffer,thumbbuf,processedbuf,length,thumblength,processedlength, picid));
                 
                 
       			}
       		}
       		
       }
   private byte[] toByte(BufferedImage input,String types) throws IOException
   {
	   ByteArrayOutputStream bs = new ByteArrayOutputStream();
	   ImageIO.write(input, types, bs);
       bs.flush();
       byte[] imageInByte = bs.toByteArray();
       bs.close();
       return imageInByte;
   }
   
    
    //Methods for managing profileImage
    public Pic getAvatar(String user){

        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            ResultSet rs = null;
            PreparedStatement ps = session.prepare("select image from userprofiles where login =?");
            BoundStatement boundStatement = new BoundStatement(ps);
           
            rs = session.execute(boundStatement.bind(user));
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


 
    
    public BufferedImage updatePic(BufferedImage newPic,String effect,int darkness) throws IOException
    {	
    	if(effect.equals("bnw"))
    	{newPic=grey(newPic);}
    	else if(effect.equals("serpia")){newPic=serpia(newPic,20);}
    	if(darkness<5){for(int i=0;i<10;i++){newPic=light(newPic,darkness);};
    	}else if(darkness>5){newPic=dark(newPic,darkness);}
    	
    return newPic;
    }




public java.util.LinkedList<Pic> getRandom()
{
	  java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
      Session session = cluster.connect("instagrim");
      PreparedStatement ps = session.prepare("select * from userpiclist ");
      ResultSet rs = null;
      BoundStatement boundStatement = new BoundStatement(ps);
      rs = session.execute(
              boundStatement.bind());
      if (rs.isExhausted()) {
          System.out.println("No Images returned");
          return null;
      } else {
    	  int i=0;
          for (Row row : rs) {
        	  i++;
        	  if(i<=6){
              String user=row.getString("user");
              java.util.UUID UUID = row.getUUID("picid"); 
              Pic pic = new Pic();
              pic.setUUID(UUID);
              pic.setUser(user);
              Pics.add(pic);

        	  }
              
              
          }
      }
      return Pics;	
}


}
