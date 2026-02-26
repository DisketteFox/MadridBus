package dev.diskettefox.madridbus.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BBDDAO {

    @SerializedName("data")
    ArrayList<Respuestas>favoritos=new ArrayList<>();

    public ArrayList<Respuestas> getFavoritos() {
        return favoritos;
    }

    public static class Respuestas{

        @SerializedName("parada_id")
        private String parada_id;

        @SerializedName("is_favorite")
        private boolean estado;

        public boolean isEstado() {
            return estado;
        }

        public String getParada_id() {
            return parada_id;
        }
    }
}
