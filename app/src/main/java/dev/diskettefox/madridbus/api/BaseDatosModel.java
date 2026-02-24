package dev.diskettefox.madridbus.api;

public class BaseDatosModel {
    String parada_id;
    Boolean is_favorite;
    public BaseDatosModel(String parada_id, Boolean is_favorite) {
        this.parada_id = parada_id;
        this.is_favorite = is_favorite;
    }
    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public String getParada_id() {
        return parada_id;
    }
}