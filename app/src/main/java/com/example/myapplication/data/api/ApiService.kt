package com.example.myapplication.data.api

import com.example.myapplication.models.Question
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("questions")
    fun getQuestions(): Observable<List<Question>>

    @FormUrlEncoded
    @POST("question/submit")
    fun postAnswer(
        @Field("id") id: Int,
        @Field("answer") answer: String
    ): Completable
}