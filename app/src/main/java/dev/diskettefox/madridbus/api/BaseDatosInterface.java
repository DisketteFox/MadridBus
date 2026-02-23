package dev.diskettefox.madridbus.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BaseDatosInterface {
    @POST("/api/addfavorito/")
    Call<BaseDatosModel>anadeFavorito(@Body BaseDatosModel parada);

    // aun no fue testeado ni creado.
    @POST("/api/deletefavorito/")
    Call<BaseDatosModel>elimidaDFavoritos(@Body BaseDatosModel parada);

    @GET("/api/getfavoritos/")
    Call<BDMRespuesta>llamaFavoritos();
    // aun no fue testeado.
}
