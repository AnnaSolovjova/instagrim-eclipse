package uk.ac.dundee.computing.aec.instagrim.stores;

public class States {
	
	private boolean Change;
	
	boolean change=false;
    String Username=null;

    public void setUsername(String name){
        this.Username=name;
    }
    public String getUsername(){
        return Username;
    }
    public void setChangeTrue(){
        change=true;
    }
    public void setChangeFalse(){
        change=false;
    }
    
   
    public boolean getChange(){
        return change;
    }

}
