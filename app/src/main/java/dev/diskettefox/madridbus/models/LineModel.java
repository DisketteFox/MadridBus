package dev.diskettefox.madridbus.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class LineModel {
    @SerializedName("data")
    private ArrayList<Data> data = new ArrayList<>();

    public ArrayList<Data> getData() {
        return data;
    }

    public static class Data {
        @SerializedName("line")
        private int lineId;
        @SerializedName("nameA")
        private String nameA;
        @SerializedName("nameB")
        private String nameB;
        @SerializedName("label")
        private String label;

        private String color;

        public int getLineId() {
            return lineId;
        }
        public String getNameA() {
            return nameA;
        }
        public String getNameB() {
            return nameB;
        }
        public String getLabel() {
            return label;
        }
        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}