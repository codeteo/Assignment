package com.example.myapplication.data.mappers

import com.example.myapplication.data.db.entities.QuestionEntity
import com.example.myapplication.models.Question

class QuestionMapper : Mapper<Question, QuestionEntity> {

    override fun mapTo(t: Question): QuestionEntity {
        return QuestionEntity(t.id, t.questionText)
    }

    override fun mapToModel(r: QuestionEntity): Question  {
        return Question(r.id, r.text)
    }

}