package dev.diskettefox.madridbus.Api_requests;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    public static String BASE_URL="https://datos.emtmadrid.es/";
    public static final String token="da0a4f54-aaa7-4f6f-b2e7-155d1ce0957d";
    private static Retrofit retrofit;

    public static Retrofit getStop(){
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient stop=new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(chain -> {
                        Request request=chain.request().newBuilder()
                                .addHeader(
                                        "X-ApiKey",token
                                )
                                .build();
                        return chain.proceed(request);
                    })
                    .build();
            retrofit =new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(stop)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
