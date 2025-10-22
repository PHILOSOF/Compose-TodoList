package com.myapp.simpletodo.di

import android.content.Context
import com.myapp.simpletodo.data.local.TaskDatabase
import com.myapp.simpletodo.data.repository.TaskRepositoryImpl
import com.myapp.simpletodo.domain.repository.TaskRepository
import com.myapp.simpletodo.domain.usecase.AddTaskUseCase
import com.myapp.simpletodo.domain.usecase.TaskUseCases
import com.myapp.simpletodo.domain.usecase.DeleteTaskUseCase
import com.myapp.simpletodo.domain.usecase.GetTaskByIdUseCase
import com.myapp.simpletodo.domain.usecase.GetTasksUseCase
import com.myapp.simpletodo.domain.usecase.ToggleTaskCompletedUseCase
import com.myapp.simpletodo.domain.usecase.UpdateTaskUseCase


interface AppContainer {
    val taskRepository: TaskRepository
    val taskUseCases: TaskUseCases
}


class DefaultAppContainer(private val context: Context) : AppContainer {

    private val taskDatabase: TaskDatabase by lazy {
        TaskDatabase.getDatabase(context)
    }

    override val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(taskDatabase.taskDao())
    }

    override val taskUseCases: TaskUseCases by lazy {
        TaskUseCases(
            getTasksUseCase = GetTasksUseCase(taskRepository),
            getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository),
            addTaskUseCase = AddTaskUseCase(taskRepository),
            updateTaskUseCase = UpdateTaskUseCase(taskRepository),
            deleteTaskUseCase = DeleteTaskUseCase(taskRepository),
            toggleTaskCompletedUseCase = ToggleTaskCompletedUseCase(taskRepository)
        )
    }
}