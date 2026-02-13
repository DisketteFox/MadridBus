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
        @SerializedName("stop")
        private String idParada;
        @SerializedName("name")
        private String nombre;
        private DatosDLinea datosDLinea;

    }
    public class DatosDLinea{
        "dataLine"
                :"label",
                "direction",
                "headerA",
                "headerB",

    }
}
