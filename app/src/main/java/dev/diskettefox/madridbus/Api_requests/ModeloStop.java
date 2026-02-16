package dev.diskettefox.madridbus.Api_requests;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ModeloStop {
    @SerializedName("data")
    private ArrayList<Data> stops = new ArrayList<>();

    public ArrayList<Data> getStopsData() {
        return stops;
    }

    public class Data {
        @SerializedName("stops")
        private List<Stop> stops;

        public List<Stop> getStops() {
            return stops;
        }
    }

    public class Stop {
        @SerializedName("stop")
        private String stopId;
        @SerializedName("name")
        private String name;
        @SerializedName("dataLine")
        private List<Dataline> dataLine;

        public String getStopId() {
            return stopId;
        }
        public String getName() {
            return name;
        }
        public List<Dataline> getDataLine() {
            return dataLine;
        }
    }

    public class Dataline {
        @SerializedName("line")
        private String lineId;
        @SerializedName("label")
        private String label;
        @SerializedName("direction")
        private String direction;
        @SerializedName("headerA")
        private String headerA;
        @SerializedName("headerB")
        private String headerB;

        public String getLineId() {
            return lineId;
        }
        public String getLabel() {
            return label;
        }
        public String getDirection() {
            return direction;
        }
        public String getHeaderA() {
            return headerA;
        }
        public String getHeaderB() {
            return headerB;
        }
    }
}
