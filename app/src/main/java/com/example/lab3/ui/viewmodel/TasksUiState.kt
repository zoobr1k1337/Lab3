package com.example.lab3.ui.viewmodel

import com.example.lab3.data.TaskItem
import com.example.lab3.data.TaskPriority

data class TasksListUiState(
    val tasks: List<TaskItem> = emptyList()
)

data class AddEditTaskUiState(
    val taskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isEditMode: Boolean = false
)

data class TaskDetailsUiState(
    val task: TaskItem? = null
)
