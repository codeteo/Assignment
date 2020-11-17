package com.example.myapplication.ui.questions.state

sealed class QuestionIntent {

    object GetFirstQuestion: QuestionIntent()

    class PostAnswer(val text: String): QuestionIntent()

    object GetNextQuestion : QuestionIntent()

    object GetPreviousQuestion : QuestionIntent()

    class RetryRequest(val text: String) : QuestionIntent()

}