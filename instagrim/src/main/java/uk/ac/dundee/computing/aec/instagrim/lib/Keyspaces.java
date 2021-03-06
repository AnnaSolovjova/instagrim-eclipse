package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace if not exists instagrimas  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreatePicTable = "CREATE TABLE if not exists instagrimas.Pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " title varchar,"
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + "  processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String Createuserpiclist = "CREATE TABLE if not exists instagrimas.userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (picid,user)\n"
                    + ") ";
            String CreateAddressType = "CREATE TYPE if not exists instagrimas.address (\n"
                    + "      street text,\n"
                    + "      city text,\n"
                    + "      zip int\n"
                    + "  );";
            String CreateUserProfile = "CREATE TABLE if not exists instagrimas.userprofiles (\n"
                    + "      login text PRIMARY KEY,\n"
                     + "     password text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "      email set<text>,\n"
                    + " 		image blob,"
                    + "      picId uuid,\n  "
                    + "      addresses  map<text, frozen <address>>\n"
                    + "  );";
            
            
            String Comments2 = "CREATE TABLE if not exists instagrimas.comments2 ("
                    + " user varchar,\n"
                    + " text varchar,\n "
                    + " commentadded timestamp,\n"
                    + " picid uuid,\n  "
                    + " PRIMARY KEY (picid,commentadded)"
                    + ")";
            
            String likes= "CREATE TABLE if not exists instagrimas.likes ("
                    + " user varchar,\n"
                    + " picid uuid,\n  "
                    + " PRIMARY KEY (picid,user)"
                    + ")";
            
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
               
            } catch (Exception et) {
                System.out.println("Can't create instagrim " + et);
            }

            //now add some column families 
          //  System.out.println("" + CreatePicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create tweet table " + et);
            }
           // System.out.println("" + Createuserpiclist);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
           // System.out.println("" + CreateAddressType);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateAddressType);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address type " + et);
            }
           // System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address Profile " + et);
            }
            //Create comments
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Comments2);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create  " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(likes);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create  " + et);
            }
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}
