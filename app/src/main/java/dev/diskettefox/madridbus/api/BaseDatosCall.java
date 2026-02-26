package dev.diskettefox.madridbus.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseDatosCall {
    public static String BASE_URL="http://192.168.56.1:8000/";
    private static Retrofit retrofit;

    public static Retrofit getBBDD(){
        if (retrofit==null){
            HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client=new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client).build();
        }
        return retrofit;
    }
}