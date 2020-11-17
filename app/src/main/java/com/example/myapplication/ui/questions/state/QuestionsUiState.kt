package com.example.myapplication.ui.questions.state

import com.example.myapplication.models.Answer
import com.example.myapplication.models.Question

data class QuestionsUiState(
    val isLoading: Boolean = false,
    val networkResult: NetworkResult,
    val index: Int = 0,
    val questions: List<Question>,
    val answers: List<Answer>
)

sealed class NetworkResult {
    object SUCCESS : NetworkResult()
    object FAILURE : NetworkResult()
    object NONE : NetworkResult()
}