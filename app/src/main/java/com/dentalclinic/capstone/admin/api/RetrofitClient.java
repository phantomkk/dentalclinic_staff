package com.dentalclinic.capstone.admin.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lucky on 13-Sep-17.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static String baseUrl = "http://150.95.104.237";
//    private static String accessToken = "null";
    private static String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjU2MGQ5NzlhODE4MDEwMDMzZWZmYmFkMzY4OTg1N2U5YWRmNGRlMjQ1ZGM5NzZhYzM3NmNjNjhhZTU4MmY2NTQyYzFjMTJlNzE4ZTlkODAyIn0.eyJhdWQiOiIxIiwianRpIjoiNTYwZDk3OWE4MTgwMTAwMzNlZmZiYWQzNjg5ODU3ZTlhZGY0ZGUyNDVkYzk3NmFjMzc2Y2M2OGFlNTgyZjY1NDJjMWMxMmU3MThlOWQ4MDIiLCJpYXQiOjE1MzEzMTY4NTksIm5iZiI6MTUzMTMxNjg1OSwiZXhwIjoxNTYyODUyODU5LCJzdWIiOiIwMTI3OTAxMTA5NiIsInNjb3BlcyI6W119.q47Ddr8LDYJqPz4LoQsCQ-gENmettdeqrcRrGjzXI_s5-Ydz0bmdZ5F_QHOu7B-G6GwTgsNtZZl4f6Y8lHndDpDvZf7K-qSOoR7Xbs-1z_xqXbgNLqDig1b4JvxCKN_fSa8Z1TH3Ro0vGrl3uHRp-tRR4EYgmfSrBF1FGbqAmGAdyazzZxUOky7sZURnpG5lDJUcPZEZ4I8mxoLDmJcw2bWC6QtecEUCq2s_SIeI9pQuJFXdzBsChFeCRvi0w01aPBXVkcw2zTLcI9tev64CO1w85wkWxmNcvCbgFIUb4H13MDZ0cXsV4S-dZ8kp3y2VOwa0AWAVHbjOmYmJj665GDKu0h7R1LoDTm9A7MSPDS9xtu-8MF30MC_pX3fchubW7hjWVlSGNnfI6ZA6jFYRrqYDPHqLDP21jKS5_RmsElZ73Rbxzj94EeQt7Fig9YWdW2awSGCvi_FHpSiDQyzAoEvtS3GKF6F3V3VGxN785bdj97fKnANc0LZp5ZiOVN9nmTCp7fBL-nAx2XDNaGyqIVjKJvwj_F2pIGtWT1s8AXUN2oF_z_BsQspupdRJxGaTdYb1qks_nxB0kO0hU4Lx-A1AZQ6bqOl7VnD4gnjeJ5C65ecO58amsbKk5TpTgBNt4mMOlPOTya8TWMdASqN1AXh4uEmfzntEdRZ9mxj7xi0";
//    private static String baseUrl = "http://10.0.2.2:8000";

    public static Retrofit getClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);//.addInterceptor(interceptor).build();
        //add header in case of authorization
        clientBuilder.addInterceptor((chain) -> {
                    Request original = chain.request();
                    Request.Builder reqBuilder = original.newBuilder()
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .addHeader("Accept", "application/json");
                    Request request = reqBuilder.build();
                    return chain.proceed(request);
                }
        );
        //add log for retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);
        OkHttpClient client = clientBuilder.build();
        //Gson builder
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static void setBaseUrl(String mBaseUrl) {
        baseUrl = mBaseUrl;
    }

    public static Retrofit getClient(String url) {
        setBaseUrl(url);
        return getClient();
    }
}
