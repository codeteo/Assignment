package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.db.XmDatabase
import com.example.myapplication.data.db.dao.AnswersDao
import com.example.myapplication.data.db.dao.QuestionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): XmDatabase {
        return Room.databaseBuilder(
            context,
            XmDatabase::class.java, "xm.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesQuestionDao(xmDatabase: XmDatabase): QuestionsDao {
        return xmDatabase.questionDao()
    }

    @Provides
    @Singleton
    fun providesAnswersDao(xmDatabase: XmDatabase): AnswersDao {
        return xmDatabase.answerDao()
    }

}