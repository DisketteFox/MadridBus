package dev.diskettefox.madridbus.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface{

    // "StopModel" to be changed
    @POST("/v1/transport/busemtmad/stops/list/")
    Call<StopModel> getStopsList(@Header("accessToken") String accessToken);
    @GET("/v1/transport/busemtmad/stops/{stopId}/detail")
    Call<StopModel> getStop(@Path("stopId") Integer stopId, @Header("accessToken") String accessToken);

    @POST("/v2/transport/busemtmad/stops/{stopId}/arrives/{lineArrive}/")
    Call<StopModel> getLineTime(@Path("stopId") Integer stopId, @Path("lineId") Integer lineId, @Header("accessToken") String accessToken);

    // "StopModel" to be changed
    @GET("https://datos.emtmadrid.es/v2/transport/busemtmad/lines/info/{dateref}/")
    Call<StopModel> getLines(@Header("accessToken") String accessToken);
    
    @GET("/v1/transport/busemtmad/lines/{lineId}/info/20260218/")
    Call<LineModel> getLineDetail(@Path("lineId") Integer lineId, @Header("accessToken") String accessToken);

}
