package dev.diskettefox.madridbus.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StopsModel {

    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private ArrayList<Stops> stops = new ArrayList<>();
    public ArrayList<Stops> getStopsData() {
        return stops;
    }
    public String getCode() {
        return code;
    }

    public static class Stops {

        @SerializedName("node")
        private String stopId;
        @SerializedName("name")
        private String stopName;

        public String getStopName() {
            return stopName;
        }

        public String getStopId() {
            return stopId;
        }
    }


}
