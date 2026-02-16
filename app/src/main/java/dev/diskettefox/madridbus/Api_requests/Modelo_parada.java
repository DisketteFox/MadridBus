package dev.diskettefox.madridbus.Api_requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Modelo_parada {
    @SerializedName("data")
    private ArrayList<Parada> paradas=new ArrayList<>();
    
    public ArrayList<Parada> getParadas() {
        return paradas;
    }

    public class Parada{
        @SerializedName("node")
        private String idParada;
        @SerializedName("name")
        private String nombreParada;

        @SerializedName("lines")
        private String[] listaLineas;

        public String getIdParada() {
            return idParada;
        }

        public String getNombreParada() {
            return nombreParada;
        }
        public String[] getListaLineas() {
            return listaLineas;
        }
    }
}
