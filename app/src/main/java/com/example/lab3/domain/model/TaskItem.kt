package com.example.lab3.domain.model

import androidx.compose.ui.graphics.Color

data class TaskItem(
    val id: Int,
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val isCompleted: Boolean,
    val dueDate: String = ""
)

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}

fun TaskPriority.toDisplayName(): String {
    return when (this) {
        TaskPriority.LOW -> "Низький"
        TaskPriority.MEDIUM -> "Середній"
        TaskPriority.HIGH -> "Високий"
    }
}

fun TaskPriority.getIndicatorColor(): Color {
    return when (this) {
        TaskPriority.LOW -> Color(0xFF4CAF50)
        TaskPriority.MEDIUM -> Color(0xFFFF9800)
        TaskPriority.HIGH -> Color(0xFFF44336)
    }
}

fun TaskPriority.getBadgeBackgroundColor(): Color {
    return when (this) {
        TaskPriority.LOW -> Color(0xFFC8E6C9)
        TaskPriority.MEDIUM -> Color(0xFFFFE0B2)
        TaskPriority.HIGH -> Color(0xFFFFCDD2)
    }
}

fun TaskPriority.getBadgeTextColor(): Color {
    return when (this) {
        TaskPriority.LOW -> Color(0xFF2E7D32)
        TaskPriority.MEDIUM -> Color(0xFFE65100)
        TaskPriority.HIGH -> Color(0xFFC62828)
    }
}

fun TaskPriority.getDefaultDueDate(): String {
    return when (this) {
        TaskPriority.HIGH -> "До завтра"
        TaskPriority.MEDIUM -> "На цей тиждень"
        TaskPriority.LOW -> "При можливості"
    }
}