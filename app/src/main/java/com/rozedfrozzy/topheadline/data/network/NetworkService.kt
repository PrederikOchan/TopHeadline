package com.rozedfrozzy.topheadline.data.network

import com.rozedfrozzy.topheadline.BuildConfig
import com.rozedfrozzy.topheadline.data.response.NewsResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

//https://newsapi.org/v2/top-headlines?country=id&apiKey=8f973ce15f5341319e5dfbeb97c3f947
const val API_KEY = BuildConfig.NEWS_API_KEY

class NetworkService {
    private var mRetrofit: Retrofit? = null

    fun fetchTopHeadline(page: Int): Single<NewsResponse>? {
        return  getRetrofit()?.create(NetworkService.ApiServices::class.java)?.getTopHeadline(page)
    }

    fun fetchNews(query: String, page: Int): Single<NewsResponse>? {
        return getRetrofit()?.create(NetworkService.ApiServices::class.java)?.getNewsQueries(query, page)
    }

    private fun getRetrofit(): Retrofit? {
        if (mRetrofit == null) {

            val requestInterceptor = Interceptor {chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            mRetrofit = Retrofit
                .Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        }
        return mRetrofit
    }

    interface ApiServices {

        @GET("top-headlines")
        fun getTopHeadline(
            @Query("page") page: Int,
            @Query("category") category: String = "general",
            @Query("country") country: String = "us",
            @Query("pageSize") pageSize: Int = 10
        ): Single<NewsResponse>

        @GET("everything")
        fun getNewsQueries(
            @Query("q") query: String,
            @Query("page") page: Int,
            @Query("language") language: String = "en",
            @Query("pageSize") pageSize: Int = 10
        ): Single<NewsResponse>
    }
}