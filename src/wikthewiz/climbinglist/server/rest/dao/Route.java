package wikthewiz.climbinglist.server.rest.dao;

import com.google.gson.annotations.SerializedName;

public class Route{
    @SerializedName("Name")
    private String name;
    
    @SerializedName("IsDone")
    private boolean isDone;
    
    public Route(String name, boolean isDone){
        this.name = name;
        this.isDone = isDone;
    }
    
    public Route(String name){
        this.name = name;
        this.isDone = false;
    }
    
    public String getName(){
        return this.name;
    }
    
    public boolean isDone(){
        return this.isDone;
    }
}