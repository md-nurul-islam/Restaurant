package model;


import com.google.gson.JsonElement;
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

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("shipping")
    @Expose
    private JsonElement shippingTo;

    @SerializedName("billing")
    @Expose
    private Object billing;

    @SerializedName("line_items")
    @Expose
    private List<SingleItemModel> itemList;


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

    public JsonElement getShippingTo() {
        return shippingTo;
    }

    public void setShippingTo(JsonElement shippingTo) {
        this.shippingTo = shippingTo;
    }

    public List<SingleItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<SingleItemModel> itemList) {
        this.itemList = itemList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Object getBilling() {
        return billing;
    }

    public void setBilling(Object billing) {
        this.billing = billing;
    }

}