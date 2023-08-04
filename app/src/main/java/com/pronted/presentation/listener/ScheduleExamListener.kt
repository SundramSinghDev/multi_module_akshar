package com.pronted.presentation.listener

interface ScheduleExamListener<T> {

    fun onDelete(item: T, position: Int)
    fun onEdit(item: T, position: Int)
}