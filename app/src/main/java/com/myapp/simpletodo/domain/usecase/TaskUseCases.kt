package com.myapp.simpletodo.domain.usecase

data class TaskUseCases(
    val getTasksUseCase: GetTasksUseCase,
    val getTaskByIdUseCase: GetTaskByIdUseCase,
    val addTaskUseCase: AddTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val toggleTaskCompletedUseCase: ToggleTaskCompletedUseCase
)