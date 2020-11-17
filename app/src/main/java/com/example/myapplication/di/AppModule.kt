package com.example.myapplication.di

import com.example.myapplication.data.api.ApiService
import com.example.myapplication.data.db.XmDatabase
import com.example.myapplication.data.db.dao.AnswersDao
import com.example.myapplication.data.db.dao.QuestionsDao
import com.example.myapplication.data.mappers.AnswerMapper
import com.example.myapplication.data.mappers.QuestionMapper
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.schedulers.BaseSchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        apiService: ApiService,
        schedulerProvider: BaseSchedulerProvider,
        xmDatabase: XmDatabase,
        questionsDao: QuestionsDao,
        answersDao: AnswersDao,
        questionMapper: QuestionMapper,
        answerMapper: AnswerMapper
    ): MainRepository {
        return MainRepository(
            apiService,
            schedulerProvider,
            xmDatabase,
            questionsDao,
            answersDao,
            questionMapper,
            answerMapper
        )
    }

    @Provides
    @Singleton
    fun providesQuestionMapper(): QuestionMapper = QuestionMapper()

    @Provides
    @Singleton
    fun providesAnswerMapper(): AnswerMapper = AnswerMapper()

}