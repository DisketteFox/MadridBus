package dev.diskettefox.madridbus.Api_requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloStop {
    @SerializedName("data")
    private ArrayList<Stops> stopsDates=new ArrayList<>();

    public ArrayList<Stops> getStop() {
        return stopsDates;
    }

    public class Stops {
        @SerializedName("stops")
        private Stop stop;

        public Stop getStop() {
            return stop;
        }
    }

    public class Stop {
        @SerializedName("stop")
        private String idParada;
        @SerializedName("name")
        private String nombreParada;

        @SerializedName("dataLine")
        private DatosDLinea datosDLinea;

        public String getIdParada() {
            return idParada;
        }

        public String getNombreParada() {
            return nombreParada;
        }

        public DatosDLinea getDatosDLinea() {
            return datosDLinea;
        }

    }

    public class DatosDLinea{
        @SerializedName("line")
        private String idDLinea;
        @SerializedName("label")
        private String numeroDlinea;
        @SerializedName("direction")
        private String direccion;
        @SerializedName("headerA")
        private String DestinoA;
        @SerializedName("headerB")
        private String DestinoB;

        public String getIdDLinea() {
            return idDLinea;
        }

        public String getNumeroDlinea() {
            return numeroDlinea;
        }

        public String getDireccion() {
            return direccion;
        }

        public String getDestinoA() {
            return DestinoA;
        }

        public String getDestinoB() {
            return DestinoB;
        }
    }
}