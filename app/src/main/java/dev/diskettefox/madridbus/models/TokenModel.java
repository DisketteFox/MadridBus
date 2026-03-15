package dev.diskettefox.madridbus.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TokenModel {
    @SerializedName("data")
    private ArrayList<TokenModel.Data> data = new ArrayList<>();
    @SerializedName("code")
    private int code;

    public ArrayList<TokenModel.Data> getData() {
        return data;
    }
    public int getCode() {
        return code;
    }

    public static class Data {
        @SerializedName("accessToken")
        private String accessToken;
        public String getAccessToken() {
            return accessToken;
        }
    }
}