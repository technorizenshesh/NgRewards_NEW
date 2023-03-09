package main.com.ngrewards.restapi;

import java.util.concurrent.TimeUnit;
import main.com.ngrewards.constant.BaseUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by technorizen on 14/2/18.
 */

public class ApiClient {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
    //https://myngrewards.com/demo/wp-content/plugins/webservice/category_lists.php
    public static final String BASE_URL = BaseUrl.baseurl;
    private static Retrofit retrofit = null;
    private static ApiInterface apiInterface = null;
    static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .readTimeout(100,TimeUnit.SECONDS).build();
      public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getApiInterface(){
       if (apiInterface==null)
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface;
    }
}
