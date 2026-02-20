package dev.diskettefox.madridbus.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BaseDatosInterface {
    @POST("/api/favoritos/")
    Call<BaseDatosModel>anadeFavorito(@Body BaseDatosModel parada);

    // aun no fue testeado.
    @GET("/api/favoritos/")
    Call<BaseDatosModel>llamaFavoritos();
}
