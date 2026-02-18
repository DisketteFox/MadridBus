package dev.diskettefox.madridbus.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaseDatosInterface {
    @POST("admin/Favoritos/")
    Call<BaseDatosModel>anadeFavorito(@Body BaseDatosModel parada);
}
