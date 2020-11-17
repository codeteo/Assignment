package com.example.myapplication.ui.main

import android.annotation.SuppressLint
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.ui.main.state.MainIntent
import com.example.myapplication.ui.main.state.MainIntent.GetQuestions
import com.example.myapplication.ui.main.state.UiState
import com.example.myapplication.ui.main.state.UiState.*
import io.reactivex.disposables.CompositeDisposable

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    val uiState: LiveData<UiState>
        get() = _uiState

    fun handleIntent(mainIntent: MainIntent) {
        when (mainIntent) {
            GetQuestions -> fetchData()
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchData() {
        _uiState.value = Loading

        // 1. clear all from db
        compositeDisposable.add(
            repository.clearAllTables()
                .andThen(repository.fetchAndSaveQuestions())
                .subscribe(
                    {
                        _uiState.postValue(Complete)
                    },
                    {
                        _uiState.postValue(Error)
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}