package com.example.lab3.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.lab3.data.TaskItem
import com.example.lab3.data.TaskPriority
import com.example.lab3.data.getDefaultDueDate

class TasksViewModel : ViewModel() {

    private val _tasks = MutableStateFlow(
        listOf(
            TaskItem(1, "Підготовка презентації", "Підготувати слайди та матеріали", TaskPriority.HIGH, false, "До завтра"),
            TaskItem(2, "Написати звіт", "Звіт за результатами тижня", TaskPriority.MEDIUM, false, "На цей тиждень"),
            TaskItem(3, "Оновити контакти", "Синхронізувати адресну книгу", TaskPriority.LOW, false, "При можливості")
        )
    )

    private val _tasksListUiState = MutableStateFlow(TasksListUiState(tasks = _tasks.value))
    val tasksListUiState: StateFlow<TasksListUiState> = _tasksListUiState.asStateFlow()

    private val _addEditTaskUiState = MutableStateFlow(AddEditTaskUiState())
    val addEditTaskUiState: StateFlow<AddEditTaskUiState> = _addEditTaskUiState.asStateFlow()

    private val _taskDetailsUiState = MutableStateFlow(TaskDetailsUiState())
    val taskDetailsUiState: StateFlow<TaskDetailsUiState> = _taskDetailsUiState.asStateFlow()

    fun prepareNewTask() {
        _addEditTaskUiState.value = AddEditTaskUiState(
            taskId = null,
            title = "",
            description = "",
            priority = TaskPriority.MEDIUM,
            isEditMode = false
        )
    }

    fun prepareEditTask(taskId: Int) {
        val task = _tasks.value.firstOrNull { it.id == taskId }
        if (task != null) {
            _addEditTaskUiState.value = AddEditTaskUiState(
                taskId = task.id,
                title = task.title,
                description = task.description,
                priority = task.priority,
                isEditMode = true
            )
        }
    }

    fun prepareDetails(taskId: Int) {
        val task = _tasks.value.firstOrNull { it.id == taskId }
        _taskDetailsUiState.value = TaskDetailsUiState(task = task)
    }

    fun updateTitle(title: String) {
        _addEditTaskUiState.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _addEditTaskUiState.update { it.copy(description = description) }
    }

    fun updatePriority(priority: TaskPriority) {
        _addEditTaskUiState.update { it.copy(priority = priority) }
    }

    fun updateCompleted(taskId: Int, isCompleted: Boolean) {
        _tasks.update { currentTasks ->
            currentTasks.map { if (it.id == taskId) it.copy(isCompleted = isCompleted) else it }
        }
        prepareDetails(taskId)
        refreshListState()
    }

    fun saveTask() {
        val state = _addEditTaskUiState.value
        if (state.title.isBlank()) return

        val autoDueDate = state.priority.getDefaultDueDate()

        if (state.isEditMode) {
            val taskId = state.taskId ?: return
            _tasks.update { current ->
                current.map {
                    if (it.id == taskId) it.copy(
                        title = state.title.trim(),
                        description = state.description.trim(),
                        priority = state.priority,
                        dueDate = autoDueDate
                    ) else it
                }
            }
        } else {
            val newTask = TaskItem(
                id = (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1,
                title = state.title.trim(),
                description = state.description.trim(),
                priority = state.priority,
                isCompleted = false,
                dueDate = autoDueDate
            )
            _tasks.update { it + newTask }
        }
        refreshListState()
    }

    fun deleteTask(taskId: Int) {
        _tasks.update { it.filterNot { task -> task.id == taskId } }
        refreshListState()
    }

    private fun refreshListState() {
        _tasksListUiState.value = TasksListUiState(tasks = _tasks.value)
    }
}