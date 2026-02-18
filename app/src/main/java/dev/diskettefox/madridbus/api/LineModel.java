package dev.diskettefox.madridbus.api;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

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
    }
}
