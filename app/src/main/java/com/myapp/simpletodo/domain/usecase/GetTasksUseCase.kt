package com.myapp.simpletodo.domain.usecase

import com.myapp.simpletodo.domain.model.Task
import com.myapp.simpletodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getAllTasks()
    }
}