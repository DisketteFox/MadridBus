package dev.diskettefox.madridbus.Api_requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface{
    @GET("/v1/transport/busemtmad/stops/1023/detail/")
    Call<Modelo_parada> testApiKey();

    /*@GET("endpoint") ejemplos de busqueda
    Call<Elemento> getElementos();
    @GET("endpoint")
    Call<Elemento>getElementoByName(@Query("filter[text]") String nombre);*/

}
