package magentoegypt.locafy.base_app.base;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static Retrofit instance;

    public static Retrofit getInstance() {
        if (null == instance)
            createNewInstance();
        return instance;
    }

    private static void createNewInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client;
       // String credential = Credentials.basic("locafy", "Market2025");
        client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {

                Request originalRequest = chain.request();
                Request requestWithUserAgent = originalRequest.newBuilder()
                      //  .header("Authorization", credential)
                        .addHeader("Cache-Control","no-cache")
                        .addHeader("User-Agent", Objects.requireNonNull(System.getProperty("http.agent")))
                        .build();
                return  chain.proceed(requestWithUserAgent);


            }
        }).build();
     //   OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
     //   httpClient.addInterceptor(logging);
     //   httpClient.dispatcher(dispatcher);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        instance = new Retrofit.Builder()
                .baseUrl(AppUrl.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


    }
}
