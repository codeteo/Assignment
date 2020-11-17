package com.example.myapplication.ui.questions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.models.Answer
import com.example.myapplication.ui.questions.state.NetworkResult.*
import com.example.myapplication.ui.questions.state.QuestionIntent
import com.example.myapplication.ui.questions.state.QuestionIntent.*
import com.example.myapplication.ui.questions.state.QuestionsUiState
import com.example.myapplication.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class QuestionsViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _isEmptyTextError: SingleLiveEvent<Any> = SingleLiveEvent()
    val isEmptyTextError: LiveData<Any> get() = _isEmptyTextError

    private val _uiState: MutableLiveData<QuestionsUiState> = MutableLiveData()
    val uiState: LiveData<QuestionsUiState>
        get() = _uiState

    fun handleIntent(intent: QuestionIntent) {
        when (intent) {
            is GetFirstQuestion -> {
                firstQuestion()
            }
            is PostAnswer -> {
                postAnswer(intent.text)
            }
            is GetNextQuestion -> {
                nextQuestion()
            }
            is GetPreviousQuestion -> {
                prevQuestion()
            }
            is RetryRequest -> {
                postAnswer(intent.text)
            }
        }
    }

    private fun nextQuestion() {
        uiState.value?.let {
            val questionsSize = uiState.value!!.questions.size
            var index = uiState.value!!.index

            if (index in 0..questionsSize - 2) {
                index++
                _uiState.postValue(
                    uiState.value?.copy(
                        index = index,
                        networkResult = NONE
                    )
                )
            }

        }

    }

    private fun prevQuestion() {
        uiState.value?.let {
            val questionsSize = uiState.value!!.questions.size
            var index = uiState.value!!.index

            if (index in 1 until questionsSize) {
                index--
                _uiState.postValue(
                    uiState.value?.copy(
                        index = index,
                        networkResult = NONE
                    )
                )
            }
        }
    }

    private fun firstQuestion() {
        // 1. Get first question from 'questions' table
        compositeDisposable.add(
            repository.getAllQuestions()
                .subscribe { questions ->
                    _uiState.postValue(
                        QuestionsUiState(
                            false,
                            NONE,
                            0,
                            questions,
                            emptyList()
                        )
                    )
                }
        )
    }

    private fun postAnswer(text: String) {
        if (text.isEmpty()) {
            _isEmptyTextError.call()
            return
        }

        // 1. show loader
        _uiState.postValue(
            uiState.value?.copy(
                isLoading = true
            )
        )

        // 2. get question's id
        val id = uiState.value!!.questions[uiState.value!!.index].id // FIXME: 14/11/2020

        // 3. post answer, save answer in db
        compositeDisposable.add(
            repository.postAnswer(id, text)
                .andThen(repository.saveAnswer(id, text))
                .subscribe(
                    {
                        // 4. update uiState
                        _uiState.postValue(
                            uiState.value?.answers?.plus(Answer(id, text))?.let { answers ->
                                _uiState.value?.copy(
                                    isLoading = false,
                                    networkResult = SUCCESS,
                                    answers = answers
                                )
                            }
                        )
                    },

                    {
                        Timber.i("Throws: %s", it.message)
                        // update state with error
                        _uiState.postValue(
                            _uiState.value?.copy(
                                isLoading = false,
                                networkResult = FAILURE,
                            )
                        )
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}