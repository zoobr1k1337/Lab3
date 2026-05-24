package com.example.lab3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import com.example.lab3.data.repository.TaskRepository
import com.example.lab3.domain.model.TaskItem
import com.example.lab3.domain.model.TaskPriority
import com.example.lab3.domain.model.getDefaultDueDate

data class TasksListUiState(val tasks: List<TaskItem> = emptyList())

data class AddEditTaskUiState(
    val taskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isEditMode: Boolean = false,
    val isCompleted: Boolean = false
)

data class TaskDetailsUiState(val task: TaskItem? = null)

class TasksViewModel(private val repository: TaskRepository) : ViewModel() {

    val tasksListUiState: StateFlow<TasksListUiState> = repository.getAllTasks()
        .map { TasksListUiState(tasks = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TasksListUiState()
        )

    private val _addEditTaskUiState = MutableStateFlow(AddEditTaskUiState())
    val addEditTaskUiState: StateFlow<AddEditTaskUiState> = _addEditTaskUiState.asStateFlow()

    private val _taskDetailsUiState = MutableStateFlow(TaskDetailsUiState())
    val taskDetailsUiState: StateFlow<TaskDetailsUiState> = _taskDetailsUiState.asStateFlow()

    fun prepareNewTask() {
        _addEditTaskUiState.value = AddEditTaskUiState()
    }

    fun prepareEditTask(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                if (task != null) {
                    _addEditTaskUiState.value = AddEditTaskUiState(
                        taskId = task.id,
                        title = task.title,
                        description = task.description,
                        priority = task.priority,
                        isEditMode = true,
                        isCompleted = task.isCompleted
                    )
                }
            }
        }
    }

    fun prepareDetails(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                _taskDetailsUiState.value = TaskDetailsUiState(task = task)
            }
        }
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
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                if (task != null) {
                    repository.updateTask(task.copy(isCompleted = isCompleted))
                }
            }
        }
    }

    fun saveTask() {
        val state = _addEditTaskUiState.value
        if (state.title.isBlank()) return

        val autoDueDate = state.priority.getDefaultDueDate()

        viewModelScope.launch {
            if (state.isEditMode && state.taskId != null) {
                repository.updateTask(
                    TaskItem(
                        id = state.taskId,
                        title = state.title.trim(),
                        description = state.description.trim(),
                        priority = state.priority,
                        isCompleted = state.isCompleted,
                        dueDate = autoDueDate
                    )
                )
            } else {
                repository.insertTask(
                    TaskItem(
                        id = 0,
                        title = state.title.trim(),
                        description = state.description.trim(),
                        priority = state.priority,
                        isCompleted = false,
                        dueDate = autoDueDate
                    )
                )
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                if (task != null) {
                    repository.deleteTask(task)
                }
            }
        }
    }
}