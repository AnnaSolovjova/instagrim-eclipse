package uk.ac.dundee.computing.aec.instagrim.stores;

import java.nio.ByteBuffer;

public class Comment {
private String comment;
private String user;
private String date;

public void setComment(String comment,String user,String date){
    this.comment=comment;
    this.user=user;
    this.date=date;
}
public String getComment(){
    return comment;
}

public String getUser(){
    return user;
}

public String getDate(){
    return date;
}
}
