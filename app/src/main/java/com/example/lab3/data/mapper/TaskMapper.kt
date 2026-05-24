package com.example.lab3.data.mapper

import com.example.lab3.data.local.entity.TaskEntity
import com.example.lab3.domain.model.TaskItem
import com.example.lab3.domain.model.TaskPriority

fun TaskEntity.toTaskItem(): TaskItem {
    return TaskItem(
        id = this.id,
        title = this.title,
        description = this.description,
        priority = TaskPriority.valueOf(this.priority),
        isCompleted = this.isCompleted,
        dueDate = this.dueDate
    )
}

fun TaskItem.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        priority = this.priority.name,
        isCompleted = this.isCompleted,
        dueDate = this.dueDate
    )
}