package dev.diskettefox.madridbus.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TimeModel {
    @SerializedName("data")
    private ArrayList<TimeModel.Data> data = new ArrayList<>();

    public ArrayList<TimeModel.Data> getData() {
        return data;
    }

    public static class Data {
        @SerializedName("Arrive")
        private List<TimeModel.Line> lines;
        public List<TimeModel.Line> getLines() {
            return lines;
        }
    }

    public static class Line {
        @SerializedName("estimateArrive")
        private String time;
        @SerializedName("DistanceBus")
        private String distance;
        public String getTime() {
            return time;
        }
        public String getDistance() {
            return distance;
        }
    }
}
