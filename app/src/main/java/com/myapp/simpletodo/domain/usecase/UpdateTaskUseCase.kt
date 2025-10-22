package com.myapp.simpletodo.domain.usecase

import com.myapp.simpletodo.domain.model.Task
import com.myapp.simpletodo.domain.repository.TaskRepository

class UpdateTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            throw IllegalArgumentException("Task title cannot be blank.")
        }
        taskRepository.updateTask(task)
    }
}