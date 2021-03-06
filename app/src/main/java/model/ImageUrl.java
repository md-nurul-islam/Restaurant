package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RR on 04-Mar-18.
 */

public class ImageUrl {
    @SerializedName("src")
    @Expose
    private String source_url;

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
