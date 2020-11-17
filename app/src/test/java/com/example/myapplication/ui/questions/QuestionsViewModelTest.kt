package com.example.myapplication.ui.questions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.models.Question
import com.example.myapplication.ui.questions.state.QuestionIntent.GetFirstQuestion
import com.example.myapplication.ui.questions.state.QuestionsUiState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuestionsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: MainRepository

    private val uiStateObserver: Observer<QuestionsUiState> = mockk()

    private lateinit var viewModel: QuestionsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = QuestionsViewModel(repository)
        viewModel.uiState.observeForever {
            uiStateObserver
        }
    }

    @Test
    fun `GetFirstQuestion calls repository to getAllQuestions `() {
        // Given
        every {
            repository.getAllQuestions()
        }.returns(Single.just(createQuestions()))

        // When
        viewModel.handleIntent(GetFirstQuestion)

        // Then
        verify { repository.getAllQuestions() }
    }

    private fun createQuestions(): List<Question> {
        return listOf(
            Question(1, "Black"),
            Question(2, "Red"),
            Question(3, "Green")
        )
    }

}