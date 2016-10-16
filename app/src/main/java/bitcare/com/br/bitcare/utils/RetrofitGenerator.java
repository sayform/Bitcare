package bitcare.com.br.bitcare.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rafael on 16/10/2016.
 */

public class RetrofitGenerator {

    private static final String API_BASE_URL = "https://9bdedf90-c878-46b8-b333-02a89091e354-bluemix.cloudant.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        final String basic =
                "Basic YmVsc2VhcmlzZG93bmV3c3RyeWluc2hvOjJjNjBjOTZhYmRkNGVhYjJlYjUxN2Q1YzI2ZjM3NTM5MzM5NGY2ZTY=";

        httpClient.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", basic).build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
