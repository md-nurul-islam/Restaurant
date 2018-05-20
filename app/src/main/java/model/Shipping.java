package model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RR on 28-Feb-18.
 */

public class Shipping {
    @SerializedName("first_name")
    @Expose
    private Shipping firstName;

    public Shipping getFirstName() {
        return firstName;
    }

    public void setFirstName(Shipping firstName) {
        this.firstName = firstName;
    }

}
