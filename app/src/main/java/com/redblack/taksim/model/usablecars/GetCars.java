package com.redblack.taksim.model.usablecars;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCars {

    @SerializedName("cars")
    @Expose
    private ArrayList<ModelCars> data_list;

    public ArrayList<ModelCars> getData_list() {
        return data_list;
    }
}
