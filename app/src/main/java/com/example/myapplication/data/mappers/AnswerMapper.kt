package com.example.myapplication.data.mappers

import com.example.myapplication.data.db.entities.AnswerEntity
import com.example.myapplication.models.Answer

class AnswerMapper : Mapper<Answer, AnswerEntity> {

    override fun mapTo(t: Answer): AnswerEntity {
        return AnswerEntity(t.id, t.answerText)
    }

    override fun mapToModel(r: AnswerEntity): Answer {
        return Answer(r.id, r.text)
    }

}