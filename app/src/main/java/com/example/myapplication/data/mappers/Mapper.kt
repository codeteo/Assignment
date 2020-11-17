package com.example.myapplication.data.mappers

interface Mapper<T, R> {

    abstract fun mapTo(t: T) : R

    abstract fun mapToModel(r: R) : T

}