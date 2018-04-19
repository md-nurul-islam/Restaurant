package model;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Products {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("shipping")
    @Expose
    private Object shippingTo;


//    @SerializedName("shipping")
//    @Expose
//    private Shipping shipping;

//    @SerializedName("images")
//    @Expose
//    private ImageUrl image;

    @SerializedName("images")
    @Expose
    private ArrayList<JsonObject> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public ImageUrl getImage() {
//        return image;
//    }
//
//    public void setImage(ImageUrl image) {
//        this.image = image;
//    }


    public ArrayList<JsonObject> getImages() {
        return images;
    }

    public void setImages(ArrayList<JsonObject> images) {
        this.images = images;
    }

    public Object getShippingTo() {
        return shippingTo;
    }

    public void setShippingTo(Object shippingTo) {
        this.shippingTo = shippingTo;
    }
}