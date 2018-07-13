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
    private static String accessToken = "null";
//    private static String baseUrl = "http://10.0.2.2:8000";

    public static Retrofit getClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);//.addInterceptor(interceptor).build();
        //add header in case of authorization
        clientBuilder.addInterceptor((chain) -> {
                    Request original = chain.request();
                    Request.Builder reqBuilder = original.newBuilder()
                            .addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU2ODg5ZjM1MTA1Njk0YjY1ZDhlMDdlZjdlMTY2NzAwMjVjMmQ1MTNiZTAyZGVkMGQ1MTY2M2I4ZDk5MjQyNzBjNjE5YTUzMWNjNTRkZDRlIn0.eyJhdWQiOiIxIiwianRpIjoiZTY4ODlmMzUxMDU2OTRiNjVkOGUwN2VmN2UxNjY3MDAyNWMyZDUxM2JlMDJkZWQwZDUxNjYzYjhkOTkyNDI3MGM2MTlhNTMxY2M1NGRkNGUiLCJpYXQiOjE1MzEyMzg5NDUsIm5iZiI6MTUzMTIzODk0NSwiZXhwIjoxNTYyNzc0OTQ1LCJzdWIiOiIwMTI3OTAxMTA5NiIsInNjb3BlcyI6W119.DV7CGGeUtOA0jV66LjE957oRsFwnEJhsDx3bypW6M8CA8lJRAyxts6F2O0siBsmkwL6P7uIj9E1LKoKFaiL88WzSPbW7FHgEDYRT7hfSF8ifWMsj8bj05rEV1bZe9-t8lKBNTjEYeG1PmSx0-szyURjvaZuz8oM5yOnIIyUszlBo4_ddfRBtrg1CVzCaxu9WN583CCB_07Td9bhgFWnjKvYPtIpxeLYLigRqUC1H25Hf_hirjk0Osui9EcoY2_Vaq5Ufq9ltKL5ywcCN1Q3iNWiVUa5jwWRAbcznrJZheEpMnnGIfcRfER4ivfBXGI2ZsgiFYSDzZVcQKD2Op6at8PZHo7r_Rbiieyc27goumVEFLkFD8oSHJtYHGRHspwL1JovNxkv7Pwot1tKSEzWEjzQqyQHKhuWU0HEuxHpiDk3seYlMCD7cMrGDkNXW0jpaSkLAoY9uXqLxPZCAKIcCE8kwT_XnSx1C7h3BpkmRu9jfhZOtvE91rAdkR-4dXZowi9eydzhgfVtiQWmPKp3SsS2lgVEfxU_oai9du60IhiDALfjVHsWIl5H1KP8bvX905YDy7tca8E-6V328iJKwuHVbPU0ZWxcKAB2qb2RklHemt-2RtpafZshI7PJ5EJKornBNldKSK72SWH9nZBUIbGkQOGy0EdurzwvL2BMHOEI")
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
