package com.example.myapplication.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.ui.main.state.MainIntent.GetQuestions
import com.example.myapplication.ui.main.state.UiState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: MainRepository

    private val uiStateObserver: Observer<UiState> = mockk()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = MainViewModel(repository)
        viewModel.uiState.observeForever {
            uiStateObserver
        }
    }

    @Test
    fun `GetQuestions calls clearsAllData and fetchAndSavesQuestions`() {
        // Given
        every {
            repository.clearAllTables()
        }.returns(Completable.complete())

        every {
            repository.fetchAndSaveQuestions()
        }.returns(Completable.complete())

        // When
        viewModel.handleIntent(GetQuestions)

        // Then
        verify {
            repository.clearAllTables()
            repository.fetchAndSaveQuestions()
        }
    }

    @Test
    fun `fetchData() updates state to Complete`() {
        // Given
        every {
            repository.clearAllTables()
        }.returns(Completable.complete())

        every {
            repository.fetchAndSaveQuestions()
        }.returns(Completable.complete())

        //When
        viewModel.handleIntent(GetQuestions)

        // Then
        assertEquals(viewModel.uiState.value, UiState.Complete)
    }

    @Test
    fun `fetchData() updates state to Error`() {
        // Given
        every {
            repository.clearAllTables()
        }.returns(Completable.complete())

        every {
            repository.fetchAndSaveQuestions()
        }.returns(Completable.error(Throwable()))

        //When
        viewModel.handleIntent(GetQuestions)

        // Then
        assertEquals(viewModel.uiState.value, UiState.Error)
    }

}