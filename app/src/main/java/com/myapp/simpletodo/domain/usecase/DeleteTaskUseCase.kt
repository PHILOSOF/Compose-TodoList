package com.myapp.simpletodo.domain.usecase

import com.myapp.simpletodo.domain.model.Task
import com.myapp.simpletodo.domain.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        taskRepository.deleteTask(task)
    }
}