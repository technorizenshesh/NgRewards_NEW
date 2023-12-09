package main.com.ngrewards.restapi;

import java.util.concurrent.TimeUnit;

import main.com.ngrewards.constant.BaseUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by technorizen on 14/2/18.
 */

public class ApiClient {
    public static final String BASE_URL = BaseUrl.baseurl;
    private static Retrofit retrofit = null;
    private static ApiInterface apiInterface = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            okhttp3.logging.HttpLoggingInterceptor interceptor = new okhttp3.logging.HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(50, TimeUnit.SECONDS) // connect timeout
                    .writeTimeout(50, TimeUnit.SECONDS) // write timeout
                    .readTimeout(50, TimeUnit.SECONDS)
                    .addInterceptor(interceptor); // read timeout
            OkHttpClient client = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getApiInterface() {
        if (apiInterface == null)
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface;
    }
}
