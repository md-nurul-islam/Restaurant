package model;


/**
 * Created by Fahima on 5/19/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateModel {

    @SerializedName("status")
    private String status;

    @SerializedName("meta_data")
    private List<HashMap> metaData = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HashMap> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<HashMap> metaData) {
        this.metaData = metaData;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}