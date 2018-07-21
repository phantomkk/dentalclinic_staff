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
                            .addHeader("Authorization", "Bearer " + accessToken)
                            //localhost+
//                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBmYzc0M2UxMGE5MGYwOTRmNDUyMGIxZmViMTljNWUzNDU1ZDNkMTQ1MmJjNDViNzAxMWNkMzVhMWNjOGY2ZmQ1NWJmNGM5NDg2YjM2YzZmIn0.eyJhdWQiOiIzIiwianRpIjoiMGZjNzQzZTEwYTkwZjA5NGY0NTIwYjFmZWIxOWM1ZTM0NTVkM2QxNDUyYmM0NWI3MDExY2QzNWExY2M4ZjZmZDU1YmY0Yzk0ODZiMzZjNmYiLCJpYXQiOjE1MzIwMTc4NjEsIm5iZiI6MTUzMjAxNzg2MSwiZXhwIjoxNTYzNTUzODYxLCJzdWIiOiIwMTI3OTAxMTA5NiIsInNjb3BlcyI6W119.tziKzJnoHBC4Knm6jhOu43S6q5UauzH3Sr1zqxkTCJOCrgYPyQgCke1Eiyc2EbNLZsboWEsKeeLWNvpfPpMonPv0HAqqcJxgynizDPVb7gTxn9BE_5VZ45-FWNfayS8EMX9BSy76CvM-Kv5UlFxQrJSSbrHoHkFXzwLu11djdwqrIBzm9oLhlUVj_S0TaS9ITPsO4n7rHk3EjxKBPW9f16RevDZIl5jXQSqJjUO8RQBogeqa0txBNb2fPRN1PGa3axSU499SaRZaWuv-IUGMywjwRpfPtvW_M6SVFwzq9B6ucfTYHyyp2i7KnLLXDA6wrh69oZtFzGkFcHvaAXCg39IptEKn7S8G25ZKPClc0xnyMyGQIoq1KdvI0eyiA5T-f0lnwVAKpb2zyQNFIFX1PvVKXgZ9KAOMX7JSSzWvcAxF-YWmQ27UX_HcrQVonplaZyGAAi5WXoOxxl_edUDWe6xXQq8NTDl7_I_t7JF4J_HrDklOX0UowpHkxpDbpTBS_07kfRRj2E0jPxwdov7os89jDH1xnkRWVYnapjs1Ir8npXP-Fl6ot8Z9AsTIipaKgfBixQ-bVEn2MKdRX3RQtN6OZb-bG41wgblZVX7nclpNNc1SJbkDsKvxSaKYUU-NWNHEYDigZDZFMXeTO5XM3ARBwAzj-xC6eR9mckjXP1w")
                            //server
//                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQxZWU1OWIxZjZiNzI1YzVjNTMzZmE2N2IzMDRmNzc2YzViODgyYWNhNmM2MDFjZWNiMWU4Njk2OTgwODJhMjdhMWY5NWYzNDc3ZWRkM2ZhIn0.eyJhdWQiOiIxIiwianRpIjoiZDFlZTU5YjFmNmI3MjVjNWM1MzNmYTY3YjMwNGY3NzZjNWI4ODJhY2E2YzYwMWNlY2IxZTg2OTY5ODA4MmEyN2ExZjk1ZjM0NzdlZGQzZmEiLCJpYXQiOjE1MzIwMTkzMDQsIm5iZiI6MTUzMjAxOTMwNCwiZXhwIjoxNTYzNTU1MzA0LCJzdWIiOiIwMTI3OTAxMTA5NiIsInNjb3BlcyI6W119.MyXe3llq5Mefd6ub-1Cr0S36nNBaQVYhsm4lg6_0k7K_Gm3-KEQEMLP7mNoTl60ANH7CbhWXrd5KT9prfVyZDUzB33xhFL_b07_CBBCXtP3xgXbBkyC4Bs-1p6xCOspbJsbzJEsdjwH51C8MtFNPt7ivHbbh2o-Ak3TGE9u6df5DRo53SrMc4hCtJBHvrJeJfDVpcwHxSvYKWBT4wrcKlcG00ugdxULIAlEm0S7S8-jJ1F-m41AtfcXwRdD5F8s4WCS3CYmNOePOTqLWFqZcZPw9tP5oXv2MKwW6L5X1qRcsCzje1V7g14MEQBN1iYGnhl75j7vtrlZn4ACOJXl95--uDaxz8BJXs1auGP2DNnkaheFGfrd20ETHrBjRa2-H4epAuOT0rwz8ad5qz8cvitjT4YdI-RjeqgvfWhTF_0SWS5r56f3OG8C7tTBQUv89e1t5jQ-gn2iMwLWGle1A6bNJiXS1MDTQG_R9ePuZba5HHHYvFbek2CuGbJeL-wz3Orjry7DxtW5k-dVazV28mgFv20sadLC1ow050feY1P1m4NwXCsl8-5H9Gbu4u9h9oYDaEUSnciA-NYZ7SuIC5krBpTKzeIZ3quDzkWK4Bl4p9zhMhxURmuj1i07eeAYlUF7E2LZ90cKZVNauVzWmwp8dT5c15OomPqkiM4z59mk")
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
