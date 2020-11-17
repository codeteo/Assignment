package com.example.myapplication.data.repository

import com.example.myapplication.data.api.ApiService
import com.example.myapplication.data.db.XmDatabase
import com.example.myapplication.data.db.dao.AnswersDao
import com.example.myapplication.data.db.dao.QuestionsDao
import com.example.myapplication.data.db.entities.AnswerEntity
import com.example.myapplication.data.db.entities.QuestionEntity
import com.example.myapplication.data.mappers.Mapper
import com.example.myapplication.models.Answer
import com.example.myapplication.models.Question
import com.example.myapplication.utils.schedulers.BaseSchedulerProvider
import com.example.myapplication.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainRepositoryTest {

    @Mock
    lateinit var apiService: ApiService

    lateinit var schedulerProvider: BaseSchedulerProvider

    @Mock
    lateinit var xmDatabase: XmDatabase

    @Mock
    lateinit var questionsDao: QuestionsDao

    @Mock
    lateinit var answersDao: AnswersDao

    @Mock
    lateinit var questionsMapper: Mapper<Question, QuestionEntity>

    @Mock
    lateinit var answersMapper: Mapper<Answer, AnswerEntity>

    private lateinit var repository: MainRepository

    @Before
    fun setUp() {
        schedulerProvider = ImmediateSchedulerProvider()

        repository = MainRepository(
            apiService, schedulerProvider, xmDatabase,
            questionsDao, answersDao, questionsMapper, answersMapper
        )
    }


    @Test
    fun `postAnswer() executes postAnswer request`() {
        // Given
        val id = 1
        val text = "Black"
        `when`(apiService.postAnswer(anyInt(), anyString()))
            .thenReturn(Completable.complete())

        // When
        repository.postAnswer(id, text)

        // Then
        verify(apiService).postAnswer(id, text)
    }

    @Test
    fun `saveAnswer() inserts new Answer in DB`() {
        // Given
        val id = 1
        val text = "Black"
        val answerEntity = AnswerEntity(id, text)
        `when`(answersDao.insert(answerEntity))
            .thenReturn(Completable.complete())

        // When
        repository.saveAnswer(id, text)

        // Then
        verify(answersDao).insert(answerEntity)
    }

    @Test
    fun `clearAllTables() calls DB to clearAllTables`() {
        // When
        repository.clearAllTables().test().apply {
            assertComplete()

            // Then
            verify(xmDatabase).clearAllTables()
        }
    }

    @Test
    fun `fetchAndSaveQuestions() executes getQuestions request`() {
        // Given
        `when`(apiService.getQuestions())
            .thenReturn(Observable.just(emptyList()))

        // When
        repository.fetchAndSaveQuestions().test().apply {
            // Then
            assertComplete()
        }

    }

    @Test
    fun `fetchAndSaveQuestions() inserts questions in DB`() {
        // Given
        `when`(apiService.getQuestions())
            .thenReturn(Observable.just(createQuestions()))

        // When
        repository.fetchAndSaveQuestions().test().apply {
            // Then
            assertComplete()

            verify(questionsDao).insert(anyList())
        }

    }

    @Test
    fun `getAllQuestions() retrieves questions from DB`() {
        // Given
        `when`(questionsDao.getAll())
            .thenReturn(Single.just(createQuestionEntities()))

        // When
        repository.getAllQuestions().test().apply {
            // Then
            assertComplete()

            verify(questionsDao).getAll()
        }

    }

    private fun createQuestions(): List<Question> {
        return listOf(
            Question(1, "Red"),
            Question(2, "Black"),
            Question(3, "Green")
        )
    }

    private fun createQuestionEntities(): List<QuestionEntity> {
        return listOf(
            QuestionEntity(1, "Red"),
            QuestionEntity(2, "Black"),
            QuestionEntity(3, "Green")
        )
    }

}