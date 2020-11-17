package com.example.myapplication.di

import com.example.myapplication.data.api.ApiService
import com.example.myapplication.utils.RequestInterceptor
import com.example.myapplication.utils.schedulers.BaseSchedulerProvider
import com.example.myapplication.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    private val BASE_URL = "https://powerful-peak-54206.herokuapp.com/".toHttpUrlOrNull()!!
    private val CONNECTION_TIMEOUT = 15L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.retryOnConnectionFailure(true)
        client.addInterceptor(interceptor)
        client.addInterceptor(RequestInterceptor())
        client.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesSchedulers(): BaseSchedulerProvider {
        return SchedulerProvider.getInstance()
    }

}