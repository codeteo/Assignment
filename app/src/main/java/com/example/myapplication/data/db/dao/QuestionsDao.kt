package com.example.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.myapplication.data.db.entities.QuestionEntity
import io.reactivex.Single

@Dao
interface QuestionsDao {

    @Query("SELECT * FROM questions")
    fun getAll(): Single<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id= :id")
    fun getOne(id: Int): Single<QuestionEntity>

    @Insert(onConflict = REPLACE)
    fun insert(questionEntities: List<QuestionEntity>)

    @Query("SELECT COUNT(*) FROM questions")
    fun getSize(): Int
}