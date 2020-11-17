package com.example.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.db.dao.AnswersDao
import com.example.myapplication.data.db.dao.QuestionsDao
import com.example.myapplication.data.db.entities.AnswerEntity
import com.example.myapplication.data.db.entities.QuestionEntity

@Database(
    entities = [QuestionEntity::class, AnswerEntity::class],
    version = 4,
    exportSchema = false
)
abstract class XmDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionsDao

    abstract fun answerDao(): AnswersDao

}