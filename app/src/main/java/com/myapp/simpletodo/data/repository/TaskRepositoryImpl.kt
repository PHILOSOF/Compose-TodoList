package com.myapp.simpletodo.data.repository

import com.myapp.simpletodo.data.local.dao.TaskDao
import com.myapp.simpletodo.data.local.model.toDomain
import com.myapp.simpletodo.data.local.model.toEntity
import com.myapp.simpletodo.domain.model.Task
import com.myapp.simpletodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTaskById(id: Int): Flow<Task?> {
        return taskDao.getTaskById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun toggleTaskCompleted(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        taskDao.updateTask(updatedTask.toEntity())
    }
}