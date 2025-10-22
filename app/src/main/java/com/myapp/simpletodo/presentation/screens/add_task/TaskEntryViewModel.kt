package com.myapp.simpletodo.presentation.screens.add_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.myapp.simpletodo.domain.usecase.TaskUseCases
import com.myapp.simpletodo.presentation.screens.common.TaskDetails
import com.myapp.simpletodo.presentation.screens.common.TaskUiState
import com.myapp.simpletodo.presentation.screens.common.toTask

class TaskEntryViewModel(private val taskUseCases: TaskUseCases) : ViewModel() {
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(newTaskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = newTaskDetails, isEntryValid = validateInput(newTaskDetails))
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskUseCases.addTaskUseCase(taskUiState.taskDetails.toTask())
        }
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}