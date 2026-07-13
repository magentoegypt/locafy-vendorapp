package magentoegypt.locafy.addons.advance_Report.appBase

import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    var baseUrl: String? = null
    private var instance: Retrofit? = null
    fun getInstance(url: String?): Retrofit? {
        baseUrl = url
        if (null == instance) createNewInstance()
        return instance
    }

    private fun createNewInstance() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val basicAuthInterceptor = Interceptor { chain ->
            val originalRequest: Request = chain.request()
          //  val credential = Credentials.basic("locafy", "Market2025")  // Automatically Base64 encodes

            val newRequest = originalRequest.newBuilder()
             //   .header("Authorization", credential)
                .header("Content-Type", "application/json")
                .build()

            chain.proceed(newRequest)
        }
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(basicAuthInterceptor)

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        val gson = GsonBuilder()
            .setLenient()
            .create()
        instance = Retrofit.Builder()
            .baseUrl(baseUrl!!)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }
}
