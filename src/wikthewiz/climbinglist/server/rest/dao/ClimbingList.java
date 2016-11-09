package wikthewiz.climbinglist.server.rest.dao;

import com.google.gson.annotations.SerializedName;

public class ClimbingList {

    @SerializedName("Martin")
    private final Person martin;

    @SerializedName("Daniel")
    private final Person daniel;

    @SerializedName("Patrik")
    private final Person patrik;

    public ClimbingList(Person martin, Person daniel, Person patrik) {
        this.martin = martin;
        this.daniel = daniel;
        this.patrik = patrik;
    }

    public Person getMartin() {
        return martin;
    }

    public Person getDaniel() {
        return daniel;
    }

    public Person getPatrik() {
        return patrik;
    }
}