package com.myapp.simpletodo.presentation.screens.task_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.simpletodo.domain.usecase.TaskUseCases
import com.myapp.simpletodo.presentation.navigation.TaskDetailsDestination
import com.myapp.simpletodo.presentation.screens.common.TaskUiState
import com.myapp.simpletodo.presentation.screens.common.toTask
import com.myapp.simpletodo.presentation.screens.common.toTaskUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class TaskDetailsUiState(
    val taskDetails: TaskUiState = TaskUiState(),
    val isTaskLoading: Boolean = true
)

class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle[TaskDetailsDestination.TASK_ID_ARG])

    val uiState: StateFlow<TaskDetailsUiState> =
        taskUseCases.getTaskByIdUseCase(taskId)
            .filterNotNull()
            .map { task ->
                TaskDetailsUiState(taskDetails = task.toTaskUiState(), isTaskLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TaskDetailsUiState(isTaskLoading = true)
            )

    suspend fun deleteTask() {
        if (!uiState.value.isTaskLoading) {
            val taskToDelete = uiState.value.taskDetails.taskDetails
            taskUseCases.deleteTaskUseCase(
                taskToDelete.copy(id = taskId).toTask()
            )
        }
    }

    fun toggleTaskCompleted() {
        if (!uiState.value.isTaskLoading) {
            viewModelScope.launch {
                val currentTaskDetails = uiState.value.taskDetails.taskDetails
                val taskToToggle = currentTaskDetails.toTask()
                taskUseCases.toggleTaskCompletedUseCase(taskToToggle)
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}