package com.myapp.simpletodo.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.simpletodo.domain.model.Task
import com.myapp.simpletodo.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val taskList: List<Task> = listOf(),
    val isLoading: Boolean = true
)

class HomeViewModel(private val taskUseCases: TaskUseCases) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        taskUseCases.getTasksUseCase()
            .map { tasks -> HomeUiState(taskList = tasks, isLoading = false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState(isLoading = true)
            )


    fun toggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            taskUseCases.toggleTaskCompletedUseCase(task)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}