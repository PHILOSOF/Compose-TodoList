package com.myapp.simpletodo.presentation.screens.common

import com.myapp.simpletodo.domain.model.Task

data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false
)

fun TaskDetails.toTask(): Task = Task(
    id = id,
    title = title.trim(),
    description = description.trim(),
    isCompleted = isCompleted
)

fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)