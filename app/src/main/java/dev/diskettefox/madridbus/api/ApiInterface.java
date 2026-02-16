package dev.diskettefox.madridbus.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface{
    @GET("/v1/transport/busemtmad/stops/{stopId}/detail")
    Call<StopModel> getStop(@Path("stopId") Integer stopId, @Header("accessToken") String accessToken);

    // "StopModel" to be changed
    @POST("/v1/transport/busemtmad/stops/list/")
    Call<StopModel> getStopsList(@Header("accessToken") String accessToken);

    // "StopModel" to be changed
    @GET("https://datos.emtmadrid.es/v2/transport/busemtmad/lines/info/{dateref}/")
    Call<StopModel> getLinesInfo(@Header("accessToken") String accessToken);

    // "StopModel" to be changed + doesn't work at all
    @GET("/v1/transport/busemtmad/lines/{lineId}/info/{dateref}/")
    Call<StopModel> getLineDetail(@Path("lineId") Integer lineId, @Header("accessToken") String accessToken);

}
