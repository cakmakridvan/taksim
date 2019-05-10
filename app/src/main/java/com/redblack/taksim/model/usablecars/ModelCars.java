package com.redblack.taksim.model.usablecars;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCars {

    @SerializedName("carNo")
    @Expose
    private String carNo;
    @SerializedName("carType")
    @Expose
    private Integer carType;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("direction")
    @Expose
    private Integer direction;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;

    @SerializedName("carModel")
    @Expose
    private String carModel;

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
