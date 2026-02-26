package dev.diskettefox.madridbus.api;

import dev.diskettefox.madridbus.models.HelloModel;
import dev.diskettefox.madridbus.models.LineModel;
import dev.diskettefox.madridbus.models.StopModel;
import dev.diskettefox.madridbus.models.TimeModel;
import dev.diskettefox.madridbus.models.TokenModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface{

    // Interface for ping
    @GET("/v1/hello")
    Call<HelloModel> getHello();

    // Interface for retrieving the token
    @GET("/v3/mobilitylabs/user/login/")
    Call<TokenModel> getToken(
            @Header("X-ClientId") String clientId,
            @Header("passKey") String passKey
    );

    @GET("/v3/mobilitylabs/user/login/")
    Call<TokenModel> getTokenByUser(
            @Header("email") String clientId,
            @Header("password") String passKey
    );

    // Interfaces for stops
    @POST("/v1/transport/busemtmad/stops/list/")
    Call<StopModel> getStopsList(
            @Header("accessToken") String accessToken
    );
    @GET("/v1/transport/busemtmad/stops/{stopId}/detail")
    Call<StopModel> getStop(
            @Path("stopId") Integer stopId,
            @Header("accessToken") String accessToken
    );

    @POST("/v2/transport/busemtmad/stops/{stopId}/arrives/{lineArrive}/")
    Call<TimeModel> getTime(
            @Path("stopId") Integer stopId,
            @Path("lineArrive") Integer lineId,
            @Header("accessToken") String accessToken,
            @Body TimeRequest body
    );

    // Interfaces for lines
    @GET("/v2/transport/busemtmad/lines/info/{dateref}/")
    Call<LineModel> getLines(
            @Header("accessToken") String accessToken
    );

    @GET("/v1/transport/busemtmad/lines/{lineId}/info/20260218/")
    Call<LineModel> getLineDetail(
            @Path("lineId") Integer lineId,
            @Header("accessToken") String accessToken
    );

}