package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Muhib on 5/17/2018.
 */

public class ShippingAddressaModel {
    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("address_1")
    @Expose
    private String address1;

    @SerializedName("address_2")
    @Expose
    private String getAddress2;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getGetAddress2() {
        return getAddress2;
    }

    public void setGetAddress2(String getAddress2) {
        this.getAddress2 = getAddress2;
    }
}
