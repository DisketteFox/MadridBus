package dev.diskettefox.madridbus.Api_requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface{

    /*@GET("/v1/transport/busemtmad/stops/1023/detail/")
    Call<Modelo_parada> testApiKey();*/

    @POST("/v1/transport/busemtmad/stops/list/")
    Call<Modelo_parada>getAllParadas(@Header("accessToken") String accessToken);

    @GET("/v1/transport/busemtmad/stops/{stopId}/detail")
    Call<Modelo_parada> getStop(@Path("stopId") String stopId, @Header("accessToken") String accessToken);


    /*@GET("endpoint") ejemplos de busqueda
    Call<Elemento> getElementos();
    @GET("endpoint")
    Call<Elemento>getElementoByName(@Query("filter[text]") String nombre);*/

}
