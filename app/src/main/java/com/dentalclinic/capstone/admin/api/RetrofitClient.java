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
                            //for server
                            .addHeader("Authorization", "Bearer " + accessToken)
//                            .addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjU4MDU1YWQ1N2E1ODMzOGQ5NGQ0MmFmZmI5M2M3MWUwODFhOTRiMDA2ZWIxNDZkZjQ3ZjI0YTU1NzM2OGI5YjdiNWFlODY2ODA3MTYxYjI0In0.eyJhdWQiOiIxIiwianRpIjoiNTgwNTVhZDU3YTU4MzM4ZDk0ZDQyYWZmYjkzYzcxZTA4MWE5NGIwMDZlYjE0NmRmNDdmMjRhNTU3MzY4YjliN2I1YWU4NjY4MDcxNjFiMjQiLCJpYXQiOjE1MzE1NjI0MzcsIm5iZiI6MTUzMTU2MjQzNywiZXhwIjoxNTYzMDk4NDM3LCJzdWIiOiIwMTI3OTAxMTA5NyIsInNjb3BlcyI6W119.oQO5OaftKdDshcZcHpdNdBl0Hc-ST90Ga5Ryj-ifu9bJsWi_8N-5SSYJQrLMWbyBcL0oNnnabga2VeF2bxWcPJNaTOEXOj16KSezJ-buSk0pkBtp8Mazm_1E3CztYTPyUwD4P4D77rHjgoHIouF-sfHV35zYtKm5On-HKPcvGja_Qy2MN_1rJcilfqr9wjBoz-5whykm2OlSbi0gDfMsWJ0D9QRm39fz_xnwJdceUYjMKsQoXfIM7TKO9DeqlArEqmm0xWXBp6RX1BOdLASv8rI184347DDTo9I9L7gGlnAtHqDjPtAw0MffesrUcR2T7_0rl3rrWToYnxYVq4j4T1-Vr3EhdBh2lRfJCPrfzhf7zHBcwDLevKDrhF8ohYnHbff0ZS2Yi2Stsldk05mXGLR2A8JJetlTmwhVxZBCTqhXN_N4Ji8dM0bp737BzrBuXIaIBLPmywGrQXi5lNh7NP0Tu7fOOzP-G2DitLXCDNy8mW7MRXBj95qYj47rS1ta0MYs1YUrJ84DNEXdurGA4rNLyRFnzxlYkTPvhfUyQu7MoSM8_JQukzBMHWj-rOIiJWbF92qczrghP-elinlUP4SSkw-ccGa1P44WvotcFrv5j0AVvSoXjWTYwN1u1C86pZxspfQS0Kwfvq9N7qCQ0mDd27Gk86vWDcQPq0tJj2I")
//                            .addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjEyNWM0YTUyNTYxYmIxOThhOWFlZGNlNzRkZDcxODA3YjU0NGI4M2Q5ZjJkNDY5ODBlYWY5Yjg0MGNjOTRjNjQ2Nzk1NWFiMmU2ZWZjNjBjIn0.eyJhdWQiOiIzIiwianRpIjoiMTI1YzRhNTI1NjFiYjE5OGE5YWVkY2U3NGRkNzE4MDdiNTQ0YjgzZDlmMmQ0Njk4MGVhZjliODQwY2M5NGM2NDY3OTU1YWIyZTZlZmM2MGMiLCJpYXQiOjE1MzE4MzY4NTMsIm5iZiI6MTUzMTgzNjg1MywiZXhwIjoxNTYzMzcyODUzLCJzdWIiOiIwMTI3OTAxMTA5NiIsInNjb3BlcyI6W119.UmusS5l3TWQ5AlTIjBscmb3iCS4-lhegOFylZOvIAS_Vwc1xGNkYgfr42aSPsc8gUSCgozsj7yX1cSNSrKi-Cu-KuzgSGHdbdHnFr6TDOXurXVtnbwuBaQkr9wob76m-tRnH3L5d0b41N9AOKOUrNW464o7Zapr09LCunG-VGg_U52GBEptz_4RW_RdUNVeRZdCtqoppkotA72JUufPJE4we8It_Op-dI3pqAOIXwtWwU34A2HZkEKLWrPAbqDb6BjAUXfcB32OX5-0uYfGJQYzTqLl5Z48mcvb2cW8rPL5FWH7Fr7EArr1iotW_TI0px5i5FZfetxKD6bl_l4DBKKaQFhTnlY8rUtNiqGDO7rMye-cx2HLmVamLL2KEo2pQXSU6zvSfFr0IwWzJ27BXpjUDIjtcUJXgWoAVlmpVivoFDdHRu59adSyG0OQkIM9fm2yuU-qBEJ6tMBkgxo4A2LZM7xwFxfA4BbQpYZWxcrjoVuNsF096SvepmSgkR3_TvbJ5tpDA67U_rjLQ-0i0sRrm2Ouuk2xuSpJsGF43CcxVizyzpWC4KwCwyFdN6l2rTz4EhkR0B2uMhQMmkvC-YKf54fuM8qkooS-ltdl7Kldf2fWYengSM15WS0xzoX0FRfWfARpB6AbjKJ10YWtzjsrSPEcyxXg6bumpEmIlgNg")
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
