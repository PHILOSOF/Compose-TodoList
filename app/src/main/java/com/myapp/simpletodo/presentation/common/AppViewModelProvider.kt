package com.myapp.simpletodo.presentation.common

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.myapp.simpletodo.TaskApplication
import com.myapp.simpletodo.presentation.screens.add_task.TaskEntryViewModel
import com.myapp.simpletodo.presentation.screens.edit_task.TaskEditViewModel
import com.myapp.simpletodo.presentation.screens.home.HomeViewModel
import com.myapp.simpletodo.presentation.screens.task_details.TaskDetailsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                taskUseCases = taskApplication().container.taskUseCases
            )
        }

        initializer {
            TaskEntryViewModel(
                taskUseCases = taskApplication().container.taskUseCases
            )
        }

        initializer {
            TaskDetailsViewModel(
                savedStateHandle = createSavedStateHandle(),
                taskUseCases = taskApplication().container.taskUseCases
            )
        }

        initializer {
            TaskEditViewModel(
                savedStateHandle = createSavedStateHandle(),
                taskUseCases = taskApplication().container.taskUseCases
            )
        }
    }
}


fun CreationExtras.taskApplication(): TaskApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)