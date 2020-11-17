package com.example.myapplication.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.myapplication.data.db.entities.AnswerEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AnswersDao {

    @Query("SELECT * FROM answers WHERE id= :id")
    fun getOne(id: Int): Single<AnswerEntity>

    @Query("SELECT * FROM answers")
    fun getAll(): Single<List<AnswerEntity>>

    @Insert(onConflict = REPLACE)
    fun insert(answerEntity: AnswerEntity): Completable

}