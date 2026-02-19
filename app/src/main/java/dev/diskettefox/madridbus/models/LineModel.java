package dev.diskettefox.madridbus.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.api.StopModel;

public class LineModel {
    @SerializedName("data")
    private ArrayList<StopModel.Data> stops = new ArrayList<>();

    public ArrayList<StopModel.Data> getStopsData() {
        return stops;
    }

    public static class Data {
        @SerializedName("stops")
        private List<StopModel.Stop> stops;

        public List<StopModel.Stop> getStops() {
            return stops;
        }
    }

    public static class Stop {
        @SerializedName("stop")
        private String stopId;
        @SerializedName("name")
        private String name;
        @SerializedName("dataLine")
        private List<StopModel.Dataline> dataLine;

        public String getStopId() {
            return stopId;
        }
        public String getName() {
            return name;
        }
        public List<StopModel.Dataline> getDataLine() {
            return dataLine;
        }
    }
}
