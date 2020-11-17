package com.example.myapplication.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class AnswerEntity(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val text : String
)
