package dev.diskettefox.madridbus.Api_requests;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    public static String BASE_URL="https://datos.emtmadrid.es";
    public static final String token="4f6e5f2f-2f96-4415-b634-78ec45d10753";
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
                                        "Authorization",
                                        "Beader " +token
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
