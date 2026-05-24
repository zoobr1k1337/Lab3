package com.example.lab3.data.repository

import com.example.lab3.data.local.dao.TaskDao
import com.example.lab3.data.mapper.toTaskEntity
import com.example.lab3.data.mapper.toTaskItem
import com.example.lab3.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<TaskItem>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toTaskItem() }
        }
    }

    fun getTaskById(id: Int): Flow<TaskItem?> {
        return taskDao.getTaskById(id).map { it?.toTaskItem() }
    }

    suspend fun insertTask(task: TaskItem) {
        taskDao.insertTask(task.toTaskEntity())
    }

    suspend fun updateTask(task: TaskItem) {
        taskDao.updateTask(task.toTaskEntity())
    }

    suspend fun deleteTask(task: TaskItem) {
        taskDao.deleteTask(task.toTaskEntity())
    }
}