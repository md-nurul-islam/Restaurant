package model;


import android.os.Parcelable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Products implements Serializable {


    @SerializedName("payment_method_title")
    @Expose
    private String paymentMethodTitle;

    @SerializedName("date_modified")
    @Expose
    private String delivery;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("status")
    @Expose
    private String status;

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
    private JsonElement billing;

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

    @SerializedName("date_created")
    @Expose
    private String dateCreated;

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

    public JsonElement getBilling() {
        return billing;
    }

    public void setBilling(JsonElement billing) {
        this.billing = billing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Products(String status, String name, String id, String total, JsonElement shippingTo, JsonElement billing, List<SingleItemModel> itemList, ArrayList<JsonObject> images, String dateCreated) {
        this.status = status;
        this.name = name;
        this.id = id;
        this.total = total;
        this.shippingTo = shippingTo;
        this.billing = billing;
        this.itemList = itemList;
        this.images = images;
        this.dateCreated = dateCreated;
    }

    public Products() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }
}