package wikthewiz.climbinglist.server.rest.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("Bohus")
    private List<Route> bohusList;

    @SerializedName("Got")
    private List<Route> gotList;

    public Person() {
        this.bohusList = new ArrayList<Route>();
        this.gotList = new ArrayList<Route>();
    }

    public Person(List<Route> bohusList, List<Route> gotList) {
        this.bohusList = bohusList;
        this.gotList = gotList;
    }

    public List<Route> getBohus() {
        return this.bohusList;
    }

    public List<Route> getGot() {
        return this.gotList;
    }
}