package com.example.myapplication.di

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.data.api.ApiService
import com.example.myapplication.data.db.XmDatabase
import com.example.myapplication.data.db.dao.AnswersDao
import com.example.myapplication.data.db.dao.QuestionsDao
import com.example.myapplication.utils.schedulers.BaseSchedulerProvider
import com.example.myapplication.utils.schedulers.ImmediateSchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class TestAppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://localhost:8080/")
            .client(okHttpClient)
            .build()
    }

/*
    @Singleton
    @Provides
    fun provideMockServer() = MockWebServer()
*/

    @Provides
    @Singleton
    fun providesSchedulers(): BaseSchedulerProvider {
        return ImmediateSchedulerProvider()
    }

    @Provides
    @Singleton
    fun providesDatabase(): XmDatabase {
        return Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            XmDatabase::class.java
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providesQuestionDao(xmDatabase: XmDatabase): QuestionsDao {
        return xmDatabase.questionDao()
    }

    @Provides
    @Singleton
    fun providesAnswersDao(xmDatabase: XmDatabase): AnswersDao {
        return xmDatabase.answerDao()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
