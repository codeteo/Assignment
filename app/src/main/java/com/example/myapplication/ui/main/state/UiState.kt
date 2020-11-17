package com.example.myapplication.ui.main.state

sealed class UiState {

    object Complete : UiState()

    object Loading : UiState()

    object Error : UiState()
}