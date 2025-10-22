package com.myapp.simpletodo.presentation.screens.edit_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.simpletodo.domain.usecase.TaskUseCases
import com.myapp.simpletodo.presentation.navigation.EditTaskDestination
import com.myapp.simpletodo.presentation.screens.common.TaskDetails
import com.myapp.simpletodo.presentation.screens.common.TaskUiState
import com.myapp.simpletodo.presentation.screens.common.toTask
import com.myapp.simpletodo.presentation.screens.common.toTaskUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private val taskId: Int = checkNotNull(savedStateHandle[EditTaskDestination.TASK_ID_ARG])

    init {
        viewModelScope.launch {
            taskUiState = taskUseCases.getTaskByIdUseCase(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(isEntryValid = true)
        }
    }

    fun updateUiState(newTaskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = newTaskDetails, isEntryValid = validateInput(newTaskDetails))
    }

    suspend fun updateTask() {
        if (validateInput(taskUiState.taskDetails)) {
            taskUseCases.updateTaskUseCase(taskUiState.taskDetails.toTask())
        }
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}