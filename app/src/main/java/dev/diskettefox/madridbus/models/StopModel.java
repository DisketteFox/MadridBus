package dev.diskettefox.madridbus.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class StopModel {
    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private ArrayList<Data> stops = new ArrayList<>();
    public ArrayList<Data> getStopsData() {
        return stops;
    }
    public String getCode() {
        return code;
    }

    public static class Data {
        @SerializedName("stops")
        private List<Stop> stops;
        public List<Stop> getStops() {
            return stops;
        }
    }

    public static class Stop {
        @SerializedName("stop")
        private String stopId;
        @SerializedName("name")
        private String name;
        @SerializedName("dataLine")
        private List<Dataline> dataLine;
        private Boolean favorite=Boolean.FALSE;
        public String getStopId() {
            return stopId;
        }
        public String getName() {
            return name;
        }
        public List<Dataline> getDataLine() {
            return dataLine;
        }
        public Boolean isFavorite() {
            return favorite;
        }
        public void setFavorite(Boolean favorito) {
            favorite = favorito;
        }
    }

    public static class Dataline {
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

        private String timeArriving;
        private String timeNext;

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

        public String getTimeArriving() {
            return timeArriving;
        }

        public void setTimeArriving(String timeArriving) {
            this.timeArriving = timeArriving;
        }

        public String getTimeNext() {
            return timeNext;
        }

        public void setTimeNext(String timeNext) {
            this.timeNext = timeNext;
        }
    }
}
