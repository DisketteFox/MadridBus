package dev.diskettefox.madridbus.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TokenModel {
    @SerializedName("accessToken")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    };
}
