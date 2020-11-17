package com.example.myapplication.data.repository

import com.example.myapplication.data.api.ApiService
import com.example.myapplication.data.db.XmDatabase
import com.example.myapplication.data.db.dao.AnswersDao
import com.example.myapplication.data.db.dao.QuestionsDao
import com.example.myapplication.data.db.entities.AnswerEntity
import com.example.myapplication.data.db.entities.QuestionEntity
import com.example.myapplication.data.mappers.Mapper
import com.example.myapplication.data.repository.MainRepository.Constants.NUM_OF_RETRIES
import com.example.myapplication.data.repository.MainRepository.Constants.RETRY_DELAY
import com.example.myapplication.models.Answer
import com.example.myapplication.models.Question
import com.example.myapplication.utils.RetryWithDelay
import com.example.myapplication.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val schedulerProvider: BaseSchedulerProvider,
    private val xmDatabase: XmDatabase,
    private val questionsDao: QuestionsDao,
    private val answersDao: AnswersDao,
    private val questionsMapper: Mapper<Question, QuestionEntity>,
    private val answersMapper: Mapper<Answer, AnswerEntity>
) {

    object Constants {
        const val NUM_OF_RETRIES = 3
        const val RETRY_DELAY = 300
    }

    fun clearAllTables(): Completable {
        return Completable.fromAction { xmDatabase.clearAllTables() }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
    }

    fun fetchAndSaveQuestions(): Completable {
        return apiService.getQuestions()
            .retryWhen(RetryWithDelay(NUM_OF_RETRIES, RETRY_DELAY))
            .map {
                    questions -> questionsDao.insert(questions.map { questionsMapper.mapTo(it) })
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .ignoreElements()
    }

    fun getAllQuestions(): Single<List<Question>> {
        return questionsDao.getAll()
            .map { question ->
                question.map { questionsMapper.mapToModel(it) }
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
    }

    fun postAnswer(questionId: Int, text: String): Completable {
        return apiService.postAnswer(questionId, text)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
    }

    fun saveAnswer(id: Int, text: String) : Completable {
        return answersDao.insert(AnswerEntity(id, text))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
    }

}