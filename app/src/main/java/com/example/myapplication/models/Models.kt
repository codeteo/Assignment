package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Question(

    @SerializedName("id")
    val id: Int,

    @SerializedName("question")
    val questionText: String,
) {
    override fun toString(): String {
        return "Question(title=$questionText)"
    }
}

data class Answer(

    @SerializedName("id")
    val id: Int,

    @SerializedName("text")
    val answerText: String,
) {
    override fun toString(): String {
        return "Answer(title=$answerText)"
    }
}